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

package cn.shanghai.oyb.compileflow.window.palette.manager

import cn.shanghai.oyb.compileflow.common.window.CommonToolWindowManager
import cn.shanghai.oyb.compileflow.window.palette.CompileFlowPaletteToolWindowContent
import cn.shanghai.oyb.compileflow.window.palette.manager.actions.ComponentToggleEditorModeAction
import cn.shanghai.oyb.compileflow.window.properties.CompileFlowPropertiesToolWindowContent
import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.window.TToolWindowContent
import cn.shanghai.oyb.flow.core.window.listener.TSelectionListener
import cn.shanghai.oyb.flow.core.window.manager.actions.EditorToggleAction
import cn.shanghai.oyb.flow.core.window.provider.TSelectionListenerProvider
import cn.shanghai.oyb.jgraphx.component.GraphComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi
import com.intellij.openapi.wm.{ToolWindowAnchor, ToolWindowManager}
import com.intellij.ui.content.{Content, ContentManager}
import com.intellij.util.ui.update.Update
import icons.CompileFlowIcons


/**
  * 流程工具调色面板窗口伴生对象
  *
  * @author ouyubin
  */


object CompileFlowPaletteToolWindowManager {

  def getInstance(editorPanel: TGraphicEditPanel): TToolWindowContent = {
    val manager: CompileFlowPaletteToolWindowManager = getInstance(editorPanel.getProject)
    if (manager.isEditorMode) {
      return manager.getContent(editorPanel).asInstanceOf[CompileFlowPropertiesToolWindowContent]
    }
    manager.toolWindowContent
  }

  def getInstance(project: Project): CompileFlowPaletteToolWindowManager = {
    project.getComponent(classOf[CompileFlowPaletteToolWindowManager])
  }
}

/**
  * 流程工具调色面板窗口管理器
  *
  * @param project
  * @param fileEditorManager
  */


class CompileFlowPaletteToolWindowManager(project: Project, fileEditorManager: FileEditorManager) extends CommonToolWindowManager(project, fileEditorManager) {

  //--构建子窗口
  protected def initToolWindow {
    //--已编程方式注册一个子窗口
    toolWindow = ToolWindowManager.getInstance(project).registerToolWindow("Compile Flow调色板", false, getAnchor, project, true,true)
    toolWindow.setIcon(CompileFlowIcons.PALETTE_ICON)
    //--判断是否为无头环境
    if (!ApplicationManager.getApplication.isHeadlessEnvironment) {
      toolWindow.getComponent.putClientProperty(ToolWindowContentUi.HIDE_ID_LABEL, "true")
    }
    //--设定工具栏动作集
    //(componentToolWindow.asInstanceOf[ToolWindowEx]).setTitleActions(toolWindowContent.createActions: _*)
    //--扩充齿轮装备动作集
    initGearActions

    val contentManager: ContentManager = toolWindow.getContentManager
    val content: Content = contentManager.getFactory.createContent(toolWindowContent.getToolWindowContentPanel, "Compile Flow调色板", false)
    content.setCloseable(false)
    //content.setPreferredFocusableComponent(doradoToolWindowContent.getComponentTree)
    contentManager.addContent(content)
    contentManager.setSelectedContent(content, true)
    toolWindow.setAvailable(true, null)
  }


  override def getAnchor: ToolWindowAnchor = {
     ToolWindowAnchor.LEFT
  }


  override def createToolWindowContent: TToolWindowContent = {
    new CompileFlowPaletteToolWindowContent(project, true)
  }

  def createComponentEditorLeftToggleAction: EditorToggleAction = {
    new ComponentToggleEditorModeAction(this, project, ToolWindowAnchor.LEFT)
  }

  def createComponentEditorRightToggleAction: EditorToggleAction = {
    new ComponentToggleEditorModeAction(this, project, ToolWindowAnchor.RIGHT)
  }

  override def getComponentName: String = {
    "Compile-Flow-Palette"
  }

  override def bindToEditPanel(editPanel: TGraphicEditPanel) {
    myWindowQueue.cancelAllUpdates()
    myWindowQueue.queue(new Update("update") {
      def run() {
        if (!myToolWindowReady || myToolWindowDisposed) {
          return
        }
        //--守护绑定编辑器面板类型
        if (toolWindow == null) {
          if (editPanel == null) {
            return
          }
          initToolWindow
        }
        if (editPanel != null) {
          //--守护绑定编辑器面板类型
          if (editPanel.getPaletteToolWindowManager.isInstanceOf[CompileFlowPaletteToolWindowManager]) {
            updateToolWindow(editPanel)
          } else {
            toolWindow.setAvailable(false, null)
          }
        } else {
          updateToolWindow(editPanel)
        }
        //--开始进行提供器监听绑定
        if (getToolWindowContent != null&&editPanel!=null) {
          getToolWindowContent.asInstanceOf[TSelectionListenerProvider].addSelectionListener(editPanel.getAdapter(classOf[GraphComponent]).asInstanceOf[TSelectionListener])
        }
      }
    })
  }
}
