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

package cn.shanghai.oyb.compileflow.common.editor.surface;

import cn.shanghai.oyb.compileflow.common.graph.CommonGraph;
import cn.shanghai.oyb.compileflow.common.render.CommonRenderResult;
import cn.shanghai.oyb.flow.core.editor.TGraphFileEditor;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.editpart.factory.TGraphEditPartFactory;
import cn.shanghai.oyb.flow.core.editor.surface.impl.AbstractGraphEditPanel;
import cn.shanghai.oyb.flow.core.editor.surface.listeners.EditPanelPsiTreeChangeListener;
import cn.shanghai.oyb.flow.core.editor.surface.listeners.TEditPanelSelectionChangeEventListener;
import cn.shanghai.oyb.jgraphx.component.GraphComponent;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.jgraphx.model.GraphModel;
import cn.shanghai.oyb.jgraphx.view.ElbowPlusConnector;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.roots.ToolbarPanel;
import com.intellij.util.Alarm;
import com.intellij.util.ThrowableConsumer;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStyleRegistry;
import com.mxgraph.view.mxStylesheet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Map;

/**
 * 主编辑器设计面板通用实现实现
 *
 * @author ouyubin
 */
public abstract class CommonGraphEditPanel extends AbstractGraphEditPanel {

    Logger LOG = Logger.getInstance(CommonGraphEditPanel.class);

    protected XmlFile myXmlFile;

    protected EditPanelPsiTreeChangeListener compileFlowPsiTreeChangeListener;

    protected TGraphEditPart rootGraphEditPart;

    //--激活状态
    public boolean isActive = false;

    protected Alarm sessionAlarm = new Alarm();

    protected MergingUpdateQueue sessionQueue;

    //--图形面板容器
    protected GraphComponent myComponentGraphComponent;

    private Document document;

    public CommonGraphEditPanel(@NotNull TGraphFileEditor editor,
                                @NotNull Project project,
                                @NotNull Module module,
                                @NotNull final VirtualFile file) {
        super(editor, project, module, file);
        //--1.构建图形视图
        createGraphicalViewer();

        myXmlFile =
                (XmlFile) ApplicationManager.getApplication().runReadAction((Computable<PsiFile>) () ->
                        PsiManager.getInstance(getProject()).findFile(file));

        //--2.进行解组动作,监听文件PSI结构对象变化(需要着重处理)
        compileFlowPsiTreeChangeListener = new EditPanelPsiTreeChangeListener(this, myXmlFile, 100, () -> {
            //--当前已激活就不再进行解析动作,重新校验文件
            if (!isActive) {
                parseFile();
                isActive = true;
            } else {
                LOG.info("执行校验操作....");
                revalidateFile();
            }
        });
        compileFlowPsiTreeChangeListener.initialize();
        compileFlowPsiTreeChangeListener.activate();

        //--向alarm中增加请求
        //externalPSIChangeListener.addRequest();

//        document= FileDocumentManager.getInstance().getDocument(file);
//        document.addDocumentListener(new DocumentListener() {
//            @Override
//            public void documentChanged(@NotNull DocumentEvent event) {
//                Object object = event.getSource();
//                LOG.info(event.getSource().toString());
//            }
//        });
//        MessageBusConnection connection = project.getMessageBus().connect();
//        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
//            @Override
//            public void after(@NotNull java.util.List<? extends VFileEvent> events) {
//                LOG.info("Virtual File has been changed");
//                //getMyComponentGraph().refresh();
//            }
//        });
    }

    /**
     * 重新校验文件
     */
    public void revalidateFile() {

    }

    /**
     * 初始化图形面板
     */
    private void createGraphicalViewer() {
        FormLayout layout = new FormLayout(
                "10px,pref,fill:pref:grow,pref",
                "5px,pref,5px,pref");
        JLabel titleLabel = createTitleLabel();
        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);
        DefaultActionGroup group = createEditActionGroup();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        contentPane.setBorder(new JBEmptyBorder(0, 0, 0, 0));
        ToolbarPanel toolbarPanel = new ToolbarPanel(contentPane, group);
        toolbarPanel.setBorder(new JBEmptyBorder(0, 0, 0, 0));
        JPanel panel = formBuilder.layout(layout).
                add(titleLabel).xyw(2, 2, 3, "left,center").
                add(toolbarPanel).xy(3, 2, "right,center").
                addSeparator(null).xyw(1, 4, 4)
                .build();
        add(panel, BorderLayout.NORTH);
        //--构建一个缺省数据图形模型对象
        GraphModel GraphModel = new GraphModel(null);
        //--注册新拐线连接器
        mxStyleRegistry.putValue("elbowPlusEdgeStyle", new ElbowPlusConnector());
        mxStylesheet stylesheet = new mxStylesheet();
        Map<String, Object> style = stylesheet.getDefaultEdgeStyle();
        style.put(mxConstants.STYLE_EDGE, "elbowPlusEdgeStyle");
        style.put(mxConstants.STYLE_ROUNDED, false);
        style.put(mxConstants.STYLE_HORIZONTAL, false);
        //--构建控制对象
        CommonGraph myCommonGraph = createGraph(GraphModel, stylesheet);
        myCommonGraph.setEditPartFactory(createEditPartFactory());
        //--构建图形面板
        this.myComponentGraphComponent = createGraphComponent(myCommonGraph);
        setMyComponentGraph(myCommonGraph);
        add(myComponentGraphComponent, BorderLayout.CENTER);

        myComponentGraphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                super.mouseReleased(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
//              if (event.getButton() == MouseEvent.BUTTON3) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    showGraphPopupMenu(event);
                }
                CommonGraphEditPanel.this.fireSelectionChanged(event);
            }
        });

    }

    protected abstract DefaultActionGroup createEditActionGroup();

    public void showGraphPopupMenu(MouseEvent event) {
    }

    @NotNull
    public abstract CommonGraph createGraph(GraphModel GraphModel, mxStylesheet stylesheet);

    public abstract JLabel createTitleLabel();

    public abstract GraphComponent createGraphComponent(CommonGraph CommonGraph);

    public abstract TGraphEditPartFactory createEditPartFactory();

    private void fireSelectionChanged(EventObject event) {
        Arrays.stream(this.getFlowEditPanelEventListenerList().getListenerList()).forEach(listener -> {
            if (listener instanceof TEditPanelSelectionChangeEventListener) {
                ((TEditPanelSelectionChangeEventListener) listener).fireSelectionChange(event);
            }
        });
    }

    //    @Override
    //    public String getEditorText() {
    //        return ApplicationManager.getApplication().runReadAction(new Computable<String>() {
    //            @Override
    //            public String compute() {
    //                return xmlFile.getText();
    //            }
    //        });
    //    }
    //
    //    @Override
    //    public void activate() {
    //        super.activate();
    //        updateRenderer(true);
    //    }
    //
    //    private void updateRenderer(final boolean updateProperties) {
    //        if (getMyRootComponent() == null) {
    //            reparseFile();
    //            return;
    //        }
    //    }

    /**
     * 解析当前文件
     */
    private void parseFile() {
        try {
            parseFile(() -> restoreState());
        } catch (RuntimeException e) {
            compileFlowPsiTreeChangeListener.cancel();
            showError("Parsing error", e.getCause() == null ? e : e.getCause());
            e.printStackTrace();
        }
    }

    private void parseFile(final Runnable runnable) {
        createRenderer(new Throwable(), new ThrowableConsumer<CommonRenderResult, Throwable>() {
            @Override
            public void consume(CommonRenderResult result) throws Throwable {
                mxCell rootCell = getModel();
                getMyComponentGraph().getModel().setRoot(rootCell);
                runnable.run();
            }
        });
    }

    /**
     * 需要子类进行覆盖注入特定模型对象
     *
     * @return
     */
    public abstract mxCell getModel();

    //--创建渲染器
    private void createRenderer(final Throwable throwable,
                                final ThrowableConsumer<CommonRenderResult, Throwable> consumer) {
        ApplicationManager.getApplication().invokeLater(() -> {
                    try {
                        consumer.consume(null);
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public Object getAdapter(Class adapter) {
        if (adapter == Graph.class) {
            return getMyComponentGraph();
        } else if (adapter == TGraphEditPartFactory.class) {
            return ((CommonGraph) getMyComponentGraph()).getEditPartFactory();
        } else if (adapter == GraphModel.class) {
            return getMyComponentGraph().getModel();
        } else if (adapter == GraphComponent.class) {
            return myComponentGraphComponent;
        } else if (adapter == XmlFile.class) {
            return myXmlFile;
        } else if (adapter == TGraphEditPart.class) {
            return getRootGraphEditPart();
        }
        return getEditor().getAdapter(adapter);
    }

    public TGraphEditPart getRootGraphEditPart() {
        return rootGraphEditPart;
    }

    @Override
    public void dispose() {
        compileFlowPsiTreeChangeListener.dispose();
    }
}