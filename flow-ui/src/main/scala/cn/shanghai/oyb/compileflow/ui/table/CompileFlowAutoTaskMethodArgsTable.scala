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

package cn.shanghai.oyb.compileflow.ui.table

import cn.shanghai.oyb.compileflow.ui.models.CompileFlowMethodArgsModel
import cn.shanghai.oyb.compileflow.ui.renderer.CompileFlowParamsTableRenderer
import cn.shanghai.oyb.compileflow.ui.table.cell.editor.CompileFlowOptionsCellEditor
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.ui.table.JBTable

/**
 *
 * 服务参数属性表
 *
 * @author ouyubin
 *
 */
class CompileFlowAutoTaskMethodArgsTable(model: CompileFlowMethodArgsModel) extends JBTable(model) with DataProvider {

  this.setFillsViewportHeight(true)
  val tableRenderer = new CompileFlowParamsTableRenderer
  getColumnModel.getColumn(0).setCellRenderer(tableRenderer)
  val IN_OUT_TYPES: Array[String] = Array("param", "inner", "return")
  getColumnModel.getColumn(3).setCellEditor(new CompileFlowOptionsCellEditor(IN_OUT_TYPES))

  override def getData(dataId: String): AnyRef = {
    null
  }
}
