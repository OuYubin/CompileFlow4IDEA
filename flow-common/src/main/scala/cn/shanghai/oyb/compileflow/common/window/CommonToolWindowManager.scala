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

package cn.shanghai.oyb.compileflow.common.window

import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.window.TToolWindowContent
import cn.shanghai.oyb.flow.core.window.impl.BaseToolWindow
import cn.shanghai.oyb.flow.core.window.manager.impl.AbstractToolWindowManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor
import org.jetbrains.annotations.Nullable

/**
  *
  * 组件通用工具窗口管理器
  *
  * @author ouyubin
  *
  */

abstract class CommonToolWindowManager(project: Project, fileEditorManager: FileEditorManager) extends AbstractToolWindowManager(project, fileEditorManager) {

  //-设定窗口内容
  setToolWindowContent(createToolWindowContent)

  //--构建子窗口
  protected def initToolWindow

  protected def getAnchor: ToolWindowAnchor = {
    //    val customization: DesignerCustomizations = getCustomizations
    //    return if (customization != null) customization.getStructureAnchor else ToolWindowAnchor.RIGHT
    ToolWindowAnchor.RIGHT
  }

  protected def updateToolWindow(@Nullable editPanel: TGraphicEditPanel) {
    toolWindowContent.update(editPanel)
    if (editPanel == null) {
      toolWindow.setAvailable(false, null)
    }
    else {
      toolWindow.setAvailable(true, null)
      toolWindow.show(null)
    }
  }

  override def disposeComponent {
    toolWindowContent.dispose
  }

  override def getComponentName: String = {
    super.getComponentName
  }

  /**
    *
    * 构建窗口
    *
    * @param editPanel
    * @return
    */
  def createToolWindow(editPanel: TGraphicEditPanel): BaseToolWindow = {
    //val toolWindowContent: TComponentToolWindowContent = createToolWindowContent
    //--注册监听等动作
    toolWindowContent.update(editPanel)
    //createToolWindow(editPanel, toolWindowContent, "Component", FlowIcons.FLOW_FILE_ICON, toolWindowContent.getToolWindowContentPanel, null, 320, null)
    null
  }

  /**
    * 子类必须实现窗口内容
    *
    * @return
    */
  def createToolWindowContent: TToolWindowContent

}

