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

package cn.shanghai.oyb.compileflow.window.properties.pages;

import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.swing.CommonTabbedPane;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import icons.CompileFlowIcons;
import icons.CompileFlowImages;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.*;

/**
 * 链接传送属性页
 *
 * @author ouyubin
 */
public class CompileFlowTransitionBasePropertyPage extends CompileFlowElementBasePropertyPage {

    private static final Logger LOG = Logger.getInstance(CompileFlowTransitionBasePropertyPage.class);

    private JBTextField priorityTextField;

    private JBTextField nameTextField;

    private JTextField expressionTextField;

    private DocumentListener priorityDocumentListener;

    private DocumentListener nameDocumentListener;

    private DocumentListener expressionDocumentListener;

    public CompileFlowTransitionBasePropertyPage() {
        super();
    }

    /**
     * @param parent
     */
    @Override
    public void createControl(JComponent parent) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(0);
        this.setLayout(borderLayout);

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);
        JLabel titleLabel = new JLabel("路由 «Transition»", CompileFlowImages.TRANSITION_IMAGE(), SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, UIUtil.getFontSize(UIUtil.FontSize.NORMAL) + JBUI.scale(2f)));

        FormLayout titleLayout = new FormLayout(
                DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref,pref:grow," +
                        "pref," + DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN(),
                "10px" + ",pref," + "9px" + ",pref");
        JPanel panel = formBuilder.layout(titleLayout).add(titleLabel).xyw(2, 2, 4)
                .addSeparator(null).xyw(1, 4, 7)
                .build();
        this.add(panel, BorderLayout.NORTH);

        CommonTabbedPane tabbedPane = createTabPanel();
        tabbedPane.setBackground(UIUtil.getPanelBackground());
        JPanel propertyPanel = createAttributesPanel();
        propertyPanel.setBackground(UIUtil.getPanelBackground());
        propertyPanel.setBorder(new JBEmptyBorder(0));
        tabbedPane.addTab("常规", CompileFlowIcons.COMMON_ICON, propertyPanel);
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private CommonTabbedPane createTabPanel() {
        return new CommonTabbedPane();
    }


    private JBPanel createAttributesPanel() {
        JBPanel jbPanel = new JBPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        jbPanel.setLayout(gridBagLayout);
        FormLayout layout = new FormLayout(
                DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref,pref:grow," + DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN(),
                DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref");

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);

        JPanel panel = formBuilder.layout(layout).
                add(((DefaultComponentFactory) componentFactory).createSeparator(createSeparatorLabel("基本"))).xyw(2, 2, 4).
                add("名称:").xy(4, 4).
                add(nameTextField = new JBTextField()).xy(5, 4).
                add("权重:").xy(4, 6).
                add(priorityTextField = new JBTextField()).xy(5, 6).
                add("表达式:").xy(4, 8).
                add(expressionTextField = new JTextField()).xy(5, 8).
                build();


        priorityDocumentListener = new DocumentListener(PRIORITY_ATTR_NAME);
        priorityTextField.getDocument().addDocumentListener(priorityDocumentListener);

        nameDocumentListener = new DocumentListener(NAME_ATTR_NAME);
        nameTextField.getDocument().addDocumentListener(nameDocumentListener);

        expressionDocumentListener = new DocumentListener(EXPRESSION_ATTR_NAME);
        expressionTextField.getDocument().addDocumentListener(expressionDocumentListener);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = JBUI.insets(0);
        jbPanel.add(panel, gbc);
        return jbPanel;
    }


    @Override
    public void refresh() {
        Object object = myInput();
        if (object != null) {
            if (object instanceof XmlTagModelElement) {
                object = ((XmlTagModelElement) object).getXmlTag();

                nameTextField.getDocument().removeDocumentListener(nameDocumentListener);
                String name = ((XmlTag) object).getAttribute(NAME_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(NAME_ATTR_NAME).getValue();
                if (!StringUtils.equals(name, nameTextField.getText())) {
                    nameTextField.setText(name);
                }
                nameTextField.getDocument().addDocumentListener(nameDocumentListener);

                String expr = ((XmlTag) object).getAttribute(EXPRESSION_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(EXPRESSION_ATTR_NAME).getValue();
                expressionTextField.getDocument().removeDocumentListener(expressionDocumentListener);
                if (!StringUtils.equals(StringEscapeUtils.unescapeXml(expr), expressionTextField.getText())) {
                    expressionTextField.setText(StringEscapeUtils.unescapeXml(expr));
                }
                expressionTextField.getDocument().addDocumentListener(expressionDocumentListener);

                priorityTextField.getDocument().removeDocumentListener(priorityDocumentListener);
                String priority = ((XmlTag) object).getAttribute(PRIORITY_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(PRIORITY_ATTR_NAME).getValue();
                if (!StringUtils.equals(priority, priorityTextField.getText())) {
                    priorityTextField.setText(priority);
                }
                priorityTextField.getDocument().addDocumentListener(priorityDocumentListener);
            }
        }

    }
}
