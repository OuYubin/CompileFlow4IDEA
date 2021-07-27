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

import cn.shanghai.oyb.compileflow.common.actions.CommonAction;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConnectionRemoveCommand;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowRemoveCommand;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.jgraphx.model.Cell;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.RefreshQueue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ouyubin
 */

public class CompileFlowDeleteAction extends CommonAction {

    public CompileFlowDeleteAction(BaseCell cell, TAdaptable adapter, String text, String description, Icon icon) {
        super(cell, adapter, text, description, icon);
        this.setShortcutSet(KeymapUtil.getActiveKeymapShortcuts(IdeActions.ACTION_DELETE));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        Cell cell = getCell();
        XmlTagModelElement child = (XmlTagModelElement) (getCell()).getValue();
        XmlTagModelElement parent = null;
        TGraphEditCommand command = null;
        if (cell.isVertex()) {
            parent =
                    (XmlTagModelElement) (getCell()).getMyEditPart().getParentEditPart().getModel();
            command = new CompileFlowRemoveCommand(getAdapter(), parent, child, getCell());

        } else {
            if (cell instanceof CompileFlowTransitionEdge) {
                parent = (XmlTagModelElement) cell.getSource().getValue();
            }
            command = new CompileFlowConnectionRemoveCommand(getAdapter(), (CompileFlowTransitionEdge) cell, GraphEditCommand.REMOVE_GRAPH_COMMAND_NAME());
        }
        if (command != null) {
            GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
            if (editDomain != null) {
                editDomain.getCommandStack().execute(command);
            }
        }
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
    }
}
