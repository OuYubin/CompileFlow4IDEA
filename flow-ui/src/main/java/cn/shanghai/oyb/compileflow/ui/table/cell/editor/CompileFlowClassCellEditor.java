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

package cn.shanghai.oyb.compileflow.ui.table.cell.editor;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JavaReferenceEditorUtil;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.intellij.util.ui.UIUtil;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * @author ouyubin
 */
public class CompileFlowClassCellEditor extends AbstractTableCellEditor {

    private final Project myProject;
    private final GlobalSearchScope mySearchScope;
    private EditorTextField myEditor;

    private Object myValue;

    private TAdaptable myAdapter;

    public CompileFlowClassCellEditor(TAdaptable adapter) {
        this.myAdapter = adapter;
        this.myProject = (Project) adapter.getAdapter(Project.class);
        this.mySearchScope = GlobalSearchScope.allScope(myProject);
    }

    public Object getCellEditorValue() {
        return myValue;
    }

    public final boolean stopCellEditing() {
        valueChanged();
        boolean isStop = super.stopCellEditing();
        this.myEditor = null;
        return isStop;
    }

    private void valueChanged() {
        this.myValue=this.myEditor.getText();
    }

    public boolean isCellEditable(EventObject e) {
        return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() >= 2;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String curValue = "";
        if(value instanceof String) curValue = (String) value;
        Document document = JavaReferenceEditorUtil.createDocument(curValue == null ? "" : curValue, this.myProject, true);
        this.myEditor = new EditorTextField(document, this.myProject, StdFileTypes.JAVA) {
            protected boolean shouldHaveBorder() {
                return false;
            }

            public void addNotify() {
                super.addNotify();
                JComponent editorComponent = this.getEditor().getContentComponent();
                editorComponent.getInputMap().put(KeyStroke.getKeyStroke(10, 0), "ENTER");
                editorComponent.getActionMap().put("ENTER", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        CompileFlowClassCellEditor.this.stopCellEditing();
                    }
                });
            }
        };
        this.myEditor.setFont(UIUtil.getLabelFont(UIUtil.FontSize.NORMAL));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.myEditor);
        FixedSizeButton button = new FixedSizeButton(this.myEditor);
        panel.add(button, "East");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreeClassChooser chooser = TreeClassChooserFactory.getInstance(CompileFlowClassCellEditor.this.myProject).createInheritanceClassChooser("选择Class", CompileFlowClassCellEditor.this.mySearchScope, (PsiClass) null, true, true, Conditions.alwaysTrue());
                chooser.showDialog();
                PsiClass psiClass = chooser.getSelected();

                if (psiClass != null) {
                    CompileFlowClassCellEditor.this.myEditor.setText(psiClass.getQualifiedName());
                }

            }
        });
        panel.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (!e.isTemporary() && CompileFlowClassCellEditor.this.myEditor != null) {
                    IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> {
                        IdeFocusManager.getGlobalInstance().requestFocus(CompileFlowClassCellEditor.this.myEditor, true);
                    });
                }

            }

            public void focusLost(FocusEvent e) {
            }
        });
        this.myEditor.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                if (!e.isTemporary()) {
                    CompileFlowClassCellEditor.this.stopCellEditing();
                }

            }
        });
        return panel;
    }

}
