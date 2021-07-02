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

package cn.shanghai.oyb.compileflow.palette.model

import scala.beans.BeanProperty


/**
 * @author ouyubin
 */
class CompileFlowPaletteGroup(@BeanProperty val name:String) {

  protected final val paletteItems: java.util.List[CompileFlowNodePaletteItem] = new java.util.ArrayList[CompileFlowNodePaletteItem]


  def addItem(item: CompileFlowNodePaletteItem) {
    paletteItems.add(item)
  }

  def getItems: java.util.List[CompileFlowNodePaletteItem] = {
    paletteItems
  }

}
