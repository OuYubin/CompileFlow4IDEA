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

package cn.shanghai.oyb.compileflow.editor.editparts.factory;

import cn.shanghai.oyb.compileflow.editor.editparts.*;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.editpart.factory.TGraphEditPartFactory;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import com.intellij.psi.xml.XmlTag;

/**
 * 复刻Eclipse定义编辑部件工厂，通过传入TAG构建编辑元件
 *
 * @author ouyubin
 */
public class CompileFlowEditPartFactory implements TGraphEditPartFactory {

    private TAdaptable adapter;


    public CompileFlowEditPartFactory(TAdaptable adapter) {
        this.adapter = adapter;
    }

    @Override
    public TGraphEditPart getGraphEditPart(Object object) {
        TGraphEditPart compileFlowEditPart = null;
        if (object instanceof XmlTagModelElement) {
            XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
            String name = xmlTag.getName();
            switch (name) {
                case CompileFlowXmlTagConstants.SCRIPT_TAG_NAME:
                    compileFlowEditPart = new CompileFlowScriptGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.BPM_TAG_NAME:
                    compileFlowEditPart = new CompileFlowBpmRootGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.START_TAG_NAME:
                    compileFlowEditPart = new CompileFlowStartGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.AUTO_TASK_TAG_NAME:
                    compileFlowEditPart = new CompileFlowAutoTaskGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.DECISION_TAG_NAME:
                    compileFlowEditPart = new CompileFlowDecisionGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.END_TAG_NAME:
                    compileFlowEditPart = new CompileFlowEndGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.NOTE_TAG_NAME:
                    compileFlowEditPart = new CompileFlowNoteGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.CONTINUE_TAG_NAME:
                    compileFlowEditPart = new CompileFlowContinueGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.BREAK_TAG_NAME:
                    compileFlowEditPart = new CompileFlowBreakGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.SUB_BPM_TAG_NAME:
                    compileFlowEditPart = new CompileFlowSubBpmGraphEditPart(object, adapter);
                    break;
                case CompileFlowXmlTagConstants.LOOP_PROCESS_TAG_NAME:
                    compileFlowEditPart=new CompileFlowLoopGraphEditPart(object,adapter);
                    break;
                default:
                    return null;
            }
        }
        return compileFlowEditPart;
    }

}
