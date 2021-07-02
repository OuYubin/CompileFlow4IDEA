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

import com.alibaba.compileflow.idea.graph.mxgraph.GraphComponent;
import com.alibaba.compileflow.idea.graph.model.EdgeModel;

import com.intellij.openapi.project.Project;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUndoableEdit;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author wuxiang
 * @since 2019-02-08
 *
 */
public class TransitionPropertiesDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JTextField expressionField;
    private JTextField priorityField;
    private EdgeModel edgeObject;
    private GraphComponent graphComponent;

    public TransitionPropertiesDialog(@Nullable Project project, GraphComponent graphComponent, mxCell cell) {

        setContentPane(contentPane);
        if (cell.getValue() instanceof EdgeModel) {
            this.edgeObject = (EdgeModel) cell.getValue();
        }
        // 让对话框居中
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.graphComponent = graphComponent;

        // 初始化值
        if (this.edgeObject != null) {
            nameField.setText(this.edgeObject.getTransition().getName());
            expressionField.setText(this.edgeObject.getTransition().getExpression());
            priorityField.setText(this.edgeObject.getTransition().getPriority());
        }

        expressionField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });

        nameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {

                super.mouseExited(e);
            }
        });

        buttonOK.addActionListener((e) ->{
                onOK();
        });

        buttonCancel.addActionListener((e)-> {
                onCancel();
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction((e)-> {
                onCancel();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        if (edgeObject != null) {
            edgeObject.getTransition().setExpression(expressionField.getText().trim());
            edgeObject.getTransition().setName(nameField.getText().trim());
            edgeObject.getTransition().setPriority(priorityField.getText().trim());
            graphComponent.refresh();
            // 触发一个事件，进行存储更新
            mxUndoableEdit edit =  new mxUndoableEdit(graphComponent.getGraph().getModel());
            graphComponent.getGraph().getModel().fireEvent(new mxEventObject(mxEvent.CHANGE,
                    "edit", edit, "changes", edit.getChanges()));
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }


}
