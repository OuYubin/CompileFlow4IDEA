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

package cn.shanghai.oyb.compileflow.window.properties

import cn.shanghai.oyb.compileflow.window.properties.api.CompileFlowPropertyPageEP
import cn.shanghai.oyb.compileflow.window.properties.manager.CompileFlowPropertyPageExtensionManager
import cn.shanghai.oyb.compileflow.window.properties.models.CompileFlowPropertyPagMultiKey
import cn.shanghai.oyb.flow.core.editor.models.cells.{BaseCell, TCell}
import cn.shanghai.oyb.flow.core.editor.models.edges.BaseEdge
import cn.shanghai.oyb.flow.core.internal.TAdaptable
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement
import cn.shanghai.oyb.flow.core.window.TToolWindow
import cn.shanghai.oyb.flow.core.window.pages.TPropertyPage
import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.collections.map.MultiKeyMap
import org.apache.commons.lang3.StringUtils

import java.awt.CardLayout
import scala.collection.JavaConversions._

/**
  *
  * 组件属性页容器对象
  *
  * @author ouyubin
  *
  */
class CompileFlowProperties extends TToolWindow {

  val LOG: Logger = Logger.getInstance(classOf[CompileFlowProperties])

  var propertyPageMap = new MultiKeyMap()


  def init(): Unit = {
    val propertyPageExtensions = CompileFlowPropertyPageExtensionManager.getFlowPropertiesPageExtensions
    for (i <- 0 until propertyPageExtensions.length) {
      val propertyPageExtension = propertyPageExtensions(i)
      val flowPropertyPageEP = propertyPageExtension.asInstanceOf[CompileFlowPropertyPageEP]
      val propertyPage = flowPropertyPageEP.createFlowPropertyPage
      val mappingName = flowPropertyPageEP.getMappingName
      val ownerName = flowPropertyPageEP.getOwnerName
      val multiKey = new CompileFlowPropertyPagMultiKey(ownerName, mappingName)
      propertyPageMap.put(multiKey, propertyPage)
    }
  }


  def createPage(): Unit = {
    //--通过cardLayout构建卡片效果
    val cardLayout = new CardLayout()
    this.setLayout(cardLayout)
    val keys = propertyPageMap.keySet
    val iterator = keys.iterator
    while (iterator.hasNext) {
      val key = iterator.next
      val page = propertyPageMap.get(key)
      page.asInstanceOf[TPropertyPage].createControl(this)
      val ownerName = key.asInstanceOf[CompileFlowPropertyPagMultiKey].getKey(0)
      val mappingName = key.asInstanceOf[CompileFlowPropertyPagMultiKey].getKey(1)
      if (ownerName == null) {
        add(page.asInstanceOf[TPropertyPage], mappingName)
      } else {
        add(page.asInstanceOf[TPropertyPage], ownerName + ":" + mappingName)
      }
    }
  }

  def refresh(componentCell: TCell): Unit = {
    val value = componentCell.asInstanceOf[BaseCell].getValue
    if (value != null) {
      value match {
        case value: XmlTagModelElement => {
          val localName = value.asInstanceOf[XmlTagModelElement].getXmlTag.getLocalName
          //--通过组件单元获取编辑器
          var adapter: TAdaptable = null
          if (componentCell.isInstanceOf[BaseEdge]) {
            adapter = componentCell.asInstanceOf[BaseEdge].getSource.asInstanceOf[BaseCell].getMyEditPart.getAdapter()
          } else {
            adapter = componentCell.myEditPart.getAdapter()
          }
          for ((mapping, page) <- propertyPageMap) {
            val ownerName = mapping.asInstanceOf[CompileFlowPropertyPagMultiKey].getKey(0).asInstanceOf[String]
            val mappingName = mapping.asInstanceOf[CompileFlowPropertyPagMultiKey].getKey(1).asInstanceOf[String]

            if (ownerName == null) {
              if (page != null && StringUtils.equals(localName, mappingName)) {
                this.getLayout.asInstanceOf[CardLayout].show(this, localName)
                refresh(adapter, page.asInstanceOf[TPropertyPage], value.asInstanceOf[XmlTagModelElement])
                return
              }
            }
            else {
              val xmlTag = value.asInstanceOf[XmlTagModelElement].getXmlTag
              if (xmlTag.getParentTag != null) {
                val currentOwnerName = xmlTag.getParentTag.getName
                if (currentOwnerName.nonEmpty) {
                  if (page != null && StringUtils.equals(localName, mappingName) && StringUtils.equals(currentOwnerName, ownerName)) {
                    this.getLayout.asInstanceOf[CardLayout].show(this, ownerName + ":" + localName)
                    refresh(adapter, page.asInstanceOf[TPropertyPage], value.asInstanceOf[XmlTagModelElement])
                    return
                  }
                }
              }
            }

          }
        }
        case _ =>
      }
    }
  }

  private def refresh(adapter: TAdaptable, page: TPropertyPage, value: XmlTagModelElement): Unit = {
    setComponentPropertyPage(page.asInstanceOf[TPropertyPage])
    componentPropertyPage.setInput(adapter, value.asInstanceOf[XmlTagModelElement])
    refresh()
  }

  def refresh(): Unit = {
    if (componentPropertyPage != null)
      componentPropertyPage.refresh()
  }

}

