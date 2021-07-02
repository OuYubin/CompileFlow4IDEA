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

package cn.shanghai.oyb.compileflow.common.editor;

import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandEnum;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.impl.AbstractGraphFileEditor;
import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel;
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;


/**
 * 通用图形编辑器缓冲基类,主要用于扩展
 *
 * @author ouyubin
 */
public class CommonGraphFileEditor extends AbstractGraphFileEditor {

    private Logger LOG = Logger.getInstance(CommonGraphFileEditor.class);

    public CommonGraphFileEditor(Project project, VirtualFile file) {
        super(project, file);
        MessageBusConnection connection = project.getMessageBus().connect();
        //--命令监听订阅
        connection.subscribe(CommandListener.TOPIC, new CommandListener() {
            public void commandFinished(CommandEvent event) {
                //LOG.info(String.format("🤪命令名称=> %s",event.getCommandName()));
                String commandName = event.getCommandName();
                //--撤销动作介入
                if (StringUtils.startsWith(commandName, "Undo")) {
                    String undoCommandName = StringUtils.substringAfter(commandName, "Undo").trim();
                    boolean isUndoCommand = GraphEditCommandEnum.values().iterator().filter(value -> StringUtils.equals(value.toString(), undoCommandName)).nonEmpty();
                    if (isUndoCommand) {
                        GraphEditCommandStack commandStack = getEditDomain().getCommandStack();
                        commandStack.undo();
                    }
                } else if (StringUtils.startsWith(commandName, "撤消")) {
                    String undoCommandName = StringUtils.substringAfter(commandName, "撤消").trim();
                    boolean isUndoCommand = GraphEditCommandEnum.values().iterator().filter(value -> StringUtils.equals(value.toString(), undoCommandName)).nonEmpty();
                    if (isUndoCommand) {
                        GraphEditCommandStack commandStack = getEditDomain().getCommandStack();
                        commandStack.undo();
                    }
                    //--重做动作介入
                } else if (StringUtils.startsWith(commandName, "Redo")) {
                    String redoCommandName = StringUtils.substringAfter(commandName, "Redo").trim();
                    //--判断是否是来自当前命令体系中控制的命令动作
                    boolean isRedoCommand = GraphEditCommandEnum.values().iterator().filter(value -> StringUtils.equals(value.toString(), redoCommandName)).nonEmpty();
                    if (isRedoCommand) {
                        GraphEditCommandStack commandStack = getEditDomain().getCommandStack();
                        commandStack.redo();
                    }
                } else if (StringUtils.startsWith(commandName, "重做")) {
                    String redoCommandName = StringUtils.substringAfter(commandName, "重做").trim();
                    //--判断是否是来自当前命令体系中控制的命令动作
                    boolean isRedoCommand = GraphEditCommandEnum.values().iterator().filter(value -> StringUtils.equals(value.toString(), redoCommandName)).nonEmpty();
                    if (isRedoCommand) {
                        GraphEditCommandStack commandStack = getEditDomain().getCommandStack();
                        commandStack.redo();
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Override
    public VirtualFile file() {
        return null;
    }

    @Override
    public void createState() {
    }

    /**
     * 子类尝试实现模型工厂构建
     *
     * @return
     */
    @Override
    public TModelElementFactory createModelFactory() {
        return null;
    }

    /**
     * 子类需要尝试实现设计器面板
     *
     * @param project
     * @param module
     * @param file
     * @return
     */
    @Override
    public TGraphicEditPanel createGraphicEditorPanel(Project project, Module module, VirtualFile file) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}

