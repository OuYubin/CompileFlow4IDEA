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

package cn.shanghai.oyb.compileflow.model.factory;

import cn.shanghai.oyb.compileflow.model.*;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;

/**
 * CompileFlow模型-编辑部件构造工厂
 *
 * @author ouyubin
 */
public class CompileFlowModelElementFactory implements TModelElementFactory {
    @Override
    public XmlTagModelElement getModel(XmlTag xmlTag) {
        XmlTagModelElement modelElement = null;
        String tagName = xmlTag.getName();
        switch (tagName) {
            //--开始向compileFlow进行适配
            case CompileFlowXmlTagConstants.SCRIPT_TAG_NAME:
                modelElement = new CompileFlowScriptElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.BPM_TAG_NAME:
                modelElement = new CompileFlowBmpElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.START_TAG_NAME:
                modelElement = new CompileFlowStartElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.END_TAG_NAME:
                modelElement = new CompileFlowEndElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.AUTO_TASK_TAG_NAME:
                modelElement = new CompileFlowAutoTaskElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.DECISION_TAG_NAME:
                modelElement = new CompileFlowDecisionElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.NOTE_TAG_NAME:
                modelElement = new CompileFlowNoteElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.CONTINUE_TAG_NAME:
                modelElement = new CompileFlowContinueElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.BREAK_TAG_NAME:
                modelElement = new CompileFlowBreakElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.SUB_BPM_TAG_NAME:
                modelElement=new CompileFlowSubBpmElement(xmlTag);
                break;
            case CompileFlowXmlTagConstants.LOOP_PROCESS_TAG_NAME:
                modelElement=new CompileFlowLoopElement(xmlTag);
                break;
            default:
                break;
        }
        return modelElement;
    }
}
