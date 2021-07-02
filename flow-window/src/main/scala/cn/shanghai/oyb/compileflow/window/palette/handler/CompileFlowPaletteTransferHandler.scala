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

package cn.shanghai.oyb.compileflow.window.palette.handler

import cn.shanghai.oyb.compileflow.palette.model.CompileFlowNodePaletteItem
import cn.shanghai.oyb.compileflow.palette.model.dataFlavor.CompileFlowPaletteDataFlavor
import cn.shanghai.oyb.compileflow.window.palette.CompileFlowPaletteItemsComponent

import java.awt.datatransfer.{DataFlavor, Transferable}
import javax.swing.TransferHandler

/**
  * @author ouyubin
  */
class CompileFlowPaletteTransferHandler(val paletteItemsComponent: CompileFlowPaletteItemsComponent) extends TransferHandler with Transferable {


  val flowPaletteDataFlavor = new CompileFlowPaletteDataFlavor(DataFlavor.javaSerializedObjectMimeType + ";class=" + classOf[CompileFlowNodePaletteItem].getName, null, classOf[CompileFlowNodePaletteItem].getClassLoader)

  override def getTransferDataFlavors: Array[DataFlavor] = {
    Array(flowPaletteDataFlavor)
  }

  override def isDataFlavorSupported(flavor: DataFlavor): Boolean = {
    true
  }

  override def getTransferData(flavor: DataFlavor): Object = {
    if(flavor.equals(flowPaletteDataFlavor)){
      return paletteItemsComponent.getSelectedValue
    }
    null
  }


}
