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

package cn.shanghai.oyb.flow.core.window

import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel

import javax.swing.JPanel
import scala.beans.BeanProperty

/**
  * 组件窗口内容接口
  *
  * @author ouyubin
  */
trait TToolWindowContent {

  def dispose

  def update(editorPanel: TGraphicEditPanel)

  def getToolWindowContentPanel: JPanel

  @BeanProperty var propertiesPage: TToolWindow = _

}
