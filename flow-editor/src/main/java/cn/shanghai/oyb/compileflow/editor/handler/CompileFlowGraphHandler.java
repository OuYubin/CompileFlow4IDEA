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

package cn.shanghai.oyb.compileflow.editor.handler;

import cn.shanghai.oyb.compileflow.editor.commands.factory.CompileFlowCommandFactory;
import cn.shanghai.oyb.compileflow.editor.component.CompileFlowGraphComponent;
import com.intellij.openapi.diagnostic.Logger;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.jgraphx.handler.GraphHandler;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell;
import cn.shanghai.oyb.compileflow.editor.commands.ICompileFlowCommandConstants;
import cn.shanghai.oyb.compileflow.editor.handler.marker.FlowmxCellMarker;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author ouyubin
 */
public class CompileFlowGraphHandler extends GraphHandler {

    private static final Logger LOG = Logger.getInstance(CompileFlowGraphHandler.class);

    private CompileFlowCommandFactory flowCommandFactory;

    private volatile TGraphEditCommand currentCommand;


    public CompileFlowGraphHandler(@NotNull mxGraphComponent graphComponent) {
        super(graphComponent);
        this.setRemoveCellsFromParent(false);
        this.flowCommandFactory = new CompileFlowCommandFactory(graphComponent);

    }

    /**
     * 移动
     *
     * @param cells
     * @param dx
     * @param dy
     * @param target
     * @param event
     */
    @Override
    protected void moveCells(@Nullable Object[] cells, double dx, double dy, @Nullable Object target, @Nullable MouseEvent event) {
        if (cells.length > 0) {
            for (Object object : cells) {
                if (object instanceof CompileFlowBpmCell) {
                    return;
                }
            }
        }
        super.moveCells(cells, dx, dy, target, event);
        for (Object cell : cells) {
            if (cell instanceof BaseCell) {
                //--将命令类型直接给定,后续应根据event类型来获取Command类型
                this.currentCommand = getCommand(ICompileFlowCommandConstants.COMMAND_CONSTRAINT);
                if (currentCommand != null) {
                    currentCommand.setEffectCell((BaseCell) cell);
                    executeCommand();
                }
            }
        }
    }

    //    protected Cursor getCursor(MouseEvent e) {
//        Cursor cursor = null;
//
//
//        if (isMoveEnabled()) {
//            Object cell = graphComponent.getCellAt(e.getX(), e.getY(), false);
//
//            if (cell != null) {
//
//                if (cell instanceof CompileFlowBpmCell) {
//                    cursor = DEFAULT_CURSOR;
//                } else {
//
//                    if (graphComponent.isFoldingEnabled()
//                            && graphComponent.hitFoldingIcon(cell, e.getX(),
//                            e.getY())) {
//                        cursor = FOLD_CURSOR;
//                    } else if (graphComponent.getGraph().isCellMovable(cell)) {
//                        cursor = MOVE_CURSOR;
//                    }
//                }
//            }
//        }
//
//        return cursor;
//    }
    protected Cursor getCursor(MouseEvent e) {
        Cursor cursor = graphComponent.getGraphControl().getCursor();
        return cursor;

    }


    public void mouseReleased(MouseEvent e) {
        if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed()) {
            mxGraph graph = graphComponent.getGraph();
            double dx = 0;
            double dy = 0;

            if (first != null && (cellBounds != null || movePreview.isActive())) {
                double scale = graph.getView().getScale();
                mxPoint trans = graph.getView().getTranslate();

                // TODO: Simplify math below, this was copy pasted from
                // getPreviewLocation with the rounding removed
                dx = e.getX() - first.x;
                dy = e.getY() - first.y;

                if (cellBounds != null) {
                    double dxg = ((cellBounds.getX() + dx) / scale)
                            - trans.getX();
                    double dyg = ((cellBounds.getY() + dy) / scale)
                            - trans.getY();

                    if (gridEnabledEvent) {
                        dxg = graph.snap(dxg);
                        dyg = graph.snap(dyg);
                    }

                    double x = ((dxg + trans.getX()) * scale) + (bbox.getX())
                            - (cellBounds.getX());
                    double y = ((dyg + trans.getY()) * scale) + (bbox.getY())
                            - (cellBounds.getY());

                    dx = Math.round((x - bbox.getX()) / scale);
                    dy = Math.round((y - bbox.getY()) / scale);
                }
            }

            if (first == null
                    || !graphComponent.isSignificant(e.getX() - first.x,
                    e.getY() - first.y)) {
                // Delayed handling of selection
                if (cell != null && !e.isPopupTrigger() && isSelectEnabled()
                        && (first != null || !isMoveEnabled())) {
                    graphComponent.selectCellForEvent(cell, e);
                }


                // Delayed folding for cell that was initially under the mouse
                if (graphComponent.isFoldingEnabled()
                        && graphComponent.hitFoldingIcon(initialCell, e.getX(),
                        e.getY())) {
                    fold(initialCell);
                } else {
                    // Handles selection if no cell was initially under the mouse
                    Object tmp = graphComponent.getCellAt(e.getX(), e.getY(),
                            graphComponent.isSwimlaneSelectionEnabled());

                    if (cell == null && first == null) {
                        if (tmp == null) {
                            if (!graphComponent.isToggleEvent(e)) {
                                graph.clearSelection();
                            }
                        } else if (graph.isSwimlane(tmp)
                                && graphComponent.getCanvas()
                                .hitSwimlaneContent(graphComponent,
                                        graph.getView().getState(tmp),
                                        e.getX(), e.getY())) {
                            graphComponent.selectCellForEvent(tmp, e);
                        }
                    }

//                    if (cell != null || tmp != null) {
//                        //--加入悬浮弹出
//                        this.currentCommand = getCommand(IFlowCommandConstants.COMMAND_POPUP);
//                        if (cell != null) {
//                            currentCommand.setComponentCell((ComponentCell) cell);
//                        } else if (tmp != null) {
//                            currentCommand.setComponentCell((ComponentCell) tmp);
//                        }
//                        currentCommand.execute();
//                    }

                    if (graphComponent.isFoldingEnabled()
                            && graphComponent.hitFoldingIcon(tmp, e.getX(),
                            e.getY())) {
                        fold(tmp);
                        e.consume();
                    }
                }
            } else if (movePreview.isActive()) {
                if (graphComponent.isConstrainedEvent(e)) {
                    if (Math.abs(dx) > Math.abs(dy)) {
                        dy = 0;
                    } else {
                        dx = 0;
                    }
                }

                mxCellState markedState = marker.getMarkedState();
                Object target = (markedState != null) ? markedState.getCell()
                        : null;

                // FIXME: Cell is null if selection was carried out, need other variable
                //trace("cell", cell);

                if (target == null
                        && isRemoveCellsFromParent()
                        && shouldRemoveCellFromParent(graph.getModel()
                        .getParent(initialCell), cells, e)) {
                    target = graph.getDefaultParent();
                }

                boolean clone = isCloneEnabled()
                        && graphComponent.isCloneEvent(e);
                Object[] result = movePreview.stop(true, e, dx, dy, clone,
                        target);

                if (cells != result) {
                    graph.setSelectionCells(result);
                }

                e.consume();
            } else if (isVisible()) {
                if (constrainedEvent) {
                    if (Math.abs(dx) > Math.abs(dy)) {
                        dy = 0;
                    } else {
                        dx = 0;
                    }
                }

                mxCellState targetState = marker.getValidState();
                Object target = (targetState != null) ? targetState.getCell()
                        : null;

                if (graph.isSplitEnabled()
                        && graph.isSplitTarget(target, cells)) {
                    graph.splitEdge(target, cells, dx, dy);
                } else {
                    if (target == cells[0]) {
                        moveCells(cells, dx, dy, target, e);
                    } else {
                        //--仅能在当前父单元中移动
                        mxICell parentCell = ((mxCell) cells[0]).getParent();
                        if (parentCell == target) {
                            moveCells(cells, dx, dy, target, e);
                        } else if (parentCell instanceof CompileFlowBpmCell) {
                            moveCells(cells, dx, dy, target, e);
                        }
                    }
                }
                e.consume();
            }
        }

        //--清除重置
        reset();

    }


    private TGraphEditCommand getCommand(String type) {
        return flowCommandFactory.getCommand(type);
    }


    /**
     * 通过文档编辑域触发命令操作
     */
    private synchronized void executeCommand() {
        if (graphComponent instanceof CompileFlowGraphComponent) {
            TAdaptable adapter = ((CompileFlowGraphComponent) graphComponent).getAdapter();
            //--获取编辑域完成command操作
            GraphEditDomain editDomain = (GraphEditDomain) adapter.getAdapter(GraphEditDomain.class);
            if (editDomain != null) {
                LOG.info("\uD83D\uDE07开始执行编辑器命令操作...");
                editDomain.getCommandStack().execute(getCurrentCommand());
            }
        }
    }

    private TGraphEditCommand getCurrentCommand() {
        return currentCommand;
    }

    public void mousePressed(MouseEvent e) {
        if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed()
                && !graphComponent.isForceMarqueeEvent(e)) {
            cell = graphComponent.getCellAt(e.getX(), e.getY(), false);
            initialCell = cell;

            if (cell != null) {
                if (isSelectEnabled()
                        && !graphComponent.getGraph().isCellSelected(cell)) {
                    graphComponent.selectCellForEvent(cell, e);
                    cell = null;
                }

                // Starts move if the cell under the mouse is movable and/or any
                // cells of the selection are movable
                if (!(cell instanceof CompileFlowBpmCell)) {
                    if (isMoveEnabled() && !e.isPopupTrigger()) {
                        start(e);
                        e.consume();
                    }
                }
            } else if (e.isPopupTrigger()) {
                graphComponent.getGraph().clearSelection();
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        // LATER: Check scrollborder, use scroll-increments, do not
        // scroll when over ruler dragging from library
        if (graphComponent.isAutoScroll()) {
            graphComponent.getGraphControl().scrollRectToVisible(
                    new Rectangle(e.getPoint()));
        }

        if (!e.isConsumed()) {
            gridEnabledEvent = graphComponent.isGridEnabledEvent(e);
            constrainedEvent = graphComponent.isConstrainedEvent(e);

            if (constrainedEvent && first != null) {
                int x = e.getX();
                int y = e.getY();

                if (Math.abs(e.getX() - first.x) > Math.abs(e.getY() - first.y)) {
                    y = first.y;
                } else {
                    x = first.x;
                }

                e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(),
                        e.getModifiers(), x, y, e.getClickCount(),
                        e.isPopupTrigger(), e.getButton());
            }

            //--反馈标记
            if (isVisible() && isMarkerEnabled()) {
                //--对目标单元进行反馈标记
                marker.process(e);

            }

            if (first != null) {
                if (movePreview.isActive()) {
                    double dx = e.getX() - first.x;
                    double dy = e.getY() - first.y;

                    if (graphComponent.isGridEnabledEvent(e)) {
                        mxGraph graph = graphComponent.getGraph();

                        dx = graph.snap(dx);
                        dy = graph.snap(dy);
                    }

                    boolean clone = isCloneEnabled()
                            && graphComponent.isCloneEvent(e);
                    movePreview.update(e, dx, dy, clone);
                    e.consume();
                } else if (cellBounds != null) {
                    double dx = e.getX() - first.x;
                    double dy = e.getY() - first.y;

                    if (previewBounds != null) {
                        setPreviewBounds(new Rectangle(getPreviewLocation(e,
                                gridEnabledEvent), previewBounds.getSize()));
                    }

                    if (!isVisible() && graphComponent.isSignificant(dx, dy)) {
                        if (imagePreview && dragImage == null
                                && !graphComponent.isDragEnabled()) {
                            updateDragImage(cells);
                        }

                        setVisible(true);
                    }

                    e.consume();
                }
            }
        }
    }


    @Override
    protected mxCellMarker createMarker() {
        return new FlowmxCellMarker(getGraphComponent());
    }

}
