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

package cn.shanghai.oyb.compileflow.window.properties;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBInsets;
import cn.shanghai.oyb.compileflow.common.window.CommonToolWindowContent;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.window.TToolWindow;
import com.mxgraph.swing.mxGraphComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * 流程属性工具窗口,类似于eclipse中属性视图
 *
 * @author ouyubin
 */
public class CompileFlowPropertiesToolWindowContent extends CommonToolWindowContent {

    public static final Logger LOG = Logger.getInstance(CompileFlowPropertiesToolWindowContent.class);


    private TToolWindow flowPropertiesPage;

    public CompileFlowPropertiesToolWindowContent(Project project, boolean updateOrientation) {
        super(project, updateOrientation);
    }


    @Override
    public JPanel createToolWindowContentPanel(Project project) {
        JBPanel propertiesPanel = new JBPanel();
        propertiesPanel.setLayout(new GridBagLayout());
        TToolWindow componentPropertiesPage = new CompileFlowProperties();
        setPropertiesPage(componentPropertiesPage);
        initPage(componentPropertiesPage);
        componentPropertiesPage.createPage();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new JBInsets(0, 0, 0, 0);
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        propertiesPanel.add((JComponent) componentPropertiesPage, constraints);
        return propertiesPanel;
    }

    private void initPage(TToolWindow componentPropertiesPage) {
        componentPropertiesPage.init();
    }

    /**
     * 选择触发动作
     *
     * @param event
     */
    @Override
    public void fireSelectionChange(Object event) {
        if (event instanceof ComponentEvent) {
            mxGraphComponent.mxGraphControl control = (mxGraphComponent.mxGraphControl) ((ComponentEvent) event).getSource();
            Object object = control.getGraphContainer().getGraph().getSelectionCell();
            if (object != null && this.getPropertiesPage() != null) {
                LOG.info("🚀触发对象: " + object.toString());
                if (object instanceof BaseCell) {
                    this.getPropertiesPage().refresh((BaseCell) object);
                }
            }
        }
    }

    public TToolWindow getPropertiesPage() {
        return flowPropertiesPage;
    }

    public void setPropertiesPage(TToolWindow flowPropertiesPage) {
        this.flowPropertiesPage = flowPropertiesPage;
    }

}
