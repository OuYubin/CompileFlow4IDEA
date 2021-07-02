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

package cn.shanghai.oyb.flow.core.dialogs;

import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.util.AbstractTreeClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * @author ouyubin
 */

public class TreeClassChooserDialog extends AbstractTreeClassChooserDialog {
    public TreeClassChooserDialog(String title, @NotNull Project project, GlobalSearchScope scope, @NotNull Class elementClass, @Nullable Filter classFilter, @Nullable PsiNamedElement initialClass) {
        super(title, project, scope, elementClass, classFilter, initialClass);
    }


    @Override
    protected PsiNamedElement getSelectedFromTreeUserObject(DefaultMutableTreeNode node) {
        Object userObject = node.getUserObject();
        if (!(userObject instanceof ClassTreeNode)) return null;
        ClassTreeNode descriptor = (ClassTreeNode) userObject;
        return descriptor.getPsiClass();
    }

    @NotNull
    @Override
    protected List getClassesByName(String name, boolean checkBoxState, String pattern, GlobalSearchScope searchScope) {
        final PsiShortNamesCache cache = PsiShortNamesCache.getInstance(getProject());
        PsiClass[] classes =
                cache.getClassesByName(name, checkBoxState ? searchScope : GlobalSearchScope.projectScope(getProject()).intersectWith(searchScope));
        return ContainerUtil.newArrayList(classes);
    }

}
