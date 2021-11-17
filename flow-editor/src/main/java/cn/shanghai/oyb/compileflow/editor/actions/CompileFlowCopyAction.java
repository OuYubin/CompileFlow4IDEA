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
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapUtil;
import cn.shanghai.oyb.compileflow.common.actions.CommonAction;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.compileflow.editor.transferable.CompileFlowTransferable;
import cn.shanghai.oyb.compileflow.model.CompileFlowXmlTagModelElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;

/**
 * @author ouyubin
 */
public class CompileFlowCopyAction extends CommonAction {

    private static final Logger LOG = Logger.getInstance(CompileFlowCopyAction.class);

    public CompileFlowCopyAction(BaseCell cell, TAdaptable adapter, String text, String description, Icon icon) {
        super(cell, adapter, text, description, icon);
        this.setShortcutSet(KeymapUtil.getActiveKeymapShortcuts(IdeActions.ACTION_EDITOR_COPY));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        java.util.List<CompileFlowXmlTagModelElement> selectionElements = new ArrayList();
        selectionElements.add((CompileFlowXmlTagModelElement) getCell().getValue());
        CompileFlowTransferable compileFlowTransferable;
        try {
            compileFlowTransferable = new CompileFlowTransferable(selectionElements.toArray(new CompileFlowXmlTagModelElement[0]));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(compileFlowTransferable, null);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
        }
    }

}
