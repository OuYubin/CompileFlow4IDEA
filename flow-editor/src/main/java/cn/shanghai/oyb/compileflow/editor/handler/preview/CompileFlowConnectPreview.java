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

package cn.shanghai.oyb.compileflow.editor.handler.preview;

import com.intellij.openapi.diagnostic.Logger;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CompileFlowConnectPreview extends mxConnectPreview {

    public static final Logger LOG=Logger.getInstance(CompileFlowConnectPreview.class);

    /**
     * @param graphComponent
     */
    public CompileFlowConnectPreview(mxGraphComponent graphComponent) {
        super(graphComponent);
    }

    protected Object createCell(mxCellState startState, String style)
    {
        mxGraph graph = graphComponent.getGraph();
        CompileFlowTransitionEdge compileFlowTransitionEdge=new CompileFlowTransitionEdge(null);
        compileFlowTransitionEdge.setSource((startState != null) ? (mxICell) startState.getCell() :null);
        compileFlowTransitionEdge.setEdge(true);
        compileFlowTransitionEdge.getGeometry().setRelative(true);
        ((mxICell) startState.getCell()).insertEdge(compileFlowTransitionEdge, true);
        return compileFlowTransitionEdge;
    }

    public void start(MouseEvent e, mxCellState startState, String style)
    {
        mxGraph graph = graphComponent.getGraph();
        sourceState = startState;
        //--重新计算开始点的坐标位置
        startPoint=transformScreenPoint(e.getX(),e.getY());

//        startPoint = transformScreenPoint(startState.getCenterX(),
//                startState.getCenterY());
        Object cell = createCell(startState, style);
        graph.getView().validateCell(cell);
        previewState = graph.getView().getState(cell);

        fireEvent(new mxEventObject(mxEvent.START, "event", e, "state",
                previewState));
    }


    @Override
    public void update(MouseEvent e, mxCellState targetState, double x, double y) {
        mxGraph graph = graphComponent.getGraph();
        mxICell cell = (mxICell) previewState.getCell();

        mxRectangle dirty = graphComponent.getGraph().getPaintBounds(
                new Object[] { previewState.getCell() });

        if (cell.getTerminal(false) != null)
        {
            cell.getTerminal(false).removeEdge(cell, false);
        }

        if (targetState != null)
        {
            ((mxICell) targetState.getCell()).insertEdge(cell, false);
        }

        mxGeometry geo = graph.getCellGeometry(previewState.getCell());

        LOG.info("起始点位=>"+startPoint.toString());
        geo.setTerminalPoint(startPoint, true);
        geo.setTerminalPoint(transformScreenPoint(x, y), false);

        revalidate(previewState);
        fireEvent(new mxEventObject(mxEvent.CONTINUE, "event", e, "x", x, "y",
                y));

        // Repaints the dirty region
        // TODO: Cache the new dirty region for next repaint
        Rectangle tmp = getDirtyRect(dirty);

        if (tmp != null)
        {
            graphComponent.getGraphControl().repaint(tmp);
        }
        else
        {
            graphComponent.getGraphControl().repaint();
        }
    }

    protected mxPoint transformScreenPoint(double x, double y)
    {
        mxGraph graph = graphComponent.getGraph();
        mxPoint tr = graph.getView().getTranslate();
        double scale = graph.getView().getScale();

        return new mxPoint(graph.snap(x / scale - tr.getX()), graph.snap(y
                / scale - tr.getY()));
    }

}
