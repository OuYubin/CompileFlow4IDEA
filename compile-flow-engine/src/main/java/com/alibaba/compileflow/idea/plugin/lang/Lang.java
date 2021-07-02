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

package com.alibaba.compileflow.idea.plugin.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.compileflow.idea.graph.util.FileUtil;
import com.alibaba.compileflow.idea.graph.util.xml.Node;
import com.alibaba.compileflow.idea.graph.util.xml.XmlUtil;

/**
 *
 * @author xuan
 * @since 2020/9/7
 */
public class Lang {
    private static final String LANG_PATH = "/com/alibaba/compileflow/idea/plugin/lang/lang.xml";

    private static final String LANG_KEY_DEFAULT = "default";
    private static final String LANG_KEY_ZH_CN = "zh_CN";

    private static final Map<String, Map<String, String>> LANG_MAP = new HashMap<>();

    private static String CURRENT_LANG = LANG_KEY_DEFAULT;

    static {
        loadLang();
    }

    private static void loadLang() {
        try {

            String langXml = FileUtil.readToString(Lang.class.getResourceAsStream(LANG_PATH));
            Node node = XmlUtil.xml2Node(langXml);

            List<Node> organizationList = node.getChildList("organization");
            if (null == organizationList) {
                return;
            }
            organizationList.forEach(organization -> {
                Map<String, String> itemMap = new HashMap<>();

                List<Node> itemList = organization.getChildList("item");
                if (null == itemList) {
                    return;
                }
                itemList.forEach(item -> itemMap.put(item.getAttr("key"), item.getAttr("value")));
                LANG_MAP.put(organization.getAttr("name"), itemMap);
            });
        } catch (Exception e) {
            //Ignore
        }
    }

    public static String getString(String key) {
        String value = null;

        Map<String, String> itemMap = LANG_MAP.get(CURRENT_LANG);
        if (null != itemMap) {
            value = itemMap.get(key);
        }
        if (null == value) {
            Map<String, String> defaultItemMap = LANG_MAP.get(LANG_KEY_DEFAULT);
            if (null != defaultItemMap) {
                value = defaultItemMap.get(key);
            }
        }
        return value;
    }

}
