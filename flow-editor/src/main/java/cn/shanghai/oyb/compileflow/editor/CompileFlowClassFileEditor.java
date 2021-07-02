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

package cn.shanghai.oyb.compileflow.editor;

import com.alibaba.compileflow.idea.graph.codec.ModelConvertFactory;
import com.alibaba.compileflow.idea.graph.model.BpmModel;
import com.alibaba.compileflow.idea.graph.model.checker.ModelCheckerMgr;
import com.google.gson.Gson;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.ThrowableConsumer;
import com.intellij.util.messages.MessageBusConnection;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeListener;

/**
 * @author ouyubin
 */
public class CompileFlowClassFileEditor implements TAdaptable, FileEditor {

    Logger LOG = Logger.getInstance(CompileFlowClassFileEditor.class);

    private Project myProject;

    private PsiFile myPsiFile;

    private JComponent myComponent;

    private Editor myEditor;

    public CompileFlowClassFileEditor(Project project, VirtualFile file) {
        this.myProject = project;
        this.myPsiFile = PsiManager.getInstance(project).findFile(file);
        Document document = new DocumentImpl("");
        myEditor = EditorFactory.getInstance().createEditor(document, myProject, JavaFileType.INSTANCE, true);
        LexerEditorHighlighter highlighter = new LexerEditorHighlighter(new JavaFileHighlighter(), EditorColorsManager.getInstance().getGlobalScheme());
        ((EditorEx) myEditor).setHighlighter(highlighter);
        myComponent = myEditor.getComponent();

        //--方案A:通过消息总线进行通知
        MessageBusConnection connection = myProject.getMessageBus().connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                LOG.info("✏️️重新生成Java代码并进行展示...");
                reloadContext();
            }
        });

        //--方案B:通过流程文件内容发生变化进行通知
//        FileDocumentManager.getInstance().getDocument(file).addDocumentListener(new DocumentListener() {
//            @Override
//            public void documentChanged(@NotNull DocumentEvent event) {
//                //Object object = event.getSource();
//                LOG.info("✏️重新生成Java代码并进行展示...");
//                reloadContext();
//            }
//        });
    }

    @Override
    public @NotNull JComponent getComponent() {
        return myComponent;
    }

    private void reloadContext() {
        parserFlow(() -> {
            //--重新绘制
            getComponent().revalidate();
            getComponent().repaint();
        });
    }

    private void parserFlow(Runnable runnable) {
        createCodeRender(new ThrowableConsumer<String, Throwable>() {
            @Override
            public void consume(String code) throws Throwable {
                ((JTextComponent) myEditor.getContentComponent()).setText(code);
                runnable.run();
            }
        });
    }


    private void createCodeRender(ThrowableConsumer throwableConsumer) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                String code = loadJavaCode(myProject, (XmlFile) myPsiFile);
                try {
                    loadJavaCode(myProject, (XmlFile) myPsiFile);
                    throwableConsumer.consume(code);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    /**
     * @see com.alibaba.compileflow.idea.plugin.provider.fileeditor.JavaSourceFileEditor#loadJavaCode
     */
    private String loadJavaCode(Project project, XmlFile file) {
        String javaCode = null;
        BpmModel bpmModel = null;
        String bpmCode = "default test";
        try {
            // Empty file
            if (file.getText().length() == 0) {
                javaCode = "// Please draw bpm flow first, thanks!!!";
            } else {
                bpmModel = ModelConvertFactory.getModelXmlConvertExt(file.getVirtualFile().getExtension()).toModel(
                        file.getText());
                javaCode = ModelConvertFactory.getModelCodeConvertExt(file.getVirtualFile().getExtension()).getJavaCode(
                        bpmModel);
                bpmCode = bpmModel.getCode();
            }
        } catch (Throwable e) {
            LOG.error(e);
            javaCode = "//Bpm file is illegal. Case:";
            javaCode += "\n";
            javaCode += "//" + e.getMessage();

            javaCode += "\n";
            Gson gson = new Gson();
            javaCode += "//" + gson.toJson(e);

            javaCode += "\n";
            javaCode += "\n";

            javaCode += "//Ops... I found some problems. Please confirm:";
            javaCode += "\n";
            javaCode += ModelCheckerMgr.check(bpmModel);
        }

        if (null == javaCode) {
            javaCode = "//Just not support!!!";
        }
        return javaCode;
    }


    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return "Java类";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Override
    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }

    @Override
    public Object getAdapter(Class<?> adapter) {
        return null;
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Override
    public void deselectNotify() {

    }

    @Override
    public void selectNotify() {

    }
}
