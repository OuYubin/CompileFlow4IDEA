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

import cn.shanghai.oyb.compileflow.palette.model.CompileFlowPaletteGroup

import java.beans.PropertyChangeListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiFile, PsiManager}

/**
 * @author ouyubin
 */
abstract class CompileFlowPaletteProvider(@scala.beans.BeanProperty val project: Project)  {

  def removeListener(p1: PropertyChangeListener)

  def addListener(p1: PropertyChangeListener)

  def getActiveGroups(virtualFile: VirtualFile): Array[CompileFlowPaletteGroup] = {
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
    getActiveGroups(psiFile)
  }

  def getActiveGroups(psi: PsiFile): Array[CompileFlowPaletteGroup]

}
