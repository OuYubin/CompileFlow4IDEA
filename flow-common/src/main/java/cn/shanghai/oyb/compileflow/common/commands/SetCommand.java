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
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;

/**
 * 设置命令
 *
 * @author ouyubin
 */
public class SetCommand extends AbstractGraphEditCommand {

    private Object myOwner;

    private String myFeatureName;

    private String myValue;

    private SetCommand(TAdaptable adapter) {
        super(adapter, GraphEditCommand.SET_GRAPH_COMMAND_NAME());
    }

    public SetCommand(TAdaptable adapter, XmlTag owner, String featureName, String value) {
        this(adapter);
        this.myOwner=owner;
        this.myFeatureName = featureName;
        this.myValue = value;
    }

    public SetCommand(TAdaptable adapter, XmlTagModelElement owner, String featureName, String value) {
        this(adapter);
        this.myOwner = owner;
        this.myFeatureName =featureName;
        this.myValue =value;
    }


    @Override
    public void execute() {
        if(myOwner instanceof XmlTagModelElement) {
            ((XmlTagModelElement)myOwner).setAttribute(myFeatureName, myValue);
        }else if(myOwner instanceof XmlTag){
            ((XmlTag)myOwner).setAttribute(myFeatureName,myValue);
        }
    }
}
