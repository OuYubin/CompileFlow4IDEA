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

import com.alibaba.compileflow.idea.graph.model.VarModel;

import java.util.List;

/**
 * @author xuan
 * @since 2020/7/24
 */
public class Checker {

    public static boolean checkVars(List<VarModel> varList) {
        if (null == varList) {
            return true;
        }
        for (VarModel var : varList) {
            if (StringUtil.isEmpty(var.getName())) {
                DialogUtil.alert("var.name can not empty.");
                return false;
            }

            if (StringUtil.isEmpty(var.getInOutType())) {
                DialogUtil.alert("var.inOutType can not empty.");
                return false;
            }

            if (StringUtil.isEmpty(var.getDataType())) {
                DialogUtil.alert("var.dataType can not empty.");
                return false;
            }
        }
        return true;
    }

}
