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

package com.alibaba.compileflow.idea.graph.nodeview.dialog;

import javax.swing.*;

import com.alibaba.compileflow.idea.graph.model.ContinueNodeModel;
import com.alibaba.compileflow.idea.graph.mxgraph.Graph;
import com.alibaba.compileflow.idea.graph.nodeview.component.ContinuePanel;
import com.alibaba.compileflow.idea.graph.util.StringUtil;

import com.intellij.openapi.project.Project;
import com.mxgraph.model.mxCell;
import org.jetbrains.annotations.Nullable;

/**
 * @author xuan
 * @since 2020/11/9
 */
public class ContinueDialog extends BaseDialog {

    public ContinueDialog(@Nullable Project project, mxCell cell, Graph graph) {
        super(project, cell, graph);
    }

    @Override
    protected String getDialogTitle() {
        return "Continue setting";
    }

    @Override
    protected JPanel getParamPanel(Project project, Graph graph, mxCell cell) {
        return new ContinuePanel();
    }

    @Override
    protected void initParamPanelView() {

    }

    @Override
    protected void initParamPanelData() {
        ContinuePanel continuePanel = (ContinuePanel)paramPanel;
        ContinueNodeModel continueNodeModel = ContinueNodeModel.getFromCellValue(cell.getValue());
        continuePanel.getExpressionField().setText(StringUtil.trimToEmpty(continueNodeModel.getExpression()));
    }

    @Override
    protected void doParamSave() {
        ContinuePanel continuePanel = (ContinuePanel)paramPanel;

        ContinueNodeModel continueNodeModel = ContinueNodeModel.getFromCellValue(cell.getValue());

        continueNodeModel.setExpression(StringUtil.trimToEmpty(continuePanel.getExpressionField().getText()));
    }
}
