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

import com.intellij.util.ui.ComboBoxCellEditor;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author ouyubin
 */
public class CompileFlowOptionsCellEditor extends ComboBoxCellEditor {

    private Object myValue;

    private TAdaptable myAdapter;

    private String[] myOptions;

    public CompileFlowOptionsCellEditor(String[] options) {
        this.myOptions = options;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    @Override
    public Object getCellEditorValue() {
        return myValue;
    }

    @Override
    protected List<String> getComboBoxItems() {
        return Arrays.asList(myOptions);
    }

    @Override
    public boolean stopCellEditing() {
        valueChanged();
        boolean isStop = super.stopCellEditing();
        return isStop;
    }

    private void valueChanged() {
        this.myValue=((JComboBox)this.getComponent()).getSelectedItem();
    }

}
