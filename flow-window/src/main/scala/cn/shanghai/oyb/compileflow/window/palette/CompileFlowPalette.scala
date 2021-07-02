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

package cn.shanghai.oyb.compileflow.window.palette

import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants
import cn.shanghai.oyb.compileflow.palette.model.{CompileFlowConnectionPaletteItem, CompileFlowNodePaletteItem, CompileFlowPaletteGroup}
import cn.shanghai.oyb.flow.core.editor.models.cells.TCell
import cn.shanghai.oyb.flow.core.internal.TAdaptable
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement
import cn.shanghai.oyb.flow.core.window.TToolWindow
import cn.shanghai.oyb.flow.core.window.pages.TPropertyPage

import java.awt.GridLayout
import java.util
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.JBEmptyBorder
import icons.{CompileFlowIcons, CompileFlowImages, CompileFlowSvgImages}

import javax.swing.ImageIcon
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * @author ouyubin
 */
class CompileFlowPalette extends TToolWindow() {

  setLayout(new GridLayout(1, 1))

  var groupComponents: java.util.List[CompileFlowPaletteGroupComponent] = new util.ArrayList[CompileFlowPaletteGroupComponent]()

  @BeanProperty var itemsComponents: java.util.List[CompileFlowPaletteItemsComponent] = new util.ArrayList[CompileFlowPaletteItemsComponent]()

  def init(): Unit = {
    val flowConnectionPaletteGroup = new CompileFlowPaletteGroup("工具")
    val generalizationPaletteItem = new CompileFlowConnectionPaletteItem("连接线", "连接线", CompileFlowIcons.CONNECTOR_ICON, CompileFlowSvgImages.CONNECTOR_IMAGE, CompileFlowSvgImages.BAN_IMAGE, "连接线", "1.0", "", "连接线")
    flowConnectionPaletteGroup.addItem(generalizationPaletteItem)

    val flowConnectionGroupComponent = new CompileFlowPaletteGroupComponent(flowConnectionPaletteGroup)
    val flowConnectionItemsComponent = new CompileFlowPaletteItemsComponent(flowConnectionPaletteGroup)
    flowConnectionGroupComponent.setItemsComponent(flowConnectionItemsComponent)
    itemsComponents.add(flowConnectionItemsComponent)
    groupComponents.add(flowConnectionGroupComponent)

    val flowPaletteGroup = new CompileFlowPaletteGroup("流程节点")
    val startPaletteItem = new CompileFlowNodePaletteItem("开始", CompileFlowXmlTagConstants.START_TAG_NAME, CompileFlowIcons.START_ICON, "开始", "2.0", "1.0", "1.0弃用",CompileFlowImages.START_IMAGE)
    val endPaletteItem = new CompileFlowNodePaletteItem("结束",CompileFlowXmlTagConstants.END_TAG_NAME, CompileFlowIcons.END_ICON, "结束", "2.0", "1.0", "1.0弃用",CompileFlowImages.END_IMAGE)
    val autoPaletteItem = new CompileFlowNodePaletteItem("任务",CompileFlowXmlTagConstants.AUTO_TASK_TAG_NAME, CompileFlowIcons.TASK_ICON, "任务", "2.0", "1.0", "1.0弃用",new ImageIcon(CompileFlowSvgImages.AUTO_TASK_IMAGE))
    val decisionPaletteItem = new CompileFlowNodePaletteItem("选择",CompileFlowXmlTagConstants.DECISION_TAG_NAME, CompileFlowIcons.DECISION_ICON, "选择", "2.0", "1.0", "1.0弃用",CompileFlowImages.DECISION_IMAGE)
    val scriptPaletteItem = new CompileFlowNodePaletteItem("脚本任务", CompileFlowXmlTagConstants.SCRIPT_TAG_NAME, CompileFlowIcons.SCRIPT_TASK_ICON, "脚本任务", "2.0", "1.0", "1.0弃用",CompileFlowImages.SCRIPT_IMAGE)
    val subBpmPaletteItem = new CompileFlowNodePaletteItem("子任务",CompileFlowXmlTagConstants.SUB_BPM_TAG_NAME, CompileFlowIcons.SUB_BPM_ICON, "子流程", "2.0", "1.0", "1.0弃用",CompileFlowImages.SUB_BPM_IMAGE)
    val notePaletteItem = new CompileFlowNodePaletteItem("注释", CompileFlowXmlTagConstants.NOTE_TAG_NAME, CompileFlowIcons.NOTE_ICON, "注释", "2.0", "1.0", "1.0弃用",CompileFlowImages.NOTE_IMAGE)
    val loopPaletteItem = new CompileFlowNodePaletteItem("循环",CompileFlowXmlTagConstants.LOOP_PROCESS_TAG_NAME, CompileFlowIcons.LOOP_PROCESS_ICON, "循环", "2.0", "1.0", "1.0弃用",new ImageIcon(CompileFlowSvgImages.LOOP_IMAGE))
    val continuePaletteItem = new CompileFlowNodePaletteItem("继续", CompileFlowXmlTagConstants.CONTINUE_TAG_NAME, CompileFlowIcons.CONTINUE_ICON, "继续", "2.0", "1.0", "1.0弃用",CompileFlowImages.CONTINUE_IMAGE)
    val breakPaletteItem = new CompileFlowNodePaletteItem("中止", CompileFlowXmlTagConstants.BREAK_TAG_NAME, CompileFlowIcons.BREAK_ICON, "中止", "2.0", "1.0", "1.0弃用",new ImageIcon(CompileFlowSvgImages.BREAK_IMAGE))
    flowPaletteGroup.addItem(startPaletteItem)
    flowPaletteGroup.addItem(endPaletteItem)
    flowPaletteGroup.addItem(autoPaletteItem)
    flowPaletteGroup.addItem(decisionPaletteItem)
    flowPaletteGroup.addItem(scriptPaletteItem)
    flowPaletteGroup.addItem(subBpmPaletteItem)
    flowPaletteGroup.addItem(notePaletteItem)
    flowPaletteGroup.addItem(loopPaletteItem)
    flowPaletteGroup.addItem(continuePaletteItem)
    flowPaletteGroup.addItem(breakPaletteItem)

    val flowGroupComponent = new CompileFlowPaletteGroupComponent(flowPaletteGroup)
    val flowItemsComponent = new CompileFlowPaletteItemsComponent(flowPaletteGroup)
    flowGroupComponent.setItemsComponent(flowItemsComponent)
    groupComponents.add(flowGroupComponent)
    itemsComponents.add(flowItemsComponent)


  }

  def createPage(): Unit = {
    val flowPaletteContainer = new CompileFlowPaletteContainer
    val scrollPanel = ScrollPaneFactory.createScrollPane(flowPaletteContainer)
    scrollPanel.setBorder(new JBEmptyBorder(0, 0, 0, 0))
    for (groupComponent <- groupComponents) {
      flowPaletteContainer.add(groupComponent)
    }
    for (itemsComponent <- itemsComponents) {
      flowPaletteContainer.add(itemsComponent)
    }
    add(scrollPanel)

  }

  def refresh(lsbCell: TCell): Unit = {
  }

  private def refresh(adapter: TAdaptable, page: TPropertyPage, value: XmlTagModelElement): Unit = {
  }

  def refresh(): Unit = {
  }
}
