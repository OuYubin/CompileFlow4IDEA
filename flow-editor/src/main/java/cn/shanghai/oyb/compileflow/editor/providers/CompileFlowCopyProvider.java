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
import cn.shanghai.oyb.compileflow.editor.transferable.CompileFlowTransferable;
import com.intellij.ide.CopyProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.compileflow.model.CompileFlowXmlTagModelElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author ouyubin
 */
public class CompileFlowCopyProvider implements CopyProvider {

    private Logger LOG = Logger.getInstance(CompileFlowCopyProvider.class);

    private TAdaptable myAdapter;

    private boolean canDelete = true;

    public CompileFlowCopyProvider(TAdaptable adapter) {
        this.myAdapter = adapter;
    }

    /**
     * @param dataContext
     */
    @Override
    public void performCopy(@NotNull DataContext dataContext) {
        Graph compileFlowGraph = (Graph) myAdapter.getAdapter(Graph.class);
        Object[] selectionCells = compileFlowGraph.getSelectionCells();
        java.util.List<CompileFlowXmlTagModelElement> selectionElements = new ArrayList();
        Arrays.stream(selectionCells).forEach(cell -> {
            if (cell instanceof CompileFlowElementCell) {
                selectionElements.add((CompileFlowXmlTagModelElement) ((CompileFlowElementCell) cell).getValue());
            }
        });
        try {
            CompileFlowTransferable compileFlowTransferable = new CompileFlowTransferable(selectionElements.toArray(new CompileFlowXmlTagModelElement[0]));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(compileFlowTransferable, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCopyEnabled(@NotNull DataContext dataContext) {
        return true;
    }

    @Override
    public boolean isCopyVisible(@NotNull DataContext dataContext) {
        return true;
    }

}
