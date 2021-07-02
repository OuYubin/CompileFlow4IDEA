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

package cn.shanghai.oyb.compileflow.editor.commands;

import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommand;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.compileflow.editor.transferable.CompileFlowTransferable;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import cn.shanghai.oyb.compileflow.model.factory.CompileFlowModelElementFactory;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.G_ATTR_NAME;
import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.ID_ATTR_NAME;

/**
 * 粘贴命令
 *
 * @author ouyubin
 */
public class CompileFlowPasteCommand extends AbstractGraphEditCommand {

    private Logger LOG = Logger.getInstance(CompileFlowPasteCommand.class);

    private XmlTagModelElement myElement;

    private Point myPoint;

    private XmlTagModelElement oldXmlTagModelElement;

    public CompileFlowPasteCommand(TAdaptable adapter, XmlTagModelElement element, Point point) {
        super(adapter, GraphEditCommand.PASTE_GRAPH_COMMAND_NAME());
        this.myElement = element;
        this.myPoint = point;
    }

    @Override
    public void execute() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);

        Object object = null;
        try {
            object = transferable.getTransferData(CompileFlowTransferable.myCompileFlowElementDataFlavor);
        } catch (IOException | UnsupportedFlavorException e) {
            e.printStackTrace();
        }
        if (object instanceof Object[]) {
            for (Object element : (Object[]) object) {
                XmlTag child = ((XmlTagModelElement) element).getXmlTag();
                XmlTag copyChild;
                if (child.isValid()) {
                    copyChild = (XmlTag) child.copy();
                    copyChild.setAttribute(ID_ATTR_NAME, String.valueOf(System.currentTimeMillis()));
                    String[] g = child.getAttributeValue(G_ATTR_NAME).split(",");
                    copyChild.setAttribute(G_ATTR_NAME, getCurrentGeometryInfo(g));
                } else {
                    //--需要手动复制当前节点信息
                    XmlTag parentXmlTag = myElement.getXmlTag();
                    XmlTag childXmlTag = parentXmlTag.createChildTag(child.getLocalName(), null, child.getValue().getText(), false);
                    //--复制属性
                    for (XmlAttribute attribute : child.getAttributes()) {
                        String attrName = attribute.getName();
                        String attrValue = attribute.getValue();
                        if (attrName.equals(G_ATTR_NAME)) {
                            attrValue = getCurrentGeometryInfo(attrValue.split(","));
                        }
                        childXmlTag.setAttribute(attribute.getName(), attrValue);
                    }
                    copyChild = childXmlTag;
                }
                PasteXmlRecursiveElementVisitor visitor = new PasteXmlRecursiveElementVisitor();
                //--通过访问器对XmlTag对象进行裁剪
                copyChild.accept(visitor);
                LOG.info(String.format("复制对象=> %s", copyChild.toString()));
                XmlTag pasteXmlTag = myElement.addSubTag(copyChild, false);
                CompileFlowModelElementFactory modelElementFactory = new CompileFlowModelElementFactory();
                oldXmlTagModelElement = modelElementFactory.getModel(pasteXmlTag);
                myElement.addChild(oldXmlTagModelElement);
            }
        }
    }

    public String getCurrentGeometryInfo(String[] g) {
        StringBuilder stringBuilder = new StringBuilder();
        double width = 100.0D;
        double height = 40.0D;
        if (g.length == 4) {
            width = Double.valueOf(g[2]);
            height = Double.valueOf(g[3]);
        }
        stringBuilder.append(myPoint.getX());
        stringBuilder.append(",");
        stringBuilder.append(myPoint.getY());
        stringBuilder.append(",");
        stringBuilder.append(width);
        stringBuilder.append(",");
        stringBuilder.append(height);
        return stringBuilder.toString();
    }

    /**
     * 撤销动作
     */
    public void undo() {
        super.undo();
        myElement.removeChild(oldXmlTagModelElement);
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
        setEffectCell(getEffectCell());
    }

    /**
     * 重做动作
     */
    @Override
    public void redo() {
        super.redo();
        myElement.addChild(oldXmlTagModelElement);
    }


    /**
     * 通过递归访问器完成自定义元素模型构建及对应xmlTag对象加工
     */
    private class PasteXmlRecursiveElementVisitor extends XmlRecursiveElementVisitor {

        @Override
        public void visitXmlTag(XmlTag xmlTag) {
            String tagName = xmlTag.getName();
            LOG.info(String.format("当前标记名称=> %s", tagName));
            //--裁剪边节点
            if (StringUtils.equals(tagName, CompileFlowXmlTagConstants.TRANSITION_TAG_NAME)) {
                XmlTag parentXmlTag = xmlTag.getParentTag();
                xmlTag.delete();
                //--裁剪完成后需要重新回到父节点重新遍历
                xmlTag = parentXmlTag;
            }
            super.visitXmlTag(xmlTag);
        }
    }
}
