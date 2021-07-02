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

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowElementCell;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.compileflow.common.commands.RemoveCommand;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.model.Cell;
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import com.mxgraph.model.mxICell;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.TO_ATTR_NAME;


/**
 * 删除命令
 *
 * @author ouyubin
 */
public class CompileFlowRemoveCommand extends RemoveCommand {

    private Cell myCell;

    private XmlTagModelElement oldXmlTagModelElement;

    public CompileFlowRemoveCommand(TAdaptable adapter, XmlTagModelElement parent, XmlTag child, Cell cell) {
        super(adapter, parent, child);
        this.myCell = cell;
    }

    public CompileFlowRemoveCommand(TAdaptable adapter, XmlTagModelElement parent, XmlTagModelElement child, Cell cell) {
        super(adapter, parent, child);
        this.myCell = cell;
    }


    /**
     * 删除图元的逻辑处理为:
     * 1.断开所有元模型连线
     * 2.删除自身为起点的视图连线对象
     * 3.删除自身为终点的视图连线对象
     * 4.最后删除自身
     */
    @Override
    public void execute() {
        BaseCell rootCell = null;
        if (myCell instanceof CompileFlowElementCell) {
            rootCell = ((CompileFlowElementCell) myCell).getMyEditPart().getRootEditPart().getMyCell();
            ((XmlTagModelElement) myCell.getValue()).cleanTransition();
            //--获取根单元
            Cell parentCell = (Cell) rootCell.getParent();
            List edgeCells = parentCell.getMyEdges();
            String toId = ((XmlTagModelElement) myCell.getValue()).getXmlTag().getAttributeValue(CompileFlowXmlTagConstants.ID_ATTR_NAME);

            //--搜索起点单元
            List edges = (List) edgeCells.stream().filter(edgeCell -> edgeCell instanceof CompileFlowTransitionEdge && StringUtils.equals(((XmlTagModelElement) ((CompileFlowTransitionEdge) edgeCell).getValue()).getXmlTag().getAttributeValue(TO_ATTR_NAME), toId)).collect(Collectors.toList());
            for (Iterator iter = edges.iterator(); iter.hasNext();) {
                CompileFlowTransitionEdge edge = (CompileFlowTransitionEdge) iter.next();
                //--获取边起点单元
                mxICell ownerCell = edge.getSource();
                if (ownerCell instanceof CompileFlowElementCell) {
                    //--删除连线图形对象
                    ((XmlTagModelElement) ownerCell.getValue()).removeSourceTransition();
                    //--删除元模型对象
                    ((XmlTagModelElement) ownerCell.getValue()).removeSubTag(((XmlTagModelElement) edge.getValue()).getXmlTag());
                }
            }

            //--依次删除终点连线
            int count = rootCell.getChildCount();
            List<BaseCell> cells = new ArrayList();
            for (int i = 0; i < count; i++) {
                cells.add((BaseCell) rootCell.getChildAt(i));
            }
            XmlTag[] transitions = ((XmlTagModelElement) myCell.getValue()).getXmlTag().findSubTags(CompileFlowXmlTagConstants.TRANSITION_TAG_NAME);
            for (XmlTag xmlTag : transitions) {
                String name = xmlTag.getAttributeValue(TO_ATTR_NAME);
                List targetList = cells.stream().filter(cell -> cell instanceof CompileFlowElementCell && StringUtils.equals(((XmlTagModelElement) cell.getValue()).getXmlTag().getAttributeValue(CompileFlowXmlTagConstants.ID_ATTR_NAME), name)).collect(Collectors.toList());
                for (Object target : targetList) {
                    if (target instanceof CompileFlowElementCell) {
                        //--删除终点连线
                        ((XmlTagModelElement) ((CompileFlowElementCell) target).getValue()).removeTargetTransition();
                    }
                }
            }
        }
        super.execute();
        //--最后删除节点
        if (getChild() instanceof XmlTagModelElement) {
            getParent().removeChild((XmlTagModelElement) getChild());
        }
        setEffectCell(rootCell);
    }

    /**
     * 重做动作,不参与原始PSI元素对象的写入动作
     */
    @Override
    public void redo() {
        super.redo();
        getParent().removeChild((XmlTagModelElement) getChild());
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
        setEffectCell(getEffectCell());
    }

    /**
     * 撤销动作,不参与原始PSI元素对象的写入动作
     */
    @Override
    public void undo() {
        super.undo();
        getParent().addChild((XmlTagModelElement) getChild());
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
        setEffectCell(getEffectCell());
    }
}
