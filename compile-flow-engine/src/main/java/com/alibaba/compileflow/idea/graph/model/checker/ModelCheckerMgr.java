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

package com.alibaba.compileflow.idea.graph.model.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.alibaba.compileflow.idea.graph.model.BaseNodeModel;
import com.alibaba.compileflow.idea.graph.model.BpmModel;
import com.alibaba.compileflow.idea.graph.model.LoopProcessNodeModel;
import com.alibaba.compileflow.idea.graph.model.checker.impl.AutoTaskNodeModelChecker;
import com.alibaba.compileflow.idea.graph.model.checker.impl.DecisionNodeModelChecker;
import com.alibaba.compileflow.idea.graph.model.checker.impl.EndNodeModelChecker;
import com.alibaba.compileflow.idea.graph.model.checker.impl.LoopProcessNodeModelChecker;
import com.alibaba.compileflow.idea.graph.model.checker.impl.StartNodeModelChecker;
import com.alibaba.compileflow.idea.graph.model.checker.impl.SubBpmNodeModelChecker;
import com.alibaba.compileflow.idea.graph.util.StringUtil;

/**
 * @author xuan
 * @since 2020/7/29
 */
public class ModelCheckerMgr {

    private static List<ModelChecker> checkerList = new ArrayList<>();

    static {
        checkerList.add(new StartNodeModelChecker());
        checkerList.add(new EndNodeModelChecker());
        checkerList.add(new DecisionNodeModelChecker());
        checkerList.add(new AutoTaskNodeModelChecker());
        checkerList.add(new SubBpmNodeModelChecker());
        checkerList.add(new LoopProcessNodeModelChecker());
    }

    public static String check(BpmModel bpmModel) {
        if (null == bpmModel) {
            return "BpmModel is null. Maybe xml syntax error!!!";
        }

        if (StringUtil.isEmpty(bpmModel.getCode())) {
            return "Bpm.code must not be empty!!!";
        }

        if (null == bpmModel.getAllNodes()) {
            return "Not fount nodes!!!";
        }

        List<BaseNodeModel> collectList = new ArrayList<>();
        fetchAllNodeList(bpmModel.getAllNodes(), collectList);

        StringBuilder message = new StringBuilder();
        for (BaseNodeModel baseNodeModel : collectList) {
            for (ModelChecker checker : checkerList) {
                if (checker.match(baseNodeModel)) {
                    Optional<String> optional = checker.execute(baseNodeModel);
                    if (optional.isPresent()) {
                        message.append("\n");
                        message.append("\n");
                        message.append(optional.get());
                    }
                }
            }
        }

        return message.toString();
    }

    private static void fetchAllNodeList(List<BaseNodeModel> nodeList, List<BaseNodeModel> collectList) {
        if (null == nodeList) {
            return;
        }

        nodeList.forEach(node -> {
            collectList.add(node);
            if (node instanceof LoopProcessNodeModel) {
                fetchAllNodeList(((LoopProcessNodeModel)node).getAllNodes(), collectList);
            }
        });
    }

}
