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

package cn.shanghai.oyb.compileflow.window.palette.manager.actions

import cn.shanghai.oyb.compileflow.window.properties.manager.CompileFlowPropertiesToolWindowManager
import cn.shanghai.oyb.flow.core.window.manager.TToolWindowManager
import cn.shanghai.oyb.flow.core.window.manager.actions.EditorToggleAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor

/**
  * @author ouyubin
  * @param manager
  * @param project
  * @param anchor
  */
class ComponentToggleEditorModeAction(manager: TToolWindowManager, project: Project, anchor: ToolWindowAnchor) extends EditorToggleAction(manager, project, anchor) {

  override def getOppositeManager: TToolWindowManager = {
    CompileFlowPropertiesToolWindowManager.getInstance(project)
    //val paletteManager = ViewPaletteToolWindowManager.getInstance(project)
    //if (manager == designerManager) paletteManager else designerManager;

  }
}
