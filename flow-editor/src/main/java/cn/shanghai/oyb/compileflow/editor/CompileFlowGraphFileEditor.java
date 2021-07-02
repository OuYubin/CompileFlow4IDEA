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

package cn.shanghai.oyb.compileflow.editor;

import cn.shanghai.oyb.compileflow.editor.surface.CompileFlowGraphEditPanel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import cn.shanghai.oyb.compileflow.common.editor.CommonGraphFileEditor;
import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel;
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory;
import cn.shanghai.oyb.compileflow.model.factory.CompileFlowModelElementFactory;

/**
 * 组件编辑器对象
 *
 * @author ouyubin
 */
public class CompileFlowGraphFileEditor extends CommonGraphFileEditor {

    public CompileFlowGraphFileEditor(Project project, VirtualFile file) {
        super(project, file);
    }

    @Override
    public TGraphicEditPanel createGraphicEditorPanel(Project project, Module module, VirtualFile file) {
        return new CompileFlowGraphEditPanel(this, project, module, file);
    }


    @Override
    public TModelElementFactory createModelFactory() {
        return new CompileFlowModelElementFactory();
    }

    public String getName() {
        return "流程设计";
    }


}
