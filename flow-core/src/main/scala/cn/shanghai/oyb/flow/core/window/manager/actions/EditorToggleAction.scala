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

package cn.shanghai.oyb.flow.core.window.manager.actions

import cn.shanghai.oyb.flow.core.window.manager.TToolWindowManager
import com.intellij.openapi.actionSystem.{AnActionEvent, ToggleAction}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.wm.ToolWindowAnchor

/**
  * @author ouyubin
  */
abstract class EditorToggleAction(manager: TToolWindowManager, project: Project, anchor: ToolWindowAnchor) extends ToggleAction(StringUtil.capitalize(anchor.toString), "Pin/unpin tool window to " + anchor + " side Component Editor", null) {

  override def isSelected(e: AnActionEvent): Boolean = {
    anchor == manager.getEditorMode
  }

  override def setSelected(e: AnActionEvent, state: Boolean) {
    if (state) {
      manager.setEditorMode(anchor)
      val oppositeManager = getOppositeManager
      if (oppositeManager.getEditorMode == anchor) {
        oppositeManager.setEditorMode(if (anchor == ToolWindowAnchor.LEFT) ToolWindowAnchor.RIGHT else ToolWindowAnchor.LEFT)
      }
    } else {
      manager.setEditorMode(null)
    }
  }

  def getOppositeManager: TToolWindowManager
}
