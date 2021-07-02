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

package cn.shanghai.oyb.flow.core.swing;


import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBInsets;

import java.awt.*;

/**
 * @author ouyubin
 */
public class CommonTabbedPane extends JBTabbedPane {

    public Insets getInsetsForTabComponent() {
        return JBInsets.create(new JBInsets(0, 8,0,8));
    }

}

