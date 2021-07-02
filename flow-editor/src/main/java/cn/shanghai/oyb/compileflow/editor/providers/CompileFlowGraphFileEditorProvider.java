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

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.impl.DomManagerImpl;
import cn.shanghai.oyb.compileflow.common.providers.AbstractGraphFileEditorProvider;
import cn.shanghai.oyb.compileflow.editor.CompileFlowGraphFileEditor;
import cn.shanghai.oyb.compileflow.resource.description.CompileFlowDomFileDescription;
import cn.shanghai.oyb.compileflow.resource.filetype.CompileFlowFileType;
import org.jetbrains.annotations.NotNull;


/**
 * 流程文件编辑器提供器
 *
 * @author ouyubin
 */
public class CompileFlowGraphFileEditorProvider extends AbstractGraphFileEditorProvider {

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        if (file.getFileType() == CompileFlowFileType.INSTANCE||file.getFileType()== XmlFileType.INSTANCE) {
            DomFileDescription domFileDescription = DomManagerImpl.getDomManager(project).getDomFileDescription(PsiManager.getInstance(project).findFile(file));
            if (domFileDescription instanceof CompileFlowDomFileDescription) {
                return true;
            }
        }
        return super.accept(project, file);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new CompileFlowGraphFileEditor(project, file);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "bmp-editor";
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
