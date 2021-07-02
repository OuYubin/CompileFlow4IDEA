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

package cn.shanghai.oyb.compileflow.editor.xml.visitor

import cn.shanghai.oyb.compileflow.common.model.visitor.EdgeMultiKey
import cn.shanghai.oyb.compileflow.editor.edges.CompileFlowTransitionEdge
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.flow.core.editor.models.edges.TEdgeVisitor
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement

import java.util
import com.intellij.psi.xml.XmlFile
import com.mxgraph.model.mxICell

import scala.beans.BeanProperty

/**
  *
  * 流程传送线访问器
  *
  * @author ouyubin
  *
  */
class CompileFlowTransitionEdgeVisitor(parentCell: mxICell) extends TEdgeVisitor {


  @BeanProperty var transitionEdgeMap = new util.HashMap[EdgeMultiKey, CompileFlowTransitionEdge]()

  @BeanProperty var sourceCells = new util.ArrayList[BaseCell]()

  @BeanProperty var targetCells = new util.ArrayList[BaseCell]()

  @BeanProperty var xmlFile: XmlFile = _

  /**
    *
    * @param cell
    */
  def visitComponentCell(cell: BaseCell): Unit = {
    val xmlTag: XmlTagModelElement = cell.getValue.asInstanceOf[XmlTagModelElement]
    val tagName = xmlTag.getXmlTag.getName
    tagName match {
      case CompileFlowXmlTagConstants.TRANSITION_TAG_NAME =>
        val ownerCell = cell.getParent
        val ownerXmlTag = ownerCell.getValue.asInstanceOf[XmlTagModelElement]
        val sourceId = ownerXmlTag.getXmlTag.getAttribute("id").getValue
        val targetId = xmlTag.getXmlTag.getAttribute("to").getValue
        val edgeMultiKey = new EdgeMultiKey(sourceId, targetId)
        //--构建传送边
        val edge = new CompileFlowTransitionEdge(xmlTag)
        edge.setEdge(true)
        edge.getGeometry.setRelative(true)
        transitionEdgeMap.put(edgeMultiKey, edge)
        //--持有传送边的单元必定是起点
        sourceCells.add(ownerCell.asInstanceOf[BaseCell])
      case _ => targetCells.add(cell)
    }

  }


}
