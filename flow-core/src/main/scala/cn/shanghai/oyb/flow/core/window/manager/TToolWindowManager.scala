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

package cn.shanghai.oyb.flow.core.window.manager

import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.window.TToolWindowContent
import cn.shanghai.oyb.flow.core.window.impl.BaseToolWindow
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.wm.{ToolWindow, ToolWindowAnchor}

import scala.beans.BeanProperty

/**
  * 组件工具窗口管理器,继承IDEA Project组件
  *
  * @author ouyubin
  */
trait TToolWindowManager extends ProjectComponent {

  @volatile
  @BeanProperty var toolWindow: ToolWindow = _

  @BeanProperty var toolWindowContent: TToolWindowContent = _

  /**
    * 已激活的编辑器面板
    *
    * @return
    */
  def getActiveEditPanel: TGraphicEditPanel


  /**
    * 绑定编辑面板
    *
    * @param editorPanel
    */
  def bindToEditPanel(editorPanel: TGraphicEditPanel)


  /**
    * 构建工具window窗口类似与eclipse的view视图
    *
    * @param designer
    * @return
    */
  def createToolWindow(designer: TGraphicEditPanel): BaseToolWindow


  /**
    * 构建window窗口内容
    *
    * @return
    */
  def createToolWindowContent: TToolWindowContent


  def getEditorMode: ToolWindowAnchor


  def setEditorMode(newState: ToolWindowAnchor)
}
