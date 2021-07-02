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

package cn.shanghai.oyb.flow.core.swing

import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.jgraphx.canvas.InteractiveCanvas
import cn.shanghai.oyb.jgraphx.model.Cell
import cn.shanghai.oyb.jgraphx.shape.IShape

/**
  *
  *
  * @author ouyubin
  *
  */
class Canvas extends InteractiveCanvas {


  @Override
  def createComponentShape(cell: Cell): IShape = {
    cell match {
      case cell: BaseCell =>
        cell.getShape
      case _ => null
    }
  }


}
