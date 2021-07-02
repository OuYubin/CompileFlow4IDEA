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

import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowPasteCommand;
import com.intellij.ide.PasteProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.component.GraphComponent;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author ouyubin
 */
public class CompileFlowPasteProvider implements PasteProvider, MouseMotionListener {

    private Logger LOG = Logger.getInstance(CompileFlowPasteProvider.class);

    private TAdaptable myAdapter;

    private boolean canDelete = true;

    private Point myPoint;

    public CompileFlowPasteProvider(TAdaptable adapter) {
        this.myAdapter = adapter;
        GraphComponent graphComponent = (GraphComponent) adapter.getAdapter(GraphComponent.class);
        graphComponent.getGraphControl().addMouseMotionListener(this);
    }

    /**
     * @param dataContext
     */
    @Override
    public void performPaste(@NotNull DataContext dataContext) {
        LOG.info(String.format("当前上下文信息=> %s", dataContext.toString()));
        Graph compileFlowGraph = (Graph) myAdapter.getAdapter(Graph.class);
        Object[] selectionCells = compileFlowGraph.getSelectionCells();
        GraphEditDomain editDomain = (GraphEditDomain) myAdapter.getAdapter(GraphEditDomain.class);
        GraphEditCommandStack commandStack = editDomain.getCommandStack();
        if (selectionCells.length > 0) {
            BaseCell selectCell = (BaseCell) selectionCells[0];
            TGraphEditCommand compileFlowPasteCommand = new CompileFlowPasteCommand(myAdapter, (XmlTagModelElement) selectCell.getValue(), myPoint);
            commandStack.execute(compileFlowPasteCommand);
        }
    }

    @Override
    public boolean isPastePossible(@NotNull DataContext dataContext) {
        return true;
    }

    @Override
    public boolean isPasteEnabled(@NotNull DataContext dataContext) {
        return true;
    }

    @Override
    public void mouseDragged(MouseEvent event) {

    }

    public void mouseMoved(MouseEvent event) {
        this.myPoint = event.getPoint();
    }

}
