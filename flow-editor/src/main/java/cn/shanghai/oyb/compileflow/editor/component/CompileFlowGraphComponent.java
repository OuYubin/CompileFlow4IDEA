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

package cn.shanghai.oyb.compileflow.editor.component;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import cn.shanghai.oyb.compileflow.common.component.CommonGraphComponent;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.window.provider.TSelectionListenerProvider;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.compileflow.editor.canvas.CompileFlowCanvas;
import cn.shanghai.oyb.compileflow.editor.handler.CompileFlowGraphConnectionHandler;
import cn.shanghai.oyb.compileflow.editor.handler.CompileFlowGraphHandler;
import cn.shanghai.oyb.compileflow.editor.handler.CompileFlowGraphTransferHandler;
import cn.shanghai.oyb.compileflow.editor.handler.CompileFlowMXVertexHandler;
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowConnectionPaletteItem;
import cn.shanghai.oyb.compileflow.resource.filetype.CompileFlowFileType;
import cn.shanghai.oyb.compileflow.model.CompileFlowXmlTagModelElement;
import com.mxgraph.swing.handler.*;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


/**
 * @author ouyubin
 */
public class CompileFlowGraphComponent extends CommonGraphComponent {

    private Logger LOG = Logger.getInstance(CompileFlowGraphComponent.class);

    public CompileFlowGraphComponent(Graph graph, TAdaptable adaptable) {
        super(graph, adaptable);
    }


    /**
     * 加入图形处理对象
     *
     * @return
     */
    @Override
    public mxGraphHandler createGraphHandler() {
        return new CompileFlowGraphHandler(this);
    }

    /**
     * 拖放处理支持
     *
     * @return
     */
    @Override
    protected TransferHandler createTransferHandler() {
        return new CompileFlowGraphTransferHandler();
    }

    /**
     * 连接处理支持
     *
     * @return
     */
    @Override
    public mxConnectionHandler createConnectionHandler() {
        return new CompileFlowGraphConnectionHandler(this);
    }

    /**
     * 选择动作支持
     *
     * @return
     */
    public mxSelectionCellsHandler createSelectionCellsHandler() {
        return new mxSelectionCellsHandler(this);
    }


    public mxCellHandler createHandler(mxCellState state) {
        if (graph.getModel().isVertex(state.getCell())) {
            return new CompileFlowMXVertexHandler(this, state);
        } else if (graph.getModel().isEdge(state.getCell())) {
            mxEdgeStyle.mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state,
                    null, null, null);

            if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
                    //|| style == mxEdgeStyle.SideToSide
                    || style == mxEdgeStyle.TopToBottom) {
                return new mxElbowEdgeHandler(this, state);
            }

            return new mxEdgeHandler(this, state);
        }

        return new mxCellHandler(this, state);
    }


    /**
     * 双击处理支持
     */
    @Override
    protected void installDoubleClickHandler() {
        graphControl.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (!e.isConsumed() && isEditEvent(e)) {
                    Object cell = getCellAt(e.getX(), e.getY(), false);

                    if (cell != null) {
                        Object value = ((BaseCell) cell).getValue();
                        if (value != null) {
                            String link = ((XmlTagModelElement) value).getXmlTag().getAttributeValue("link");
                            if (link != null) {
                                File file = new File(link);
                                if (file.exists()) {
                                    VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
                                    LOG.info("\uD83D\uDD30虚拟文件路径 => " + virtualFile.getCanonicalPath());
                                    if (virtualFile != null && virtualFile.getFileType() == CompileFlowFileType.INSTANCE) {
                                        Project project = ((XmlTagModelElement) value).getXmlTag().getProject();
                                        FileEditorManager.getInstance(project).openFile(virtualFile, true);
                                    }

                                }
                            }
                        }

                    }
                }
            }
        });
    }

    @Override
    public mxInteractiveCanvas createCanvas() {
        return new CompileFlowCanvas();
    }

    public void selectionChanged(TSelectionListenerProvider selectionListenerProvider) {
        this.mySelectionListenerProvider = selectionListenerProvider;
        Object selectionModel = selectionListenerProvider.getSelection();
        TGraphEditPart rootEditPart = (TGraphEditPart) this.getAdapter().getAdapter(TGraphEditPart.class);
        CompileFlowXmlTagModelElement model = (CompileFlowXmlTagModelElement) rootEditPart.getModel();
        if (selectionModel != null) {
            if (selectionModel instanceof CompileFlowConnectionPaletteItem) {
                //--开启连线模式
                model.setConnectMode(true);
                Image image = ((CompileFlowConnectionPaletteItem) selectionModel).getImage();
                Point hotSpot = new Point(image.getWidth(null) / 2, image.getHeight(null) / 2);
                String title = ((CompileFlowConnectionPaletteItem) selectionModel).getTitle();
                if (StringUtils.equals(title, "连接线")) {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Cursor cursor = toolkit.createCustomCursor(image, hotSpot, title);
                    this.getGraphControl().setCursor(cursor);
                }
            }
        } else {
            //--关闭连线模式
            model.setConnectMode(false);
            this.getGraphControl().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    protected void installKeyHandler() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isEscapeEnabled()) {
                    TSelectionListenerProvider provider = CompileFlowGraphComponent.this.getSelectionListenerProvider();
                    if (provider != null) {
                        provider.resetSelection();
                    }
                    escape(e);
                }
            }
        });
    }
}
