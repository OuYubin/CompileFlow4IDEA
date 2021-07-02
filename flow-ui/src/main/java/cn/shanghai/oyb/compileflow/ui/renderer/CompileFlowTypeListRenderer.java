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

package cn.shanghai.oyb.compileflow.ui.renderer;

import icons.CompileFlowImages;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author ouyubin
 */
public class CompileFlowTypeListRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (index == 0|| StringUtils.equals((String) value,"java"))
            this.setIcon(CompileFlowImages.CLASS_IMAGE());
        else if (index == 1||StringUtils.equals((String) value,"spring-bean"))
            this.setIcon(CompileFlowImages.SPRING_BEAN_IMAGE());
        return this;
    }


}
