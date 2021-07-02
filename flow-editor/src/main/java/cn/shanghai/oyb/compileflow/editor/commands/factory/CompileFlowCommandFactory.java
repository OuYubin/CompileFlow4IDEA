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

package cn.shanghai.oyb.compileflow.editor.commands.factory;


import cn.shanghai.oyb.compileflow.common.component.CommonGraphComponent;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.commands.TGraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConnectionAddCommand;
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowConstraintCommand;
import cn.shanghai.oyb.compileflow.editor.commands.ICompileFlowCommandConstants;
import com.mxgraph.swing.mxGraphComponent;

/**
 * 命令工厂对象
 *
 * @author ouyubin
 */
public class CompileFlowCommandFactory {

    private mxGraphComponent graphComponent;

    private TAdaptable adapter;

    public CompileFlowCommandFactory(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;
    }

    public TGraphEditCommand getCommand(String commandType) {
        TGraphEditCommand command = null;
        if (adapter == null) {
            if (graphComponent instanceof CommonGraphComponent) {
                adapter = ((CommonGraphComponent) graphComponent).getAdapter();
            }
        }
        switch (commandType) {
            case ICompileFlowCommandConstants
                    .COMMAND_CONSTRAINT:
                command = new CompileFlowConstraintCommand(adapter, GraphEditCommand.CONSTRAINT_GRAPH_COMMAND_NAME());
                break;
            case ICompileFlowCommandConstants.COMMAND_CONNECTION:
                command=new CompileFlowConnectionAddCommand(adapter, GraphEditCommand.ADD_GRAPH_COMMAND_NAME());
                break;
            default:
                break;
        }
        return command;
    }
}
