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

package cn.shanghai.oyb.compileflow.resource.psi;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author ouyubin
 */
public class ComponentFileViewProvider extends SingleRootFileViewProvider {
    public ComponentFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile) {
        super(psiManager, virtualFile);
    }

    public ComponentFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile, boolean b) {
        super(psiManager, virtualFile, b);
    }

    public ComponentFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile, boolean b, @NotNull Language language) {
        super(psiManager, virtualFile, b, language);
    }

    @Override
    public boolean supportsIncrementalReparse(@NotNull Language language) {
        return false;
    }

}
