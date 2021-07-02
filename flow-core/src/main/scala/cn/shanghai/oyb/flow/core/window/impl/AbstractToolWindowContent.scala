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

package cn.shanghai.oyb.flow.core.window.impl

import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.editor.surface.listeners.TEditPanelSelectionChangeEventListener
import cn.shanghai.oyb.flow.core.window.TToolWindowContent

import java.awt.Dimension
import com.intellij.openapi.project.Project
import com.intellij.ui.{IdeBorderFactory, ScrollPaneFactory, SideBorder}

import javax.swing.{JPanel, ScrollPaneConstants}

/**
  *
  * 设计器属性编辑子窗口内容构建,早期直接使用Andriod Studio中设计的DesignerToolWindow,但该类为final,其工具栏无法进行扩展定义,故需要重新定义该类方便自由控制
  *
  * 将自己作为编辑面板的监听器,打开编辑器时将自己作为监听器注册到编辑面板的监听列表当中
  *
  * @author ouyubin
  */
abstract class AbstractToolWindowContent(project: Project, updateOrientation: Boolean) extends TToolWindowContent with TEditPanelSelectionChangeEventListener {

  //var doradoTreeBuilder: DoradoTreeBuilder = _
  //@BeanProperty var doradoTree = new DoradoTree
  val componentToolWindowContentScrollPane = ScrollPaneFactory.createScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED)
  componentToolWindowContentScrollPane.setBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM))
  componentToolWindowContentScrollPane.setPreferredSize(new Dimension(250, -1))
  //  componentTree.initQuickFixManager(treeScrollPane.getViewport())

  //--需要通过子类复写createToolWindowContentPanel来完成关于属性表格的子类化
  var propertiesPanel = createToolWindowContentPanel(project)

  //val toolWindowPanel = new JBPanel
  //componentToolWindowContentScrollPane.add(propertiesPanel)
  componentToolWindowContentScrollPane.getViewport.setView(propertiesPanel)
  //componentToolWindowContentScrollPane.setViewportView(propertiesPanel)

  //    new Splitter(true, 0.42f)
  //  toolWindowPanel.setDividerWidth(3)
  //  toolWindowPanel.setShowDividerControls(false)
  //  toolWindowPanel.setShowDividerIcon(false)n
  //  toolWindowPanel.setFirstComponent(componentToolWindowConentScrollPane)
  //toolWindowPanel.setSecondComponent(doradoPropertyTablePanel)

  //    if (updateOrientation) {
  //      componentToolWindowContentScrollPane.addComponentListener(new ComponentAdapter() {
  //        override def componentResized(event: ComponentEvent) {
  //          val size = componentToolWindowContentScrollPane.getSize()
  //          val newVertical = size.width < size.height
  //          if (toolWindowPanel.getOrientation() != newVertical) {
  //            toolWindowPanel.setOrientation(newVertical)
  //          }
  //        }
  //      })
  //    }


  /**
    *
    * @param editPanel
    */
  def update(editPanel: TGraphicEditPanel) {
    //    clearTreeBuilder
    //    doradoTree.newModel
    //    if (designer == null) {
    //      doradoTree.setDesignerPanel(null)
    //      doradoPropertyTablePanel.setArea(null, null)
    //    }
    //    else {
    //      doradoTree.setDesignerPanel(designer)
    //      //--初始化构建TreeBuilder
    //      doradoTreeBuilder = new DoradoTreeBuilder(doradoTree, designer)
    //      //--关联树形区域与属性编辑区域
    //      doradoPropertyTablePanel.setArea(designer, doradoTreeBuilder.getTreeArea())
    //    }
  }

  override def dispose() {
    //clearTreeBuilder()
    propertiesPanel = null
    //doradoTree = null
    //propertyTablePanel = null
  }

  def clearTreeBuilder() {
    //    if (doradoTreeBuilder != null) {
    //      Disposer.dispose(doradoTreeBuilder)
    //      doradoTreeBuilder = null
    //    }
  }

  def getToolWindowContentPanel: JPanel = {
    propertiesPanel
  }

  // def createActions(): Array[AnAction] = {


  //    val expandAll = new AnAction("Expand All", null, AllIcons.Actions.Expandall) {
  //      override def actionPerformed(event: AnActionEvent) {
  //        if (doradoTreeBuilder != null) {
  //          doradoTreeBuilder.expandAll(null)
  //        }
  //      }
  //    }
  //
  //    val collapseAll = new AnAction("Collapse All", null, AllIcons.Actions.Collapseall) {
  //      override def actionPerformed(event: AnActionEvent) {
  //        if (doradoTreeBuilder != null) {
  //          TreeUtil.collapseAll(doradoTree, 1)
  //        }
  //      }
  //    }

  // return Array(expandAll, collapseAll)
  //}


//  def getPropertyTable(): RadPropertyTable = {
//    //return propertyTablePanel.getPropertyTable()
//    return null
//  }

  //override def expandFromState() {
  //    if (componentTreeBuilder != null) {
  //      componentTreeBuilder.expandFromState()
  //    }
  //}

  // override def refresh(updateProperties: Boolean) {
  //    if (doradoTreeBuilder != null) {
  //      if (updateProperties) {
  //        //doradoTreeBuilder.selectFromSurface()
  //        doradoTreeBuilder.queueUpdate()
  //      }
  //      else {
  //        doradoTreeBuilder.queueUpdate()
  //      }
  //    }
  //}

  // @Override
  // override def updateInspections() {
  //    if (componentTree != null) {
  //      componentTree.updateInspections()
  //    }
  //    if (propertyTablePanel != null) {
  //      propertyTablePanel.getPropertyTable().updateInspections()
  //    }
  // }

  def createToolWindowContentPanel(project: Project): JPanel

}
