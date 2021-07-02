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

package cn.shanghai.oyb.compileflow.resource.description;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import icons.CompileFlowIcons;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author ouyubin
 */
public class CompileFlowDomFileDescription extends DomFileDescription<CompileFlowElement> {

    //private static final String LSB_NAMESPACE_PREFIX = "lsb";


    public CompileFlowDomFileDescription() {
        super(CompileFlowElement.class, "bpm");
    }

    @Nullable
    @Override
    public Icon getFileIcon(@Iconable.IconFlags int flags) {
        return CompileFlowIcons.FLOW_FILE_ICON;
    }


    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        if (!super.isMyFile(file, module)) {
            return false;
        }
        final XmlTag rootTag = file.getRootTag();
        if (rootTag == null) {
            return false;
        }
        String rootName = rootTag.getName();
        if (StringUtils.equals(rootName, "bpm")) {
            return true;
        }
        return false;
    }
}
