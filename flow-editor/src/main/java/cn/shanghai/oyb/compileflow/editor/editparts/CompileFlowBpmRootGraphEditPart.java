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
import cn.shanghai.oyb.compileflow.editor.xml.visitor.CompileFlowBpmXmlRecursiveElementVisitor;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.geometry.BaseGeometry;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell;
import cn.shanghai.oyb.flow.core.psi.xml.TXmlRecursiveElementVisitor;
import cn.shanghai.oyb.jgraphx.geometry.Geometry;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyChangeEvent;
import java.util.List;


/**
 * 流程处理根编辑图元
 *
 * @author ouyubin
 */
public class CompileFlowBpmRootGraphEditPart extends CommonGraphEditPart {

    public CompileFlowBpmRootGraphEditPart(Object model, TAdaptable adapter) {
        super(model, adapter);
    }

    @Override
    public BaseCell createCell(Geometry geometry) {
        return new CompileFlowBpmCell(getModel());
    }

    @Override
    public TGraphEditPart getRootEditPart() {
        return this;
    }

    public List<XmlTagModelElement> getModelChildren(){
        return ((XmlTagModelElement)getModel()).getChildren();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (StringUtils.equals(propertyName, XmlTagModelElement.ADD_SUB_TAG_PROP())) {
            refreshChildren();
            refreshVisual();
        } else if (StringUtils.equals(propertyName, XmlTagModelElement.REMOVE_SUB_TAG_PROP())) {
            refreshChildren();
            refreshVisual();
        }else if (StringUtils.equals(propertyName, XmlTagModelElement.CONNECT_MODE_ENABLE())) {
            setIsConnectMode(true);
            refreshChildren();
            refreshVisual();
        } else if (StringUtils.equals(propertyName, XmlTagModelElement.CONNECT_MODE_DISABLE())) {
            setIsConnectMode(false);
            refreshChildren();
            refreshVisual();
        }
//        else if (StringUtils.equals(propertyName, LFTXmlTagModelElement.REMOVE_CONNECTION_PROP())) {
//            refreshChildren();
//            refreshVisual();
//        }
    }


}
