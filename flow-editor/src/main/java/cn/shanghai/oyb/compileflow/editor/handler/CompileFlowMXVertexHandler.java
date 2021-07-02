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

import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConstraintCommand;
import cn.shanghai.oyb.compileflow.editor.commands.factory.CompileFlowCommandFactory;
import cn.shanghai.oyb.compileflow.editor.component.CompileFlowGraphComponent;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell;
import cn.shanghai.oyb.compileflow.editor.commands.ICompileFlowCommandConstants;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author ouyubin
 */
public class CompileFlowMXVertexHandler extends mxVertexHandler {

    private CompileFlowCommandFactory flowCommandFactory;

    /**
     * @param graphComponent
     * @param state
     */
    public CompileFlowMXVertexHandler(mxGraphComponent graphComponent, mxCellState state) {
        super(graphComponent, state);
        this.flowCommandFactory = new CompileFlowCommandFactory(graphComponent);
    }


    public void mouseDragged(MouseEvent event) {
        Object cell = getState().getCell();
        if (!(cell instanceof CompileFlowBpmCell)) {
            super.mouseDragged(event);
        }
    }

    public void mouseReleased(MouseEvent event) {
        Object cell = getState().getCell();
        if (!(cell instanceof CompileFlowBpmCell)) {
            super.mouseReleased(event);
        }

    }

    protected Rectangle[] createHandles() {
        Object cell = getState().getCell();
        if (cell instanceof CompileFlowBpmCell) {
            return null;
        }
        Rectangle[] h = null;

        if (graphComponent.getGraph().isCellResizable(getState().getCell())) {
            Rectangle bounds = getState().getRectangle();
            int inset = 1;

            int left = bounds.x - inset;
            int top = bounds.y - inset;

            int w2 = bounds.x + (bounds.width / 2) - inset;
            int h2 = bounds.y + (bounds.height / 2) - inset;

            int right = bounds.x + bounds.width + inset;
            int bottom = bounds.y + bounds.height - inset;

            h = new Rectangle[9];

            int s = 5;
            h[0] = new Rectangle(left - s, top - s, s, s);
            h[1] = new Rectangle(w2, top - s, s, s);
            h[2] = new Rectangle(right, top - s, s, s);
            h[3] = new Rectangle(right, h2, s, s);
            h[4] = new Rectangle(right, bottom, s, s);
            h[7] = new Rectangle(left - s, h2, s, s);
            h[6] = new Rectangle(left - s, bottom, s, s);
            h[5] = new Rectangle(w2, bottom, s, s);

        } else {
            h = new Rectangle[1];
        }

        int s = mxConstants.LABEL_HANDLE_SIZE;
        mxRectangle bounds = state.getLabelBounds();
//        h[h.length - 1] = new Rectangle((int) (bounds.getX()
//                + bounds.getWidth() / 2 - s), (int) (bounds.getY()
//                + bounds.getHeight() / 2 - s), 2 * s, 2 * s);

        return h;
    }

    public void refresh(mxCellState state) {
        this.state = state;
        handles = createHandles();
        mxGraph graph = graphComponent.getGraph();
        mxRectangle tmp = graph.getBoundingBox(state.getCell());

        if (tmp != null) {
            bounds = tmp.getRectangle();

            if (handles != null) {
                for (int i = 0; i < handles.length; i++) {
                    if (isHandleVisible(i)) {
                        bounds.add(handles[i]);
                    }
                }
            }
        }
    }

    public static Cursor[] CURSORS = new Cursor[]{
            new Cursor(Cursor.NW_RESIZE_CURSOR),
            new Cursor(Cursor.N_RESIZE_CURSOR),
            new Cursor(Cursor.NE_RESIZE_CURSOR),
            new Cursor(Cursor.W_RESIZE_CURSOR),
            new Cursor(Cursor.SE_RESIZE_CURSOR),
            new Cursor(Cursor.E_RESIZE_CURSOR),
            new Cursor(Cursor.SW_RESIZE_CURSOR),
            new Cursor(Cursor.S_RESIZE_CURSOR)
            , new Cursor(Cursor.MOVE_CURSOR)};

    public void mouseMoved(MouseEvent e) {
        if (!e.isConsumed() && handles != null) {
            int index = getIndexAt(e.getX(), e.getY());

            if (index >= 0 && isHandleEnabled(index)) {
                Cursor cursor = getCursor(e, index);

                if (cursor != null) {
                    graphComponent.getGraphControl().setCursor(cursor);
                    e.consume();
                } else {
                    graphComponent.getGraphControl().setCursor(
                            new Cursor(Cursor.HAND_CURSOR));
                }
            }
        }
    }

    protected Cursor getCursor(MouseEvent e, int index) {
        if (index >= 0 && index <= CURSORS.length) {
            return CURSORS[index];
        }

        return null;
    }


    public Color getSelectionColor() {
        return new JBColor(new Color(255, 200, 0), new Color(192, 192, 192));
        //return new JBColor(Color.BLACK,new Color(174,234,0));
    }

    public Stroke getSelectionStroke() {
        return new BasicStroke(1F);
    }

    protected Color getHandleFillColor(int index) {
        if (isLabel(index)) {
            return mxSwingConstants.LABEL_HANDLE_FILLCOLOR;
        }
        return new JBColor(new Color(0, 123, 241), new Color(192, 192, 192));
    }

    protected Color getHandleBorderColor(int index) {
        return new JBColor(new Color(0, 123, 241), new Color(192, 192, 192));
    }

    protected void resizeCell(MouseEvent e) {
        mxGraph graph = graphComponent.getGraph();
        double scale = graph.getView().getScale();

        Object cell = state.getCell();
        mxGeometry geometry = graph.getModel().getGeometry(cell);

        if (geometry != null) {
            double dx = (e.getX() - first.x) / scale;
            double dy = (e.getY() - first.y) / scale;

            if (isLabel(index)) {
                geometry = (mxGeometry) geometry.clone();

                if (geometry.getOffset() != null) {
                    dx += geometry.getOffset().getX();
                    dy += geometry.getOffset().getY();
                }

                if (gridEnabledEvent) {
                    dx = graph.snap(dx);
                    dy = graph.snap(dy);
                }

                geometry.setOffset(new mxPoint(dx, dy));
                graph.getModel().setGeometry(cell, geometry);
            } else {
                mxRectangle bounds = union(geometry, dx, dy, index);
                Rectangle rect = bounds.getRectangle();

                // Snaps new bounds to grid (unscaled)
                if (gridEnabledEvent) {
                    int x = (int) graph.snap(rect.x);
                    int y = (int) graph.snap(rect.y);
                    rect.width = (int) graph.snap(rect.width - x + rect.x);
                    rect.height = (int) graph.snap(rect.height - y + rect.y);
                    rect.x = x;
                    rect.y = y;
                }

                graph.resizeCell(cell, new mxRectangle(rect));

                //--执行自定义操作
                CompileFlowConstraintCommand command = (CompileFlowConstraintCommand) flowCommandFactory.getCommand(ICompileFlowCommandConstants.COMMAND_CONSTRAINT);
                TAdaptable adapter = ((CompileFlowGraphComponent) graphComponent).getAdapter();
                assert adapter != null;
                GraphEditDomain editDomain = (GraphEditDomain) adapter.getAdapter(GraphEditDomain.class);
                command.setEffectCell((BaseCell) cell);
                editDomain.getCommandStack().execute(command);
            }
        }


    }

    public void paint(Graphics g) {
        Rectangle bounds = getState().getRectangle();
        if (g.hitClip(bounds.x, bounds.y, bounds.width, bounds.height)) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke stroke = g2.getStroke();
            g2.setStroke(getSelectionStroke());
            g.setColor(getSelectionColor());
            if (UIUtil.isUnderDarcula())
                g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height);
            else
                g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 1);
            g2.setStroke(stroke);
        }
        if (handles != null && isHandlesVisible()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(getSelectionStroke());
            for (int i = 0; i < handles.length; i++) {
                if (isHandleVisible(i)
                        && g2.hitClip(handles[i].x, handles[i].y,
                        handles[i].width, handles[i].height)) {
                    g2.setColor(getSelectionColor());
                    g2.fillRect(handles[i].x, handles[i].y, handles[i].width,
                            handles[i].height);
                    if (i < 8) {
                        g2.drawRect(handles[i].x, handles[i].y,
                                handles[i].width, handles[i].height);
                    } else {
//                        g.drawOval(handles[i].x, handles[i].y,
//                                handles[i].width, handles[i].height);
                        g.drawLine(handles[i].x, handles[i].y + handles[i].height, handles[i].x + handles[i].width / 2, handles[i].y);
                        g.drawLine(handles[i].x + handles[i].width / 2, handles[i].y, handles[i].x + handles[i].width, handles[i].y + handles[i].height);
                        g.drawLine(handles[i].x, handles[i].y + handles[i].height, handles[i].x + handles[i].width, handles[i].y + handles[i].height);
                    }
                }
            }
        }
    }

    @Override
    protected boolean isHandleVisible(int index) {
        if (index % 2 == 0) {
            return super.isHandleVisible(index);
        } else {
            return false;
        }
    }

    protected mxRectangle union(mxRectangle bounds, double dx, double dy,
                                int index) {
        double left = bounds.getX();
        double right = left + bounds.getWidth();
        double top = bounds.getY();
        double bottom = top + bounds.getHeight();

        if (index > 3) {
            bottom = bottom + dy;
        } else {
            top = top + dy;
        }

        if (index == 0 || index == 6) {
            left += dx;
        } else if (index == 2 || index == 4) {
            right += dx;
        }

        double width = right - left;
        double height = bottom - top;

        if (width < 0) {
            left += width;
            width = Math.abs(width);
        }

        if (height < 0) {
            top += height;
            height = Math.abs(height);
        }

        return new mxRectangle(left, top, width, height);
    }

}
