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

import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;


/**
 * 通用模型删除命令对象
 */

public class DeleteCommand extends AbstractGraphEditCommand {

    private XmlTag xmlTag;


    public DeleteCommand(TAdaptable adapter) {
        super(adapter, GraphEditCommand.DELETE_GRAPH_COMMAND_NAME());
    }

    public DeleteCommand(TAdaptable adapter, XmlTag xmlTag) {
        this(adapter);
        this.xmlTag = xmlTag;
    }


    @Override
    public void execute() {
        xmlTag.delete();
    }


}
