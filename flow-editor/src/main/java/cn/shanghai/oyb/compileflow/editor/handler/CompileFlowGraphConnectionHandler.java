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

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowElementCell;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConnectionAddCommand;
import cn.shanghai.oyb.compileflow.editor.commands.factory.CompileFlowCommandFactory;
import cn.shanghai.oyb.compileflow.editor.component.CompileFlowGraphComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import cn.shanghai.oyb.compileflow.common.component.CommonGraphComponent;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.window.provider.TSelectionListenerProvider;
import cn.shanghai.oyb.jgraphx.handler.ConnectionHandler;
import cn.shanghai.oyb.compileflow.editor.commands.ICompileFlowCommandConstants;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell;
import cn.shanghai.oyb.compileflow.editor.handler.preview.CompileFlowConnectPreview;
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowConnectionPaletteItem;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * @author ouyubin
 */
public class CompileFlowGraphConnectionHandler extends ConnectionHandler {

    private Logger LOG = Logger.getInstance(CompileFlowGraphConnectionHandler.class);

    private CompileFlowCommandFactory myMetaDataElementConnectCommandFactory;

    private Object mySelectionModel;

    private boolean sourceAnchorEnable = false;

    Rectangle[] anchorHandles;

    public CompileFlowGraphConnectionHandler(mxGraphComponent graphComponent) {
        super(graphComponent);
        myMetaDataElementConnectCommandFactory = createConnectCommandFactory(graphComponent);
        Color validColor = new JBColor(new Color(0, 104, 208), new Color(0, 104, 208));

        //--连接反馈
        marker = new mxCellMarker(graphComponent, validColor) {
            private static final long serialVersionUID = 103433247310526381L;

            public final Stroke META_DATA_STROKE = new BasicStroke(3F);

            /**
             * 获取当前目标单元
             *
             * @param e
             * @return
             */
            protected Object getCell(MouseEvent e) {
                Object cell = super.getCell(e);
                if (isConnecting()) {
                    if (source != null) {
                        error = validateConnection(source.getCell(), cell);

                        if (error != null && error.length() == 0) {
                            cell = null;

                            // Enables create target inside groups
                            if (createTarget) {
                                error = null;
                            }
                        }
                    }
                } else if (!isValidSource(cell)) {
                    //--需要守护,当光标移动时不做守护
                    if (cell != this.getMarkedState()) {
                        cell = null;
                    }
                }

                return cell;
            }

            public mxCellState process(MouseEvent e) {
                mxCellState state = null;

                if (isEnabled()) {
                    state = getState(e);
//                    if (state != null) {
//                        LOG.info(state.toString());
//                    } else {
//                        LOG.info("no cell");
//                    }
                    boolean valid = (state != null) ? isValidState(state) : false;
                    Color color = getMarkerColor(e, state, valid);

                    highlight(state, color, valid);
                }

                return state;
            }

            //--是否是有效单元以便于是否显示高光颜色
            protected boolean isValidState(mxCellState state) {
                if (isConnecting()) {
                    return error == null;
                } else {
                    return super.isValidState(state);
                }
            }

            public void highlight(mxCellState state, Color color, boolean valid) {
                if (valid) {
                    validState = state;
                } else {
                    validState = null;
                }
                if (state == null) {
                    return;
                } else {
                    super.highlight(state, color, valid);
                }
            }


            protected Color getMarkerColor(MouseEvent e, mxCellState state,
                                           boolean isValid) {
                return (isHighlighting() || isConnecting()) ? super
                        .getMarkerColor(e, state, isValid) : null;
            }

            protected boolean intersects(mxCellState state, MouseEvent e) {
                if (!isHighlighting() || isConnecting()) {
                    return true;
                }

                if (isHotspotEnabled()) {
                    int x = e.getX();
                    int y = e.getY();
                    if (hotspot > 0) {
                        int lx = (int) Math.round(state.getX());
                        int ty = (int) Math.round(state.getY());
                        int width = (int) Math.round(state.getWidth());
                        int height = (int) Math.round(state.getHeight());

//                        if (mxUtils
//                                .getString(state.getStyle(), mxConstants.STYLE_SHAPE, "")
//                                .equals(mxConstants.SHAPE_SWIMLANE))
//                        {
//                            int start = mxUtils.getInt(state.getStyle(),
//                                    mxConstants.STYLE_STARTSIZE,
//                                    mxConstants.DEFAULT_STARTSIZE);
//
//                            if (mxUtils.isTrue(state.getStyle(),
//                                    mxConstants.STYLE_HORIZONTAL, true))
//                            {
//                                cy = (int) Math.round(state.getY() + start / 2);
//                                height = start;
//                            }
//                            else
//                            {
//                                cx = (int) Math.round(state.getX() + start / 2);
//                                width = start;
//                            }
//                        }
//
//                        int w = (int) Math.max(min, width * hotspot);
//                        int h = (int) Math.max(min, height * hotspot);
//
//                        if (max > 0)
//                        {
//                            w = Math.min(w, max);
//                            h = Math.min(h, max);
//                        }

                        //--设置触发矩形区域
                        Rectangle rect = new Rectangle(x,
                                y, (int) state.getWidth(), (int) state.getHeight());

                        return rect.contains(x, y);
                    }
                }
                return true;
            }

            public void mark() {
                if (markedState != null) {
                    Rectangle bounds = markedState.getRectangle();
                    bounds.grow(1, 1);
//                    bounds.width += 1;
//                    bounds.height += 1;
                    setBounds(bounds);

                    if (getParent() == null) {
                        setVisible(true);

                        if (KEEP_ON_TOP) {
                            graphComponent.getGraphControl().add(this, 0);
                        } else {
                            graphComponent.getGraphControl().add(this);
                        }
                    }

                    repaint();
                    eventSource.fireEvent(new mxEventObject(mxEvent.MARK, "state",
                            markedState));
                }
            }


            public void paint(Graphics g) {
                if (markedState != null && currentColor != null) {
                    ((Graphics2D) g).setStroke(META_DATA_STROKE);
                    g.setColor(currentColor);

                    if (markedState.getAbsolutePointCount() > 0) {
                        Point last = markedState.getAbsolutePoint(0).getPoint();

                        for (int i = 1; i < markedState.getAbsolutePointCount(); i++) {
                            Point current = markedState.getAbsolutePoint(i).getPoint();
                            g.drawLine(last.x - getX(), last.y - getY(), current.x
                                    - getX(), current.y - getY());
                            last = current;
                        }
                    } else {
                        //if (UIUtil.isUnderDarcula())
                        //g.drawRect(0, 0, getWidth(), getHeight() - 2);
                        //else
                        //g.drawRect(0, 0, getWidth(), getHeight());
                    }
                }
            }
        };
        marker.setHotspotEnabled(true);
    }

    @NotNull
    public CompileFlowCommandFactory createConnectCommandFactory(mxGraphComponent graphComponent) {
        return new CompileFlowCommandFactory(graphComponent);
    }

    /**
     * 控制连接起点锚点动作
     *
     * @return
     */
    protected Rectangle[] createSourceAnchorHandles() {
        Object cell = getSource().getCell();
        if (cell instanceof CompileFlowBpmCell) {
            return null;
        }
        Rectangle[] h = null;

        //--TODO:加强编辑部件中对连接锚点的定义,最终确定绘制位置
        Rectangle bounds = getSource().getRectangle();
        int inset = 1;

        int left = bounds.x - inset;
        int top = bounds.y - inset;

        int halfWidth = bounds.x + (bounds.width / 2) - inset;
        int halfHeight = bounds.y + (bounds.height / 2) - inset;

        int right = bounds.x + bounds.width + inset;
        int bottom = bounds.y + bounds.height - inset;

        h = new Rectangle[4];

        int s = 8;
        h[0] = new Rectangle(halfWidth - s / 2, top - s / 2, s, s);
        h[1] = new Rectangle(right - s / 2, halfHeight - s / 2, s, s);
        h[2] = new Rectangle(halfWidth - s / 2, bottom - s / 2, s, s);
        h[3] = new Rectangle(left - s / 2, halfHeight - s / 2, s, s);

        return h;
    }


    @Override
    public void mouseMoved(MouseEvent event) {
        mouseDragged(event);

        if (isHighlighting() && !marker.hasValidState()) {
            source = null;
        }

        Object cell = graphComponent.getCellAt(event.getX(), event.getY(), false);
        //--如果已处于连线状态
        if(connectPreview.isActive()){
            cell=getSource();
        }

        TSelectionListenerProvider provider = ((CommonGraphComponent) graphComponent).getSelectionListenerProvider();
        if (provider != null) {
            mySelectionModel = provider.getSelection();

            if (cell != null && cell instanceof CompileFlowElementCell) {
                //if (cell.parent.parent !is BeansComponentCell) {
                LOG.info("\uD83D\uDE80 =>" + cell.toString());
                if (!isHighlighting() && source != null) {
                    //--控制触发范围
                    double x = source.getX();
                    double y = source.getY();
                    setBounds(new Rectangle((int) x, (int) y, (int) source.getWidth(), (int) source.getHeight()));
                } else {
                    setBounds(null);
                }

                if (mySelectionModel != null) {
                    //--在触发范围之内
                    if (source != null && (bounds == null || bounds.contains(event.getPoint()))) {
                        if (mySelectionModel instanceof CompileFlowConnectionPaletteItem) {
                            //--控制鼠标指针
                            Toolkit toolkit = Toolkit.getDefaultToolkit();
                            Image image = ((CompileFlowConnectionPaletteItem) mySelectionModel).getImage();
                            Point hotspot = new Point(2, 15);
                            Cursor cursor = toolkit.createCustomCursor(image, hotspot, "connectCursor");
                            graphComponent.getGraphControl().setCursor(cursor);
                            //graphComponent.getGraphControl().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                            event.consume();

                            //--叠加连线锚点控制
//                            anchorHandles = createSourceAnchorHandles();
//                            mxGraph graph = graphComponent.getGraph();
//                            mxRectangle tmp = graph.getBoundingBox(cell);
//
//                            if (tmp != null) {
//                                bounds = tmp.getRectangle();
//                                if (anchorHandles != null) {
//                                    for (int i = 0; i < anchorHandles.length; i++) {
//                                        bounds.add(anchorHandles[i]);
//                                    }
//                                }
//                            }
//                            sourceAnchorEnable = true;
//
//                            if (anchorHandles != null) {
//                                int index = getIndexAt(event.getX(), event.getY());
//
//                                if (index >= 0) {
//                                    graphComponent.getGraphControl().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
//                                    event.consume();
//                                } else {
//                                    graphComponent.getGraphControl().setCursor(new Cursor(Cursor.HAND_CURSOR));
//                                }
//                            }
                        }
                    } else {
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        if (mySelectionModel instanceof CompileFlowConnectionPaletteItem) {
                            Image image = ((CompileFlowConnectionPaletteItem) mySelectionModel).getBanImage();
                            Point hotspot = new Point(7, 7);
                            Cursor cursor = toolkit.createCustomCursor(image, hotspot, "banCursor");
                            graphComponent.getGraphControl().setCursor(cursor);
                            sourceAnchorEnable = false;
                        }
                    }
                }
//                else {
//                    graphComponent.getGraphControl().setCursor(graphComponent.getGraphControl().getCursor());
//                }
            } else {
                if (mySelectionModel != null) {
                    if (mySelectionModel instanceof CompileFlowConnectionPaletteItem) {
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        Image image = ((CompileFlowConnectionPaletteItem) mySelectionModel).getBanImage();
                        Point hotspot = new Point(2, 15);
                        Cursor cursor = toolkit.createCustomCursor(image, hotspot, "banCursor");
                        graphComponent.getGraphControl().setCursor(cursor);
                        sourceAnchorEnable = false;
                    }
                }
//                else {
//                    graphComponent.getGraphControl().setCursor(graphComponent.getGraphControl().getCursor());
//                }
            }
        }
    }

//    public int getIndexAt(int x, int y) {
//        if (anchorHandles != null) {
//            //int tol = graphComponent.getTolerance();
//            Rectangle rect = new Rectangle(x, y, 6, 6);
//
//            for (int i = anchorHandles.length - 1; i >= 0; i--) {
//                if (anchorHandles[i].intersects(rect)) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }

    public void mouseDragged(MouseEvent e) {
        if (!e.isConsumed() && graphComponent.isEnabled() && isEnabled()) {
            // Activates the handler
            if (!active && first != null) {
                double dx = Math.abs(first.getX() - e.getX());
                double dy = Math.abs(first.getY() - e.getY());
                int tol = graphComponent.getTolerance();

                if (dx > tol || dy > tol) {
                    active = true;
                }
            }

            if (e.getButton() == 0 || (isActive() && connectPreview.isActive())) {
                mxCellState state = marker.process(e);

                if (connectPreview.isActive()) {
                    connectPreview.update(e, marker.getValidState(), e.getX(),
                            e.getY());
                    //setBounds(null);
                    e.consume();
                } else {
                    source = state;
                }
            }
        }
    }

    /**
     * 鼠标释放动作
     */
    public void mouseReleased(MouseEvent e) {
        if (isActive()) {
            if (error != null) {
                if (error.length() > 0) {
                    JOptionPane.showMessageDialog(graphComponent, error);
                }
            } else if (first != null) {
                mxGraph graph = graphComponent.getGraph();
                double dx = first.getX() - e.getX();
                double dy = first.getY() - e.getY();

                if (connectPreview.isActive()
                        && (marker.hasValidState() || isCreateTarget() || graph
                        .isAllowDanglingEdges())) {
                    //--鼠标释放后

                    //--连线起点
                    if (source == null)
                        return;
                    Object sourceCell = this.source.getCell();
                    LOG.info("\uD83D\uDC46连线起点 => " + sourceCell.toString());
                    //--连线终点
                    Object targetCell = graphComponent.getCellAt(e.getX(), e.getY());
                    LOG.info("\uD83D\uDC47连线终点 => " + targetCell.toString());

                    if (mySelectionModel != null) {
                        if (mySelectionModel instanceof CompileFlowConnectionPaletteItem) {
                            String connectType = ((CompileFlowConnectionPaletteItem) mySelectionModel).getTitle();
                            if (connectType != null) {
                                CompileFlowConnectionAddCommand command = (CompileFlowConnectionAddCommand) myMetaDataElementConnectCommandFactory.getCommand(ICompileFlowCommandConstants.COMMAND_CONNECTION);
                                command.setSource((BaseCell) sourceCell);
                                command.setTarget((BaseCell) targetCell);
                                TAdaptable adapter = ((CompileFlowGraphComponent) graphComponent).getAdapter();
                                assert adapter != null;
                                GraphEditDomain editDomain = (GraphEditDomain) adapter.getAdapter(GraphEditDomain.class);
                                editDomain.getCommandStack().execute(command);
                            }
                        }
                    }
                }
            }
        }
        reset();
    }

    protected mxConnectPreview createConnectPreview() {
        return new CompileFlowConnectPreview(graphComponent);
    }


    public void reset() {
        super.reset();
    }

    public void paint(Graphics g) {
        if (bounds != null) {
            if (connectIcon != null) {
                g.drawImage(connectIcon.getImage(), bounds.x, bounds.y,
                        bounds.width, bounds.height, null);
            } else if (handleEnabled) {
                g.setColor(Color.BLACK);
                g.draw3DRect(bounds.x, bounds.y, bounds.width - 1,
                        bounds.height - 1, true);
                g.setColor(Color.GREEN);
                g.fill3DRect(bounds.x + 1, bounds.y + 1, bounds.width - 2,
                        bounds.height - 2, true);
                g.setColor(Color.BLUE);
                g.drawRect(bounds.x + bounds.width / 2 - 1, bounds.y
                        + bounds.height / 2 - 1, 1, 1);
            } else if (sourceAnchorEnable||connectPreview.isActive()) {
//                Graphics2D g2 = (Graphics2D) g;
//                BasicStroke basicStroke = new BasicStroke(1F);
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
//                for (int i = 0; i < anchorHandles.length; i++) {
//                    if (g2.hitClip(anchorHandles[i].x, anchorHandles[i].y,
//                            anchorHandles[i].width, anchorHandles[i].height)) {
//                        g2.setStroke(basicStroke);
//                        g2.setColor(new JBColor(new Color(0, 134, 133), new Color(0, 134, 133)));
//                        g2.drawOval(anchorHandles[i].x, anchorHandles[i].y,
//                                anchorHandles[i].width, anchorHandles[i].height);
//                        g2.setColor(new JBColor(new Color(255, 255, 255), new Color(255, 255, 255)));
//                        g2.fillOval(anchorHandles[i].x + 1, anchorHandles[i].y + 1,
//                                anchorHandles[i].width - 1, anchorHandles[i].height - 1);
//                    }
//                }
            }
        }
    }

    public void mousePressed(MouseEvent e)
    {
        if (!graphComponent.isForceMarqueeEvent(e)
                && !graphComponent.isPanningEvent(e)
                && !e.isPopupTrigger()
                && graphComponent.isEnabled()
                && isEnabled()
                && !e.isConsumed()
                && ((isHighlighting() && marker.hasValidState()) || (!isHighlighting()
                && bounds != null && bounds.contains(e.getPoint()))))
        {
            start(e, marker.getValidState());
            e.consume();
        }
    }

    public void start(MouseEvent e, mxCellState state)
    {
        first = e.getPoint();
        connectPreview.start(e, state, "");
    }

}