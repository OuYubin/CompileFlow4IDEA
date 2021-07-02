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

package cn.shanghai.oyb.compileflow.common.commands;

import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import com.intellij.psi.xml.XmlTag;


/**
 * 通用模型新增命令对象
 *
 * @author ouyubin
 */

public class AddCommand extends AbstractGraphEditCommand {

    private XmlTag parent;

    private XmlTag child;


    public AddCommand(TAdaptable adapter) {
        super(adapter, GraphEditCommand.ADD_GRAPH_COMMAND_NAME());
    }

    public AddCommand(TAdaptable adapter, XmlTag parent, XmlTag child) {
        this(adapter);
        this.parent=parent;
        this.child=child;
    }

    @Override
    public void execute() {
        parent.addSubTag(child, false);
    }


}
