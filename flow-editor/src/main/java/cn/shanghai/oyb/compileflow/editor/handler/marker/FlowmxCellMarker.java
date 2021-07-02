/*
 * Copyright (c) 2021. Ou Yubin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.shanghai.oyb.compileflow.editor.handler.marker;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.jgraphx.handler.marker.CellMarker;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

/**
 * 图形单元标记,用于处理单元移动时标记处理
 *
 * @author ouyubin
 */
public class FlowmxCellMarker extends CellMarker {

    private static final Logger LOG = Logger.getInstance(FlowmxCellMarker.class);

    private MarkLocationEnum markLocationEnum;

    public FlowmxCellMarker(mxGraphComponent mxGraphComponent) {
        super(mxGraphComponent);
    }

    public boolean isEnabled() {
        return graphComponent.getGraph().isDropEnabled();
    }

    /**
     * @param e
     * @return
     */
    public Object getCell(MouseEvent e) {
        mxIGraphModel model = graphComponent.getGraph().getModel();
        TransferHandler th = graphComponent.getTransferHandler();
        boolean isLocal = th instanceof mxGraphTransferHandler
                && ((mxGraphTransferHandler) th).isLocalDrag();

        mxGraph graph = graphComponent.getGraph();

        //--源单元
        Object[] selectionCells = graph.getSelectionCells();
        //LOG.info("源(拖)资源信息=>" + selectionCells[0].toString());


        //--目标单元
        Object cell = super.getCell(e);
        if (cell != null) {
            //LOG.info("目标(放)资源类型信息=>" + cell.getClass());
        }

//        if ((selectionCells.length == 1 && selectionCells[0] == cell && cell instanceof MetaDataClassCell) || (selectionCells.length == 1 && selectionCells[0] == cell && cell instanceof MetaDataInterfaceCell)) {
//            return cell;
//        }

        //--判断是否为容器内移动的列单元及表单元素单元,表明数据列与表单项在进行重新排列操作
        //--如果进行元素互换直接返回被换位单元对象
//        if ((selectionCells[0] instanceof MetaDataPropertyCell && cell instanceof MetaDataPropertyCell) || (selectionCells[0] instanceof MetaDataOperationCell && cell instanceof MetaDataOperationCell)) {
//            if (selectionCells[0] == cell) {
//                cell = null;
//            }
//            Object parent = graph.getDropTarget(selectionCells, e.getPoint(), cell);
//            if (parent != null) {
//                LOG.info("处理过的目标(放)资源类型信息=>" + cell.toString());
//            }
//            //--守护是否存在父放入子中
//            while (parent != null) {
//                if (mxUtils.contains(selectionCells, parent)) {
//                    return null;
//                }
//                parent = model.getParent(parent);
//            }
//            boolean clone = graphComponent.isCloneEvent(e);
//
//            if (isLocal && cell != null && selectionCells.length > 0 && !clone
//                    && graph.getModel().getParent(selectionCells[0]) == cell) {
//                cell = null;
//            }
        //} else {
        cell = graph.getDropTarget(selectionCells, e.getPoint(), cell);
        Object parent = cell;
        if (parent != null) {
            //LOG.info("处理过的目标(放)资源类型信息=>" + cell.toString());
        }
        //--守护是否存在父放入子中
        while (parent != null) {
            if (mxUtils.contains(selectionCells, parent)) {
                return parent;
            }
            parent = model.getParent(parent);
        }

        boolean clone = graphComponent.isCloneEvent(e);

        if (isLocal && cell != null && selectionCells.length > 0 && !clone
                && graph.getModel().getParent(selectionCells[0]) == cell) {
            cell = null;
        }
        //}
        return cell;
    }


    public mxCellState process(MouseEvent event) {
        mxCellState state = null;

        if (isEnabled()) {
            //--获取拖放目标资源
            state = getState(event);
            if (state != null) {
                boolean valid = (state != null) && isValidState(state);
                Color color = getMarkerColor(event, state, valid);

                BaseCell lftCell = (BaseCell) state.getCell();
                //--目标资源守护
//                if (lftCell instanceof MetaDataPropertyCell ||lftCell instanceof MetaDataOperationCell) {
                highlight(state, color, valid, event);
//                } else {
//                    highlight(state, color, valid);
//                }

            }
        }

        return state;
    }

    @Override
    public void highlight(mxCellState state, Color color, boolean valid) {
        super.highlight(state, color, valid);
    }

    /**
     * 需要针对鼠标放置位置不同进行高亮处理
     *
     * @param state
     * @param color
     * @param valid
     * @param event
     */
    public void highlight(mxCellState state, Color color, boolean valid, MouseEvent event) {
        if (valid) {
            validState = state;
        } else {
            validState = null;
        }


        if (state != markedState || color != currentColor) {
            currentColor = color;

            if (state != null && currentColor != null) {
                markedState = state;
                mark();
            } else if (markedState != null) {
                markedState = null;
                unmark();
            }
        } else {
            if (state != null) {
                //--目标资源单元
                BaseCell lftCell = (BaseCell) state.getCell();

//                if (lftCell instanceof MetaDataPropertyCell||lftCell instanceof MetaDataOperationCell) {
                mxRectangle rectangle = graphComponent.getGraph().getCellBounds(lftCell);

                if(rectangle!=null) {
                    double width = rectangle.getWidth();
                    double height = rectangle.getHeight();
                    double x = rectangle.getX();
                    double centerY = rectangle.getCenterY();
                    double centerX = rectangle.getCenterX();

                    double markRegionPadding = BigDecimal.valueOf(width).divide(BigDecimal.valueOf(3), 5, BigDecimal.ROUND_HALF_UP).doubleValue();

                    double markRegionOfVerticalPadding = BigDecimal.valueOf(height).divide(BigDecimal.valueOf(3), 5, BigDecimal.ROUND_HALF_UP).doubleValue();

                    Point point = event.getPoint();

                    if (point.getX() < centerX + markRegionPadding && point.getX() > centerX - markRegionPadding && point.getY() > centerY - markRegionOfVerticalPadding && point.getY() < centerY + markRegionOfVerticalPadding) {
                        markLocationEnum = MarkLocationEnum.CENTER;
                    } else {
                        if (point.getY() < centerY) {
                            markLocationEnum = MarkLocationEnum.TOP;
                        }
//                        if (point.getX() < x + markRegionPadding) {
//                            markLocationEnum = MarkLocationEnum.LEFT;
//                        }
                        if (point.getY() > centerY) {
                            markLocationEnum = MarkLocationEnum.BOTTOM;
                        }
//                        if (point.getX() > x + width - markRegionPadding) {
//                            markLocationEnum = MarkLocationEnum.RIGHT;
//                        }
                    }
                    markedState = state;
                    mark();
                }
            }
        }
//        else if (markedState != null) {
//                markedState = null;
//                unmark();
//            }
    }
    //}


    public void paint(Graphics g) {
        if (markedState != null && currentColor != null) {
            ((Graphics2D) g).setStroke(DEFAULT_STROKE);
            g.setColor(currentColor);

            //--放置单元
            BaseCell lftCell = (BaseCell) markedState.getCell();
            if (markedState.getAbsolutePointCount() > 0) {
                Point last = markedState.getAbsolutePoint(0).getPoint();

                for (int i = 1; i < markedState.getAbsolutePointCount(); i++) {
                    Point current = markedState.getAbsolutePoint(i).getPoint();
                    g.drawLine(last.x - getX(), last.y - getY(), current.x
                            - getX(), current.y - getY());
                    last = current;
                }
            } else {
//                if (lftCell instanceof MetaDataPropertyCell || lftCell instanceof MetaDataOperationCell) {
                if (markLocationEnum != null) {
                    g.setColor(new JBColor(new Color(213, 0, 0), new Color(16, 116, 65)));
                    if (markLocationEnum == MarkLocationEnum.TOP) {
                        g.drawLine(1 + 3, 1, 1 + getWidth() - 8, 1);
                    } else if (markLocationEnum == MarkLocationEnum.BOTTOM) {
                        g.drawLine(1 + 3, 1 + getHeight() - 6, 1 + getWidth() - 8, 1 + getHeight() - 6);
                    } else if (markLocationEnum == MarkLocationEnum.CENTER) {
                        g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
                    }
                }
            }
            //else {
            //g.setColor(new Color(244, 86, 107));
            //g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            //}
        }
    }


    public MarkLocationEnum getMarkLocationEnum() {
        return markLocationEnum;
    }
}

/**
 * 标注位置枚举定义
 */
enum MarkLocationEnum {

    TOP("top"), LEFT("left"), BOTTOM("bottom"), RIGHT("right"), CENTER("center");

    String location;

    MarkLocationEnum(String location) {
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}



