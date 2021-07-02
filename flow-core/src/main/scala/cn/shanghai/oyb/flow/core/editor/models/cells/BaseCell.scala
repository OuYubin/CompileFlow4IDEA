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

package cn.shanghai.oyb.flow.core.editor.models.cells

import cn.shanghai.oyb.flow.core.editor.models.edges.TEdgeVisitor
import cn.shanghai.oyb.jgraphx.model.Cell
import cn.shanghai.oyb.jgraphx.shape.IShape
import com.mxgraph.model.mxGeometry

/**
  * 组件图形编辑单元,将图元直接与单元格进行绑定,用于和jgraphx对接
  *
  * @author ouyubin
  */


class BaseCell(value: Object, geometry: mxGeometry, style: String) extends Cell(value, geometry, style) with TCell {

  private var myShape: IShape = _

  def this(value: Object) {
    this(value, null, null)
  }

  def this(value: Object, geometry: mxGeometry) {
    this(value, geometry, null)
  }

  def accept(visitor: TEdgeVisitor): Unit = {
    visitor.visitComponentCell(this)
  }

  /**
    * 构建形状
    *
    * @return
    */
  def getShape= myShape

}
