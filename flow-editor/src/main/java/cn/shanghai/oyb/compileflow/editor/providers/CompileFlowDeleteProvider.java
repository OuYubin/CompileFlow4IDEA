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

package cn.shanghai.oyb.compileflow.editor.providers;

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowElementCell;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConnectionRemoveCommand;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowRemoveCommand;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import com.intellij.ide.DeleteProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author ouyubin
 */
public class CompileFlowDeleteProvider implements DeleteProvider {

    private Logger LOG = Logger.getInstance(CompileFlowDeleteProvider.class);

    private TAdaptable adapter;

    public CompileFlowDeleteProvider(TAdaptable adapter) {
        this.adapter = adapter;
    }

    /**
     * @param dataContext
     */
    @Override
    public void deleteElement(@NotNull DataContext dataContext) {
        Graph compileFlowGraph = (Graph) adapter.getAdapter(Graph.class);
        GraphEditDomain editDomain = (GraphEditDomain) adapter.getAdapter(GraphEditDomain.class);
        GraphEditCommandStack commandStack = editDomain.getCommandStack();
        Object[] selectionCells = compileFlowGraph.getSelectionCells();
        Arrays.stream(selectionCells).forEach(cell -> {
            if (cell instanceof CompileFlowElementCell) {
                XmlTagModelElement flowXmlTagElement =
                        (XmlTagModelElement) ((CompileFlowElementCell) cell).getValue();
                XmlTagModelElement parent = (XmlTagModelElement) ((CompileFlowElementCell) cell).getMyEditPart().getParentEditPart().getModel();
                TGraphEditCommand lftCommand = new CompileFlowRemoveCommand(adapter,
                        parent, flowXmlTagElement.getXmlTag(), (CompileFlowElementCell) cell);
                commandStack.execute(lftCommand);
            } else if (cell instanceof CompileFlowTransitionEdge) {
//                FlowTransitionEdge flowXmlTagElement =
//                        (FlowTransitionEdge) ((FlowTransitionEdge) cell).getValue();
                //FlowXmlTagElement parent = (FlowXmlTagElement) ((FlowElementCell) cell).getMyEditPart().getParent().getModel();
                TGraphEditCommand command = new CompileFlowConnectionRemoveCommand(adapter, (CompileFlowTransitionEdge) cell, GraphEditCommand.REMOVE_GRAPH_COMMAND_NAME());
                commandStack.execute(command);
            }
        });
        compileFlowGraph.clearSelection();
    }

    @Override
    public boolean canDeleteElement(@NotNull DataContext dataContext) {
        return true;
    }
}
