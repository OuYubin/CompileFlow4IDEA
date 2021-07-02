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

package cn.shanghai.oyb.compileflow.editor.xml.codec;

import cn.shanghai.oyb.compileflow.editor.xml.visitor.CompileFlowTransitionEdgeVisitor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.compileflow.common.graph.CommonGraph;
import cn.shanghai.oyb.compileflow.common.model.parser.utils.XmlElementUtil;
import cn.shanghai.oyb.compileflow.common.model.visitor.EdgeMultiKey;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.editpart.factory.TGraphEditPartFactory;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.editor.models.edges.BaseEdge;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.jgraphx.model.Cell;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程模型解码器
 *
 * @author ouyubin
 */
public class CompileFlowModelCodec extends XmlRecursiveElementVisitor {

    private final Logger LOG = Logger.getInstance(CompileFlowModelCodec.class);

    private CommonGraph myGraph;

    private mxIGraphModel myGraphModel;

    private mxCell myRootCell;

    private mxICell myCurrentCell;

    private mxICell parentCell;

    private TGraphEditPart myCurrentFlowGraphEditPart;

    private XmlTagModelElement myCurrentXmlTagModelElement;

    private TGraphEditPart myRootGraphEditPart;

    private TGraphEditPartFactory myFlowEditPartFactory;

    private TModelElementFactory myFlowModelElementFactory;

    /**
     * 传送线边访问器
     */
    private CompileFlowTransitionEdgeVisitor flowTransitionEdgeVisitor;


    public CompileFlowModelCodec(TAdaptable adapter) {
        this.myGraph = (CommonGraph) adapter.getAdapter(Graph.class);
        //--jgraphx root图形模型
        this.myGraphModel = myGraph.getModel();
        this.myRootCell = (mxCell) myGraphModel.getRoot();
        this.parentCell = (mxCell) myGraphModel.getChildAt(myRootCell, 0);
        this.myFlowEditPartFactory = myGraph.getEditPartFactory();
        this.myFlowModelElementFactory = (TModelElementFactory) adapter.getAdapter(TModelElementFactory.class);
        this.flowTransitionEdgeVisitor = new CompileFlowTransitionEdgeVisitor(parentCell);
    }

    /**
     * 解码动作,根据传入信息进行解码生成图形框架所需信息
     */
    public void decode(XmlFile xmlFile) {
        //--节点及边模型构建
        XmlElementUtil.readComponentFile(xmlFile, this);
        //--整理边
        makeUpEdges();

    }

    public TGraphEditPart getRootGraphEditPart() {
        return myRootGraphEditPart;
    }

    private void makeUpEdges() {
        //--整理传送边模型
        makeUpTransitionEdges();
    }

    private void makeUpTransitionEdges() {
        HashMap<EdgeMultiKey, CompileFlowTransitionEdge> wireEdgeMap = flowTransitionEdgeVisitor.getTransitionEdgeMap();
        for (Iterator<Map.Entry<EdgeMultiKey, CompileFlowTransitionEdge>> iter = wireEdgeMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<EdgeMultiKey, CompileFlowTransitionEdge> entry = iter.next();
            EdgeMultiKey edgeMultiKey = entry.getKey();
            CompileFlowTransitionEdge flowTransitionEdge = entry.getValue();
            String source = (String) edgeMultiKey.getKey(0);
            String target = (String) edgeMultiKey.getKey(1);
            List<BaseCell> sourceOrTargetCells = flowTransitionEdgeVisitor.getSourceCells();
            List<BaseCell> sourceCells =
                    sourceOrTargetCells.stream().filter(cell -> StringUtils.equals(
                            ((XmlTagModelElement) cell.getValue()).getXmlTag().getAttributeValue("id"), source)).collect(Collectors.toList());
            if (!sourceCells.isEmpty()) {
                for (BaseCell sourceCell : sourceCells) {
                    List<BaseCell> targetCells = flowTransitionEdgeVisitor.getTargetCells();
                    targetCells =
                            targetCells.stream().filter(cell -> StringUtils.equals(((XmlTagModelElement) cell.getValue()).getXmlTag().getAttributeValue("id"), target)).collect(Collectors.toList());
                    for (BaseCell targetCell : targetCells) {
                        ((Cell) parentCell).insertLftEdge(flowTransitionEdge);
                        sourceCell.insertLftEdge(flowTransitionEdge, true);
                        targetCell.insertLftEdge(flowTransitionEdge, false);
                    }
                }
            }
        }
    }

    /**
     * 获取顶级单元
     *
     * @return
     */
    public mxCell getRootCell() {
        return myRootCell;
    }

    /**
     * 构建一个jGraphx需要的模型数据,此方法仅仅构建编辑部件及jGraphx图形单元信息
     *
     * @param xmlTag
     */
    @Override
    public void visitXmlTag(XmlTag xmlTag) {
        TGraphEditPart componentEditPart = null;
        BaseCell cell = null;
        String tagName = xmlTag.getName();
        LOG.info("😛构建" + tagName + "节点的编辑元件");

        XmlTagModelElement xmlTagModelElement = null;
        if (StringUtils.equals(tagName, CompileFlowXmlTagConstants.TRANSITION_TAG_NAME)) {
            XmlTagModelElement xmlTagElement = new XmlTagModelElement(xmlTag);
            //--传送链接线边单元构建
            cell = new CompileFlowTransitionEdge(xmlTagElement);
        } else {
            //--通过模型工厂构建自定义模型
            xmlTagModelElement = createModelElement(xmlTag);
            if (myCurrentXmlTagModelElement == null) {
                myCurrentXmlTagModelElement = xmlTagModelElement;
            }
            componentEditPart = createGraphEditPart(xmlTagModelElement);
        }

        //--构建编辑元件的层次结构,构建时不能触发任何编辑元件刷新动作,避免编辑部件与数据模型匹配不正确
        if (componentEditPart != null) {
            if (myRootGraphEditPart == null) {
                myRootGraphEditPart = componentEditPart;
            }
            if (myCurrentFlowGraphEditPart != null) {
                //--新增子编辑部件
                myCurrentFlowGraphEditPart.addChild(componentEditPart);
                if (xmlTagModelElement != null)
                    myCurrentXmlTagModelElement.addChild(xmlTagModelElement);
            }
            myCurrentXmlTagModelElement=xmlTagModelElement;
            myCurrentFlowGraphEditPart = componentEditPart;
            cell = componentEditPart.getMyCell();
        }

        if (cell != null) {
            //--仅针对连线边处理
            if (cell instanceof BaseEdge) {
                cell.setParent(myCurrentCell);
                cell.setEdge(true);
            }
            //--首次构建根节点时触发
            if (myCurrentCell == null) {
                parentCell.insert(cell);
                cell.setParent(parentCell);
            }
            myCurrentCell = cell;
            //--构建传送边关系
            cell.accept(flowTransitionEdgeVisitor);
        }

        //--执行递归
        super.visitXmlTag(xmlTag);
        //--递归完成后随即返回父级cell
        if (cell != null) {
            myCurrentCell = cell.getParent();
        }
        //--递归完成后随即返回父级编辑部件
        if (componentEditPart != null) {
            myCurrentFlowGraphEditPart = componentEditPart.getParentEditPart();
        }
        if(xmlTagModelElement!=null)
            myCurrentXmlTagModelElement=xmlTagModelElement.getParent();
    }


    private TGraphEditPart createGraphEditPart(XmlTagModelElement xmlTagModelElement) {
        return myFlowEditPartFactory.getGraphEditPart(xmlTagModelElement);
    }

    private XmlTagModelElement createModelElement(XmlTag xmlTag) {
        return myFlowModelElementFactory.getModel(xmlTag);
    }


}
