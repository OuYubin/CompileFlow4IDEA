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

package cn.shanghai.oyb.compileflow.editor.editparts;

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowStartCell;
import cn.shanghai.oyb.compileflow.editor.geometry.CompileFlowGeometryConstants;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.editor.models.edges.EdgeElement;
import cn.shanghai.oyb.flow.core.geometry.BaseGeometry;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.geometry.Geometry;
import cn.shanghai.oyb.jgraphx.model.Cell;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import com.mxgraph.model.mxICell;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;

/**
 * 流程开发编辑部件
 *
 * @author ouyubin
 */
public class CompileFlowStartGraphEditPart extends CompileFlowElementEditPart {

    public CompileFlowStartGraphEditPart(Object model, TAdaptable adapter) {
        super(model, adapter);
    }

    @Override
    public BaseCell createCell(Geometry geometry) {
        CompileFlowStartCell compileFlowStartCell = new CompileFlowStartCell(getModel(), geometry);
        return compileFlowStartCell;
    }

    @NotNull
    public BaseGeometry createGeometry() {
        int x = 0;
        int y = 0;
        //-设置初始大小
        double width = CompileFlowGeometryConstants.START_GEOMETRY[0];
        double height = CompileFlowGeometryConstants.START_GEOMETRY[1];
        return new BaseGeometry(x, y, width, height);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        String propertyName = event.getPropertyName();
        if (StringUtils.equals(propertyName, XmlTagModelElement.ADD_SOURCE_CONNECTION_PROP())) {
            addSourceConnection();
        }else if(StringUtils.equals(propertyName, XmlTagModelElement.SET_META_PROP())||StringUtils.equals(propertyName, XmlTagModelElement.SET_PROP())){
            refreshVisual();
        }
        super.refreshVisual();
    }


    public void addSourceConnection() {
        TGraphEditPart rootEditPart = getRootEditPart();
        mxICell rootCell = rootEditPart.getMyCell().getParent();
        if (rootCell instanceof Cell) {
            CompileFlowTransitionEdge flowTransitionEdge = new CompileFlowTransitionEdge(null);
            flowTransitionEdge.setEdge(true);
            flowTransitionEdge.getGeometry().setRelative(true);
            ((Cell) rootCell).insertLftEdge(flowTransitionEdge);
            EdgeElement edgeElement = new EdgeElement(getMyCell(), null);
            flowTransitionEdge.setValue(edgeElement);
            this.getMyCell().insertLftEdge(flowTransitionEdge, true);
        }
    }

    @Override
    public void refreshVisual() {
        super.refreshVisual();
    }
}
