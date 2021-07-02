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

import javax.swing.table.AbstractTableModel
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 *
 * 服务方法参数表模型
 *
 * @author ouyubin
 *
 *
 */
class CompileFlowMethodArgsModel(val propertyPage: TPropertyPage) extends AbstractTableModel {

  @BeanProperty var adapter: TAdaptable = _

  var myArgs: java.util.List[CompileFlowPropertyModel[Any]] = _

  val columnNames: List[String] = List("名称", "类型", "缺省值", "输入/出类型", "描述", "上下文变量名称")


  override def getRowCount: Int = {
    if (myArgs != null)
      myArgs.size()
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
    if (myArgs.isEmpty) {
      myArgs(rowIndex)
    } else {
      val arg = myArgs.get(rowIndex)
      val varTag = arg.getProperty.asInstanceOf[XmlTag]
      columnIndex match {
        case 0 => varTag.getAttributeValue("name")
        case 1 => varTag.getAttributeValue("dataType")
        case 2 => varTag.getAttributeValue("defaultValue")
        case 3 => varTag.getAttributeValue("inOutType")
        case 4 => varTag.getAttributeValue("description")
        case 5 => varTag.getAttributeValue("contextVarName")
        case _ => null
      }
    }
  }

  override def setValueAt(value: Any, rowIndex: Int, columnIndex: Int): Unit = {
    val lftEditDomain: GraphEditDomain = adapter.getAdapter(classOf[GraphEditDomain]).asInstanceOf[GraphEditDomain]
    val lftCommandStack: GraphEditCommandStack = lftEditDomain.getCommandStack
    val flowArg = myArgs.get(rowIndex)
    val argElement = flowArg.getProperty
    columnIndex match {
      case 0 =>
        if (argElement.isInstanceOf[XmlTag]) {
          val xmlTag = argElement.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "name", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
//      case 1 =>
//        val xmlTag = argElement.asInstanceOf[XmlTag]
//        lftCommandStack.execute(new LFTSetCommand(adapter, xmlTag, "dataType", value.asInstanceOf[String]))
//        propertyPage.refresh()
      case 2 =>
        val xmlTag = argElement.asInstanceOf[XmlTag]
        lftCommandStack.execute(new SetCommand(adapter, xmlTag, "defaultValue", value.asInstanceOf[String]))
        propertyPage.refresh()
      case 3 =>
        val xmlTag = argElement.asInstanceOf[XmlTag]
        lftCommandStack.execute(new SetCommand(adapter, xmlTag, "inOutType", value.asInstanceOf[String]))
        propertyPage.refresh()
      case 4 =>
        if (argElement.isInstanceOf[XmlTag]) {
          val xmlTag = argElement.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "desc", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
      case 5 =>
        if (argElement.isInstanceOf[XmlTag]) {
          val xmlTag = argElement.asInstanceOf[XmlTag]
          lftCommandStack.execute(new SetCommand(adapter, xmlTag, "contextVarName", value.asInstanceOf[String]))
          propertyPage.refresh()
        }
    }
  }

  def setArgs(args: java.util.List[CompileFlowPropertyModel[Any]]): Unit = {
    this.myArgs = args
    this.fireTableDataChanged()
  }

  override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = {
    val editableColumnIndexs = Array(0, 2, 3, 4, 5)
    editableColumnIndexs.contains(columnIndex)
  }
}
