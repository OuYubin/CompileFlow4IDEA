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

package cn.shanghai.oyb.compileflow.editor.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import cn.shanghai.oyb.compileflow.common.actions.CommonAction;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author ouyubin
 */
public class CompileFlowSelectAllAction extends CommonAction {

    public CompileFlowSelectAllAction(BaseCell cell, TAdaptable adapter, String text, String description, Icon icon) {
        super(cell, adapter, text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        XmlTagModelElement metaDataXmlTagElement =
//                (XmlTagModelElement) (getCell()).getValue();
//        XmlTag xmlTag = metaDataXmlTagElement.getXmlTag();
//        XmlTagModelElement parent =
//                (XmlTagModelElement) (getCell()).getMyEditPart().getParentEditPart().getModel();
//        CompileFlowElementRemoveCommand removeCommand = new CompileFlowElementRemoveCommand(getAdapter(), parent, xmlTag, (CompileFlowElementCell) getCell());
//        EditDomain editDomain = (EditDomain) getAdapter().getAdapter(EditDomain.class);
//        if (editDomain != null) {
//            editDomain.getCommandStack().execute(removeCommand);
//        }
//        LFTMXGraph ltfGraph = (LFTMXGraph) getAdapter().getAdapter(LFTMXGraph.class);
//        ltfGraph.clearSelection();
    }

}
