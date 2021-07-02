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

package cn.shanghai.oyb.compileflow.common.model.parser.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

/**
 * 通用xml遍历工具类
 *
 * @author ouyubin
 *
 */
public class XmlElementUtil {


    public static void readComponentFile(XmlFile xmlFile, XmlRecursiveElementVisitor visitor) {
        ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
                    XmlTag root = xmlFile.getRootTag();
                    if (root != null && root.getFirstChild() != null) {
                        //--接受访问者,生成根节点对象
                        root.accept(visitor);
                        return xmlFile.getText();
                    }
                    return "";
                }
        );
    }

}
