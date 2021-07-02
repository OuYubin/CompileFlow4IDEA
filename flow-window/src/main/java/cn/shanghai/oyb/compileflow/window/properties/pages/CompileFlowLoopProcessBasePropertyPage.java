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
import icons.CompileFlowSvgImages;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.*;

/**
 * 循环属性页
 *
 * @author ouyubin
 */
public class CompileFlowLoopProcessBasePropertyPage extends CompileFlowElementBasePropertyPage {

    private static final Logger LOG = Logger.getInstance(CompileFlowLoopProcessBasePropertyPage.class);

    private JBTextField collectionVarNameTextField;

    private JBTextField nameTextField;

    private JTextField expressionTextField;

    public CompileFlowLoopProcessBasePropertyPage() {
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
        JLabel titleLabel = new JLabel("循环 «LoopProcess»", new ImageIcon(CompileFlowSvgImages.LOOP_IMAGE), SwingConstants.LEFT);
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
        JPanel othersPanel = createOthersPanel();
        tabbedPane.addTab("其他", CompileFlowIcons.OTHERS_ICON, othersPanel);
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
                DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref,"+ DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref,"+DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref,"+DEFAULT_PROPERTY_PAGE_ROW_SPEC()+",pref,"+DEFAULT_PROPERTY_PAGE_ROW_SPEC());

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);

        JPanel panel = formBuilder.layout(layout).
                add(((DefaultComponentFactory) componentFactory).createSeparator(createSeparatorLabel("基本"))).xyw(2, 2, 4).
                add("名称:").xy(4, 4).
                add(nameTextField = new JBTextField()).xy(5, 4).
                add("集合变量名称:").xy(4, 6).
                add(collectionVarNameTextField = new JBTextField()).xy(5, 6).
                add("变量名称:").xy(4, 8).
                add(expressionTextField = new JTextField()).xy(5, 8).
                add("索引变量名称:").xy(4, 10).
                add(expressionTextField = new JTextField()).xy(5, 10).
                add("变量类型:").xy(4, 12).
                add(expressionTextField = new JTextField()).xy(5, 12).
                add("开始节点ID:").xy(4, 14).
                add(expressionTextField = new JTextField()).xy(5, 14).
                add("结束节点ID:").xy(4, 16).
                add(expressionTextField = new JTextField()).xy(5, 16).
                build();

//        collectionVarNameTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent event) {
//                updatePriority();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent event) {
//                updatePriority();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent event) {
//                updatePriority();
//            }
//
//            private void updatePriority() {
//                Object object = myInput();
//                if (object != null) {
//                    if (object instanceof XmlTagModelElement) {
//                        XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
//                        String priority = xmlTag.getAttribute(PRIORITY_ATTR_NAME) == null ? "" : xmlTag.getAttribute(PRIORITY_ATTR_NAME).getValue();
//                        String text = collectionVarNameTextField.getText();
//                        if (StringUtils.equals(priority, text)) {
//                            return;
//                        }
//                        EditDomain editDomain = (EditDomain) getAdapter().getAdapter(EditDomain.class);
//                        CommandStack commandStack = editDomain.getCommandStack();
//                        SetCommand setCommand = new SetCommand(getAdapter(), (XmlTagModelElement) object,PRIORITY_ATTR_NAME
//                                , collectionVarNameTextField.getText());
//                        commandStack.execute(setCommand);
//                    }
//                }
//            }
//        });
//
//        nameTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent event) {
//                updateName();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent event) {
//                updateName();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent event) {
//                updateName();
//            }
//
//            private void updateName() {
//                Object object = myInput();
//                if (object != null) {
//                    if (object instanceof XmlTagModelElement) {
//                        XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
//                        String name = xmlTag.getAttribute(NAME_ATTR_NAME) == null ? "" : xmlTag.getAttribute(NAME_ATTR_NAME).getValue();
//                        String text = nameTextField.getText();
//                        if (StringUtils.equals(name, text)) {
//                            return;
//                        }
//                        EditDomain editDomain = (EditDomain) getAdapter().getAdapter(EditDomain.class);
//                        CommandStack commandStack = editDomain.getCommandStack();
//                        SetCommand setCommand = new SetCommand(getAdapter(), (XmlTagModelElement) object,
//                                NAME_ATTR_NAME
//                                , nameTextField.getText());
//                        commandStack.execute(setCommand);
//                    }
//                }
//            }
//        });
//
//        expressionTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent event) {
//                updateDisplayName();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent event) {
//                updateDisplayName();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent event) {
//                updateDisplayName();
//            }
//
//            private void updateDisplayName() {
//                Object object = myInput();
//                if (object != null) {
//                    if (object instanceof XmlTagModelElement) {
//                        XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
//                        String currentExpr = xmlTag.getAttribute(EXPRESSION_ATTR_NAME) == null ? "" : xmlTag.getAttribute(EXPRESSION_ATTR_NAME).getValue();
//                        String text = expressionTextField.getText();
//                        if (StringUtils.equals(StringEscapeUtils.unescapeXml(currentExpr), text)) {
//                            return;
//                        }
//                        EditDomain editDomain = (EditDomain) getAdapter().getAdapter(EditDomain.class);
//                        CommandStack commandStack = editDomain.getCommandStack();
//
//                        //--需要进行转义处理
//                        String expr = StringEscapeUtils.escapeXml(expressionTextField.getText());
//                        SetCommand setCommand = new SetCommand(getAdapter(), xmlTag,
//                                EXPRESSION_ATTR_NAME
//                                , expr);
//                        commandStack.execute(setCommand);
//                    }
//                }
//            }
//        });
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
        super.refresh();
        Object object = myInput();
        if (object != null) {
            if (object instanceof XmlTagModelElement) {
                object = ((XmlTagModelElement) object).getXmlTag();
                String expr = ((XmlTag) object).getAttribute(EXPRESSION_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(EXPRESSION_ATTR_NAME).getValue();
                if (!StringUtils.equals(StringEscapeUtils.unescapeXml(expr), expressionTextField.getText())) {
                    expressionTextField.setText(StringEscapeUtils.unescapeXml(expr));
                }

                String name = ((XmlTag) object).getAttribute(NAME_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(NAME_ATTR_NAME).getValue();
                if (!StringUtils.equals(name, nameTextField.getText())) {
                    nameTextField.setText(name);
                }

                String priority = ((XmlTag) object).getAttribute(PRIORITY_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(PRIORITY_ATTR_NAME).getValue();
                if (!StringUtils.equals(priority, collectionVarNameTextField.getText())) {
                    collectionVarNameTextField.setText(priority);
                }

            }
        }

    }
}
