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

import cn.shanghai.oyb.compileflow.common.editor.editpart.CommonGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.models.edges.BaseEdge;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.ModelMetaElement;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.component.GraphComponent;
import cn.shanghai.oyb.jgraphx.model.Cell;
import cn.shanghai.oyb.compileflow.editor.component.CompileFlowGraphComponent;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxRectangle;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

/**
 * 流程编辑单元基础通用父类
 *
 * @author ouyubin
 */
public class CompileFlowElementEditPart extends CommonGraphEditPart {


    public CompileFlowElementEditPart(Object model, TAdaptable adapter) {
        super(model, adapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        String propertyName = event.getPropertyName();
        if (StringUtils.equals(propertyName, XmlTagModelElement.REMOVE_TARGET_CONNECTION_PROP())) {
            removeTargetConnection();
        } else if (StringUtils.equals(propertyName, XmlTagModelElement.REMOVE_SOURCE_CONNECTION_PROP())) {
            removeSourceConnection();
        } else if (StringUtils.equals(propertyName, XmlTagModelElement.CLEAN_TRANSITION_PROP())) {
            cleanTransition();
        }
        super.refreshVisual();
    }


    /**
     * 断开相关链接
     */
    private synchronized void cleanTransition() {
        List<Object> objects = this.getMyCell().getMyEdges();
        for (Iterator iter = objects.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof CompileFlowTransitionEdge) {
                if (((CompileFlowTransitionEdge) object).getSource() == this.getMyCell()) {
                    //--断开链接
                    ((CompileFlowTransitionEdge) object).setSource(null);
                } else {
                    ((CompileFlowTransitionEdge) object).setTarget(null);
                }
            }
        }
    }

    @Override
    public synchronized void removeTargetConnection() {
        TGraphEditPart parentEditPart = getRootEditPart();
        mxICell parentCell = parentEditPart.getMyCell().getParent();
        List<Object> edges = ((Cell) parentCell).getMyEdges();
        List<Object> objects = this.getMyCell().getMyEdges();
        for (Iterator iter = objects.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof CompileFlowTransitionEdge) {
                if (((CompileFlowTransitionEdge) object).getTarget() == this.getMyCell() && ((CompileFlowTransitionEdge) object).getSource() == null) {
                    edges.remove(object);
                    this.getMyCell().removeEdge((BaseEdge) object, false);
                    iter.remove();
                }
            }
        }
    }

    public synchronized void removeSourceConnection() {
        TGraphEditPart editPart = getRootEditPart();
        mxICell parentCell = editPart.getMyCell().getParent();
        List<Object> edges = ((Cell) parentCell).getMyEdges();
        List<Object> objects = this.getMyCell().getMyEdges();
        for (Iterator iter = objects.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof CompileFlowTransitionEdge) {
                if (((CompileFlowTransitionEdge) object).getSource() == this.getMyCell() && ((CompileFlowTransitionEdge) object).getTarget() == null) {
                    edges.remove(object);
                    this.getMyCell().removeEdge((BaseEdge) object, true);
                    iter.remove();
                }
            }
        }
    }

    @Override
    public void refreshVisual() {
        Object object = getModel();
        if (object instanceof XmlTagModelElement) {
            ModelMetaElement flowElementMeta = ((XmlTagModelElement) object).getMeta();
            if (flowElementMeta != null) {
                getGeometry().setX(flowElementMeta.getX());
                getGeometry().setY(flowElementMeta.getY());
                if (flowElementMeta.getWidth() >= 0) {
                    getGeometry().setWidth(Math.max(flowElementMeta.getWidth(), this.getGeometry().getWidth()));
                }
                if (flowElementMeta.getHeight() >= 0) {
                    getGeometry().setHeight(Math.max(flowElementMeta.getHeight(), this.getGeometry().getHeight()));
                }
            } else {
                double width = this.getGeometry().getWidth();
                double height = this.getGeometry().getHeight();
                mxRectangle rectangle =
                        ((CompileFlowGraphComponent) adapter().getAdapter(GraphComponent.class)).getLayoutAreaSize();
                double x = rectangle.getCenterX() - width / 2;
                double y = rectangle.getCenterY() - height / 2;
                getGeometry().setX(x);
                getGeometry().setY(y);
            }
        }
        this.getMyCell().setConnectable(getRootEditPart().isConnectMode());
        super.refreshVisual();
    }
}
