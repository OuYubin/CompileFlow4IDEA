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

package cn.shanghai.oyb.compileflow.resource.filetype;

import com.intellij.ide.highlighter.DomSupportEnabled;
import com.intellij.ide.highlighter.XmlLikeFileType;
import com.intellij.lang.xml.XMLLanguage;
import icons.CompileFlowIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 *
 *
 * @author ouyubin
 */
public class CompileFlowFileType extends XmlLikeFileType implements DomSupportEnabled {

    public static CompileFlowFileType INSTANCE=new CompileFlowFileType();

    @NonNls
    public static final String DEFAULT_EXTENSION = "bpm";
    @NonNls
    public static final String DOT_DEFAULT_EXTENSION = "." + DEFAULT_EXTENSION;

    private CompileFlowFileType() {
       super(XMLLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "CompileFlow";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "CompileFlow";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return CompileFlowIcons.FLOW_FILE_ICON;
    }
}
