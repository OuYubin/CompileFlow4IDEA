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

package cn.shanghai.oyb.compileflow.ui.models

import cn.shanghai.oyb.compileflow.common.commands.SetCommand
import cn.shanghai.oyb.compileflow.ui.table.models.CompileFlowPropertyModel
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain
import cn.shanghai.oyb.flow.core.internal.TAdaptable
import cn.shanghai.oyb.flow.core.window.pages.TPropertyPage
import com.intellij.psi.xml.XmlTag

import javax.swing.table.DefaultTableModel
import scala.beans.BeanProperty

/**
 *
 * 流程参数列表模型
 *
 * @author ouyubin
 *
 *
 */
class CompileFlowParamsModel(val propertyPage: TPropertyPage) extends DefaultTableModel() {

  @BeanProperty var adapter: TAdaptable = _

  var params: java.util.List[CompileFlowPropertyModel[Any]] = _

  val columnNames: List[String] = List("名称", "类型", "缺省值", "输入/出类型", "描述", "上下文名称")


  override def getRowCount: Int = {
    if (params != null)
      params.size()
    else
      0
  }

  override def getColumnCount: Int = {
    columnNames.length
  }


  override def getColumnName(column: Int): String = {
    columnNames(column)
  }

  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
    if (params.isEmpty) {
      null
    } else {
      val flowParam = params.get(rowIndex)
      val paramElement = flowParam.getProperty

      columnIndex match {
        case 0 =>
          if (paramElement.isInstanceOf[String]) {
            return paramElement.asInstanceOf[String]
          } else {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("name")
          }
        case 1 =>
          if (paramElement.isInstanceOf[XmlTag]) {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("dataType")
          }
        case 2 =>
          if (paramElement.isInstanceOf[XmlTag]) {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("defaultValue")
          }
        case 3 =>
          if (paramElement.isInstanceOf[XmlTag]) {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("inOutType")
          }
        case 4 =>
          if (paramElement.isInstanceOf[XmlTag]) {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("description")
          }
        case 5 =>
          if (paramElement.isInstanceOf[XmlTag]) {
            return paramElement.asInstanceOf[XmlTag].getAttributeValue("contextVarName")
          }
        case _ =>
      }
      null
    }

  }


  override def setValueAt(value: Any, rowIndex: Int, columnIndex: Int): Unit = {
    val lftEditDomain: GraphEditDomain = adapter.getAdapter(classOf[GraphEditDomain]).asInstanceOf[GraphEditDomain]
    val lftCommandStack: GraphEditCommandStack = lftEditDomain.getCommandStack
    val flowParam = params.get(rowIndex)
    val param = flowParam.asInstanceOf[CompileFlowPropertyModel[Any]].getProperty
    val input = propertyPage.getMyInput
    columnIndex match {
      case 0 =>
        if (param.isInstanceOf[XmlTag]) {
          val xmlTag = param.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "name", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
      case 1 =>
        val xmlTag = param.asInstanceOf[XmlTag]
        lftCommandStack.execute(new SetCommand(adapter, xmlTag, "dataType", value.asInstanceOf[String]))
        propertyPage.refresh()
      case 2 =>
        val xmlTag = param.asInstanceOf[XmlTag]
        lftCommandStack.execute(new SetCommand(adapter, xmlTag, "defaultValue", value.asInstanceOf[String]))
        propertyPage.refresh()
      case 3 =>
        val xmlTag = param.asInstanceOf[XmlTag]
        lftCommandStack.execute(new SetCommand(adapter, xmlTag, "inOutType", value.asInstanceOf[String]))
        propertyPage.refresh()
      case 4 =>
        if (param.isInstanceOf[XmlTag]) {
          val xmlTag = param.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "description", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
      case 5 =>
        if (param.isInstanceOf[XmlTag]) {
          val xmlTag = param.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "contextVarName", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
    }
  }

  override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = {
    true
  }

  def setParams(params: java.util.List[CompileFlowPropertyModel[Any]]): Unit = {
    this.params = params
    this.fireTableDataChanged()
  }

  def getParams: java.util.List[CompileFlowPropertyModel[Any]] = {
    params
  }

}
