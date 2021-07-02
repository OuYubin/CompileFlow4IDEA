/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.compileflow.idea.graph.util;

import com.alibaba.compileflow.idea.plugin.LanguageConstants;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author xuan
 * @since 2020/7/17
 */
public class DialogUtil {

    public static void prompt(String message, String initialSelectionValue,
        Callback<String, String> okCallback) {

        String inputValue = Messages.showInputDialog(message, LanguageConstants.INPUT_DIALOG_TITLE,
            ImageIconUtil.LOGO_ICON,
            initialSelectionValue, null);

        if (null != inputValue) {
            okCallback.call(inputValue);
        }
    }

    public static void alert(String str) {
        Messages.showInfoMessage(str, LanguageConstants.INPUT_DIALOG_TITLE);
    }

    public static String selectClassName(Project project) {
        TreeClassChooserFactory chooserFactory = TreeClassChooserFactory.getInstance(project);
        GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
        TreeClassChooser chooser =
            chooserFactory.createNoInnerClassesScopeChooser(
                "Select Class", searchScope, (psiClass) -> true, null);
        chooser.showDialog();
        PsiClass psiClass = chooser.getSelected();
        if (null != psiClass) {
            return psiClass.getQualifiedName();
        }

        //cancel
        return null;
    }

}
