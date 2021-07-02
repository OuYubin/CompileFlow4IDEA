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

package cn.shanghai.oyb.compileflow.editor.edges

import cn.shanghai.oyb.compileflow.editor.shapes.CompileFlowTransitionShape
import cn.shanghai.oyb.flow.core.editor.models.edges.BaseEdge
import cn.shanghai.oyb.jgraphx.shape.IShape
import com.mxgraph.model.mxGeometry

/**
  * 
  * 流程链接线单元对象
  *
  * @author ouyubin
  *
  * @param value
  * @param geometry
  * @param style
  */
class CompileFlowTransitionEdge(value: Object, geometry: mxGeometry, style: String) extends BaseEdge(value, geometry, style) {


  def this(value: Object) {
    this(value, new mxGeometry(), null)
  }

  override def getShape: IShape = new CompileFlowTransitionShape(getValue)

}
