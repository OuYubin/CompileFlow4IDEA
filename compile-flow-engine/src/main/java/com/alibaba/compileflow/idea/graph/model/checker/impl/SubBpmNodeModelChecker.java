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

package com.alibaba.compileflow.idea.graph.model.checker.impl;

import java.util.Optional;

import com.alibaba.compileflow.idea.graph.model.BaseNodeModel;
import com.alibaba.compileflow.idea.graph.model.SubBpmNodeModel;
import com.alibaba.compileflow.idea.graph.model.checker.ModelChecker;
import com.alibaba.compileflow.idea.graph.util.CollectionUtil;
import com.alibaba.compileflow.idea.graph.util.StringUtil;

/**
 * @author xuan
 * @since 2020/7/29
 */
public class SubBpmNodeModelChecker implements ModelChecker {

    @Override
    public Optional<String> execute(BaseNodeModel model) {
        SubBpmNodeModel subBpm = (SubBpmNodeModel)model;

        StringBuilder message = new StringBuilder();
        if (StringUtil.isEmpty(subBpm.getId())) {
            message.append("Id is empty;");
        }
        if (StringUtil.isEmpty(subBpm.getType())) {
            message.append("Type is empty;");
        }
        if (StringUtil.isEmpty(subBpm.getSubBpmCode())) {
            message.append("SubBpmCode is empty;");
        }
        if (CollectionUtil.isEmpty(subBpm.getOutTransitions())) {
            message.append("No next node;");
        }

        if (message.length() > 0) {
            return Optional.of("subBpm[id=" + subBpm.getId() + "]has problem:" + message.toString());
        }
        return Optional.empty();
    }

    @Override
    public boolean match(BaseNodeModel model) {
        return model instanceof SubBpmNodeModel;
    }

}
