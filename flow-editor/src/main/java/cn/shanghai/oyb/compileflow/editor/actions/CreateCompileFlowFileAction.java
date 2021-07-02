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

package cn.shanghai.oyb.compileflow.editor.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import icons.CompileFlowIcons;
import org.jetbrains.annotations.NotNull;

/**
 * 创建流程文件
 *
 * @author ouyubin
 */
public class CreateCompileFlowFileAction extends CreateFileFromTemplateAction implements DumbAware {


    public CreateCompileFlowFileAction() {
        super("CompileFlow 文件", "Create new Compile Flow file", CompileFlowIcons.FLOW_FILE_ICON);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
        builder
                .setTitle("创建新CompileFlow文件")
                .addKind("Flow", CompileFlowIcons.FLOW_FILE_ICON, "flow");
    }

    @Override
    protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
        return "CompileFlow 文件";
    }

    /**
     * overwrite this method create new Flow file
     *
     * @param name
     * @param templateName
     * @param psiDirectory
     * @return
     */
    @Override
    protected PsiFile createFile(String name, String templateName, PsiDirectory psiDirectory) {
        return super.createFile(name, templateName, psiDirectory);
    }

    //    @NotNull
//    @Override
//    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory) {
//        MyInputValidator inputValidator = new MyInputValidator(project, psiDirectory);
//        DialogWrapper dialog = new ComponentCreateNewFileDialog(project, inputValidator);
//        dialog.show();
//        return inputValidator.getCreatedElements();
//    }

//    @Override
//    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
//
//    }
//
//    @Override
//    protected String getActionName(PsiDirectory psiDirectory, @NotNull String s, String s1) {
//        return null;
//    }
}
