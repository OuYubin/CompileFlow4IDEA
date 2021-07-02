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

import cn.shanghai.oyb.compileflow.common.commands.SetCommand;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.window.pages.BasePropertyPage;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.awt.*;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.ID_ATTR_NAME;

/**
 * @author ouyubin
 */
public class CompileFlowElementBasePropertyPage extends BasePropertyPage {

    private JBTextField idTextField;

    @Override
    public void createControl(JComponent parent) {

    }

    public JBPanel createOthersPanel() {
        JBPanel jbPanel = new JBPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        jbPanel.setLayout(gridBagLayout);
        FormLayout layout = new FormLayout(
                DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref,pref:grow," + DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN(),
                DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC());

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);

        JPanel panel = formBuilder.layout(layout).
                add(((DefaultComponentFactory) componentFactory).createSeparator(createSeparatorLabel("基本"))).xyw(2, 2, 4).
                add("唯一标示:").xy(4, 4).
                add(idTextField = new JBTextField()).xy(5, 4).
                build();

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
                if(((XmlTag) object).isValid()) {
                    String name = ((XmlTag) object).getAttribute(ID_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(ID_ATTR_NAME).getValue();
                    if (!StringUtils.equals(name, idTextField.getText())) {
                        idTextField.setText(name);
                    }
                }
            }
        }
    }

    protected class DocumentListener implements javax.swing.event.DocumentListener {

        private String tagName;

        public DocumentListener(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            try {
                updateAttribute(documentEvent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            try {
                updateAttribute(documentEvent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            try {
                updateAttribute(documentEvent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        private void updateAttribute(DocumentEvent documentEvent) throws Throwable {
            try {
                String text = documentEvent.getDocument().getText(0, documentEvent.getDocument().getLength());
                Object object = myInput();
                XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
                String value = xmlTag.getAttributeValue(tagName) == null ? "" : xmlTag.getAttributeValue(tagName);
                if (StringUtils.equals(value, text)) {
                    return;
                }
                GraphEditDomain componentEditDomain =
                        (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                GraphEditCommandStack componentCommandStack = componentEditDomain.getCommandStack();
                SetCommand umlElementSetCommand = new SetCommand(getAdapter(),
                        (XmlTagModelElement) object,
                        tagName
                        , StringUtils.isBlank(text) ? null : text);
                componentCommandStack.execute(umlElementSetCommand);
            } catch (BadLocationException e) {
                throw new Throwable("更新属性错误...", e);
            }
        }
    }

}
