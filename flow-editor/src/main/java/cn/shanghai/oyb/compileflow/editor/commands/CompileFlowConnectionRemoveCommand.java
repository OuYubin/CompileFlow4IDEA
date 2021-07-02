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

package cn.shanghai.oyb.compileflow.editor.commands;

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowElementCell;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;

import java.util.List;

/**
 * 流程链接删除动作
 *
 * @author ouyubin
 */
public class CompileFlowConnectionRemoveCommand extends AbstractGraphEditCommand {

    static private final Logger LOG = Logger.getInstance(CompileFlowConnectionRemoveCommand.class);

    private CompileFlowTransitionEdge transitionEdge;

    public CompileFlowConnectionRemoveCommand(TAdaptable adapter, CompileFlowTransitionEdge transitionEdge, String commandName) {
        super(adapter,commandName);
        this.transitionEdge = transitionEdge;
    }


    @Override
    public synchronized void execute() {
        CompileFlowElementCell sourceCell = (CompileFlowElementCell) transitionEdge.getSource();
        List edges = sourceCell.getMyEdges();
        for (Object edge : edges) {
            if(edge==transitionEdge){
                ((CompileFlowTransitionEdge)edge).setTarget(null);
                ((XmlTagModelElement)sourceCell.getValue()).removeSourceTransition();
                break;
            }
        }
        XmlTag xmlTag = ((XmlTagModelElement) transitionEdge.getValue()).getXmlTag();
        xmlTag.delete();
    }



}
