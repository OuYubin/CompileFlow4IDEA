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

package cn.shanghai.oyb.compileflow.common.editor.editpart;

import cn.shanghai.oyb.flow.core.editor.editpart.AbstractGraphEditPart;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.psi.xml.TXmlRecursiveElementVisitor;

/**
 * 图像编辑元件主类
 *
 * @author ouyubin
 */
public class CommonGraphEditPart extends AbstractGraphEditPart {

    public CommonGraphEditPart(Object model, TAdaptable adapter) {
        super(model, adapter);
    }

    @Override
    public TXmlRecursiveElementVisitor createRecursiveElementVisitor() {
        return null;
    }

    @Override
    public void addSourceConnection() {
        super.addSourceConnection();
    }

    public void addTargetConnection() {
        super.addTargetConnection();
    }

    @Override
    public void removeSourceConnection() {
        super.removeSourceConnection();
    }

    @Override
    public void removeTargetConnection() {
        super.removeTargetConnection();
    }
}
