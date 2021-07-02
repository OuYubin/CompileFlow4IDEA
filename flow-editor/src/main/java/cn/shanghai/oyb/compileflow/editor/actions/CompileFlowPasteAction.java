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

import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowPasteCommand;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.keymap.KeymapUtil;
import cn.shanghai.oyb.compileflow.common.actions.CommonAction;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell;
import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowLoopCell;
import cn.shanghai.oyb.compileflow.editor.transferable.CompileFlowTransferable;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author ouyubin
 */
public class CompileFlowPasteAction extends CommonAction {

    private BaseCell selectCell;

    private Point myPoint;

    public CompileFlowPasteAction(BaseCell cell, TAdaptable adapter, String text, String description, Icon icon, Point point) {
        super(cell, adapter, text, description, icon);
        this.setShortcutSet(KeymapUtil.getActiveKeymapShortcuts(IdeActions.ACTION_EDITOR_PASTE));
        this.myPoint=point;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
        GraphEditCommandStack commandStack = editDomain.getCommandStack();
        if (selectCell != null) {
            TGraphEditCommand compileFlowPasteCommand = new CompileFlowPasteCommand(getAdapter(), (XmlTagModelElement) selectCell.getValue(),myPoint);
            compileFlowPasteCommand.setEffectCell(selectCell);
            commandStack.execute(compileFlowPasteCommand);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        //--判断系统剪切板中是否有信息
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);

        Object object = null;
        try {
            DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
            boolean isContain = ArrayUtils.contains(dataFlavors, CompileFlowTransferable.myCompileFlowElementDataFlavor);
            if (!isContain) {
                event.getPresentation().setEnabled(false);
                return;
            }
            object = transferable.getTransferData(CompileFlowTransferable.myCompileFlowElementDataFlavor);
        } catch (IOException | UnsupportedFlavorException e) {
            e.printStackTrace();
        }

        if (ObjectUtils.NULL.equals(object)) {
            event.getPresentation().setEnabled(false);
        } else {

            if (selectCell == null) {
                if (StringUtils.equals(event.getPlace(), COMMON_ACTION_PLACE)) {
                    selectCell = (BaseCell) event.getData(PASTE_KEY);
                }
            }
            event.getPresentation().setEnabled((!ObjectUtils.NULL.equals(object)) && (selectCell instanceof CompileFlowBpmCell || selectCell instanceof CompileFlowLoopCell));
        }
    }

}
