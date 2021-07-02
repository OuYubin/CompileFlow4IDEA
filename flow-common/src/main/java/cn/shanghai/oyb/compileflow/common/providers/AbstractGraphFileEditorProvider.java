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

package cn.shanghai.oyb.compileflow.common.providers;

import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 *
 * 抽象图图形文件编辑器提供器
 *
 * @author ouyubin
 */
public abstract class AbstractGraphFileEditorProvider implements FileEditorProvider, DumbAware {


    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return false;
    }

    @NotNull
    @Override
    public abstract FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file);

    @Override
    public void disposeEditor(@NotNull FileEditor editor){
        
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile file) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
                return true;
            }
        };
    }

    @Override
    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public abstract String getEditorTypeId();

    @NotNull
    @Override
    public abstract FileEditorPolicy getPolicy();

}
