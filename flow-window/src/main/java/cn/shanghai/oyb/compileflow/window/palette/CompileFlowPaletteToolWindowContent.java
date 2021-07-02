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

package cn.shanghai.oyb.compileflow.window.palette;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import cn.shanghai.oyb.compileflow.common.window.CommonToolWindowContent;
import cn.shanghai.oyb.flow.core.window.listener.TSelectionListener;
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowNodePaletteItem;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import icons.CompileFlowIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 流程调色工具面板窗口内容,类似于eclipse中属性视图
 *
 * @author ouyubin
 */
public class CompileFlowPaletteToolWindowContent extends CommonToolWindowContent {

    private Object mySelectionModel;

    private CompileFlowPalette myFlowPalettePage;

    public static final Logger LOG = Logger.getInstance(CompileFlowPaletteToolWindowContent.class);

    public CompileFlowPaletteToolWindowContent(Project project, boolean updateOrientation) {
        super(project, updateOrientation);
    }

    @Override
    public JPanel createToolWindowContentPanel(Project project) {
        JBPanel rootPanel = new JBPanel();
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(0);
        borderLayout.setVgap(0);
        rootPanel.setLayout(borderLayout);

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);
        JLabel titleLabel = new JLabel("流程节点", CompileFlowIcons.PALETTE_ICON, SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, UIUtil.getFontSize(UIUtil.FontSize.NORMAL)+ JBUI.scale(2f)));

        FormLayout titleLayout = new FormLayout(
                "10px" + ",pref," + "5px" + ",pref,pref:grow," +
                        "pref," + "10px",
                "10px,pref,9px,pref");
        JPanel panel = formBuilder.layout(titleLayout).add(titleLabel).xyw(2, 2, 4,"left,center")
                .addSeparator(null).xyw(1, 4, 7)
                .build();
        rootPanel.add(panel, BorderLayout.NORTH);


        JBPanel palettePanel = new JBPanel();
        palettePanel.setBorder(new JBEmptyBorder(new JBInsets(0, 0, 0, 0)));
        palettePanel.setLayout(new GridBagLayout());
        myFlowPalettePage = new CompileFlowPalette();
        myFlowPalettePage.init();

        java.util.List<CompileFlowPaletteItemsComponent> itemsComponents = myFlowPalettePage.getItemsComponents();
        for (CompileFlowPaletteItemsComponent itemsComponent : itemsComponents) {
            itemsComponent.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    itemsComponent.setMyNeedClearSelection(SwingUtilities.isLeftMouseButton(event) && itemsComponent.myBeforeClickSelectedRow() >= 0 && itemsComponent.locationToIndex(event.getPoint()) == itemsComponent.myBeforeClickSelectedRow() && !UIUtil.isControlKeyDown(event) && !event.isShiftDown());
                }

                public void mouseReleased(MouseEvent event) {
                    if (SwingUtilities.isLeftMouseButton(event) && itemsComponent.myBeforeClickSelectedRow() >= 0 && itemsComponent.locationToIndex(event.getPoint()) == itemsComponent.myBeforeClickSelectedRow() && !UIUtil.isControlKeyDown(event) && !event.isShiftDown() && itemsComponent.myNeedClearSelection()) {
                        itemsComponent.clearSelection();
                        mySelectionModel = null;
                    } else {
                        int index = itemsComponent.locationToIndex(event.getPoint());
                        if (index != -1) {
                            CompileFlowNodePaletteItem paletteItem = itemsComponent.getMyGroup().getItems().get(index);
                            mySelectionModel = paletteItem;
                        }
                    }
                    CompileFlowPaletteToolWindowContent.this.valueChanged();

                }

            });
        }
        myFlowPalettePage.createPage();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new JBInsets(0, 0, 0, 0);
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        palettePanel.add(myFlowPalettePage, constraints);
        rootPanel.add(palettePanel, BorderLayout.CENTER);
        rootPanel.setBorder(new JBEmptyBorder(new JBInsets(0, 0, 0, 0)));
        return rootPanel;
    }

    public void valueChanged() {
        for (TSelectionListener listener : myEventListenerList.getListeners(TSelectionListener.class)) {
            listener.selectionChanged(this);
        }
    }

    public Object getSelection() {
        return mySelectionModel;
    }

    public void resetSelection() {
        java.util.List<CompileFlowPaletteItemsComponent> itemsComponents = myFlowPalettePage.getItemsComponents();
        for (CompileFlowPaletteItemsComponent itemsComponent : itemsComponents) {
            itemsComponent.clearSelection();
        }
        mySelectionModel = null;
        CompileFlowPaletteToolWindowContent.this.valueChanged();
    }

}
