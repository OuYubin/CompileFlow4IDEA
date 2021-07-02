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

package cn.shanghai.oyb.compileflow.window.palette.models;

import cn.shanghai.oyb.compileflow.palette.model.CompileFlowPaletteGroup;
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowNodePaletteItem;

import javax.swing.*;

/**
 * @author ouyubin
 */
public class CompileFlowPaletteListModel extends AbstractListModel implements ListModel {

    private CompileFlowPaletteGroup flowPaletteGroup;

    public CompileFlowPaletteListModel(CompileFlowPaletteGroup flowPaletteGroup) {
        this.flowPaletteGroup = flowPaletteGroup;
    }

    public int getSize() {
        return flowPaletteGroup.getItems().size();
    }

    public CompileFlowNodePaletteItem getElementAt(int index) {
        return flowPaletteGroup.getItems().get(index);
    }
}
