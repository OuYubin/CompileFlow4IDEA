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

package cn.shanghai.oyb.compileflow.editor.transferable;

import java.awt.datatransfer.DataFlavor;

/**
 * @author ouyubin
 */
class CompileFlowElementDataFlavor extends DataFlavor {

    public CompileFlowElementDataFlavor() {
    }

    public CompileFlowElementDataFlavor(String mimeType, String humanPresentableName, ClassLoader classLoader) throws ClassNotFoundException {
        super(mimeType, humanPresentableName, classLoader);
    }
}
