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

package cn.shanghai.oyb.compileflow.editor.handler

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowLoopCell
import cn.shanghai.oyb.compileflow.editor.commands.CompileFlowAddCommand
import cn.shanghai.oyb.compileflow.editor.component.CompileFlowGraphComponent
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowNodePaletteItem
import cn.shanghai.oyb.compileflow.palette.model.dataFlavor.CompileFlowPaletteDataFlavor
import cn.shanghai.oyb.flow.core.editor.commands.{GraphEditCommand, TGraphEditCommand}
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.jgraphx.handler.GraphTransferHandler
import com.intellij.openapi.diagnostic.Logger

import java.awt.datatransfer.{DataFlavor, Transferable}
import javax.swing.JComponent

/**
  *
  * 编辑器拖拽控制对象
  *
  * @author ouyubin
  *
  */

class CompileFlowGraphTransferHandler extends GraphTransferHandler() {

  val LOG: Logger = Logger.getInstance(classOf[CompileFlowGraphTransferHandler])

  /**
    * 拖拽完成
    */
  override def importData(component: JComponent, transferHandler: Transferable): Boolean = {
    var result: Boolean = false

    if (isLocalDrag) {
      result = true
    } else {
      try {
        updateImportCount(transferHandler)

        component match {
          case component: CompileFlowGraphComponent =>
            if (component.isEnabled) {
              val transferData = transferHandler.getTransferData(new CompileFlowPaletteDataFlavor(DataFlavor.javaSerializedObjectMimeType + ";class=" + classOf[CompileFlowNodePaletteItem].getName, null, classOf[CompileFlowNodePaletteItem].getClassLoader))
              //LOG.info("🀄️传送的数据=> "+transferData.toString())
              val adapter = component.getAdapter
              val point = getLocation()
              var compileFlowElementAddCommand: TGraphEditCommand = null
              transferData match {
                case data: CompileFlowNodePaletteItem => {
                  compileFlowElementAddCommand = new CompileFlowAddCommand(adapter, data, point,GraphEditCommand.ADD_GRAPH_COMMAND_NAME)
                  if (compileFlowElementAddCommand != null) {
                    //LOG.info("👈坐标为:(" + point.x + "." + point.y + ")")
                    //--目标节点单元
                    val cell = component.getCellAt(point.x, point.y)
                    if(cell.isInstanceOf[CompileFlowLoopCell]){
                      //--重新折算坐标
                      val dx=point.x-cell.asInstanceOf[CompileFlowLoopCell].getGeometry.getX
                      val dy=point.y-cell.asInstanceOf[CompileFlowLoopCell].getGeometry.getY
                      point.x=dx.toInt
                      point.y=dy.toInt
                    }
                    compileFlowElementAddCommand.setEffectCell(cell.asInstanceOf[BaseCell])
                    val componentEditDomain = adapter.getAdapter(classOf[GraphEditDomain]).asInstanceOf[GraphEditDomain]
                    componentEditDomain.getCommandStack.execute(compileFlowElementAddCommand)
                  }
                }
                case _ =>
              }
            }
          case _ =>
        }
      }
      catch {
        case ex: Exception => LOG.error("Failed to import data", ex)
      }
    }
    result
  }

}