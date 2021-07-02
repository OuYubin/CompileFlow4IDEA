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

package cn.shanghai.oyb.flow.core.editor.impl

import cn.shanghai.oyb.flow.core.editor.TGraphFileEditor
import cn.shanghai.oyb.flow.core.editor.commands.{GraphEditCommandStack, TGraphEditCommand}
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory
import cn.shanghai.oyb.flow.core.window.TToolWindow
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.{FileEditorLocation, FileEditorState, FileEditorStateLevel}
import com.intellij.openapi.module.{Module, ModuleUtilCore}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import org.jetbrains.annotations.Nullable

import java.beans.PropertyChangeListener
import javax.swing.JComponent
import scala.beans.BeanProperty

/**
 * 主编辑器抽象基类
 *
 * @author ouyubin
 *
 *
 */
abstract class AbstractGraphFileEditor(val project: Project, val file: VirtualFile) extends UserDataHolderBase with TGraphFileEditor {

  val LOG: Logger = Logger.getInstance("#" + AbstractGraphFileEditor.this.getClass.getName)

  var originalFile: VirtualFile = file
  if (file.isInstanceOf[LightVirtualFile]) {
    originalFile = (file.asInstanceOf[LightVirtualFile]).getOriginalFile
  }
  val module: Module = findModule(project, originalFile)
  if (module == null) {
    throw new IllegalArgumentException("No module for file " + file + " in project " + project)
  }

  //--子类中完成自定义模型工厂构建
  val lftModelFactory = createModelFactory


  //--设计器面板,需要在子类中集中实现
  val graphicEditorPanel = createGraphicEditorPanel(project, module, file)


  //--构建图形编辑域,用于控制编辑命令管理,同时命令执行结束后触发监听控制编辑器在整个环境中的状态控制,如编辑保存,撤销,重做状态等
  @BeanProperty val editDomain = initEditDomain

  override def getGraphicEditorPanel: TGraphicEditPanel = {
    graphicEditorPanel
  }

  def initEditDomain: GraphEditDomain = {
    val commandStack = new GraphEditCommandStack
    commandStack.addListener((event) => {
      val command = event.getSource
      var effectCell: BaseCell = null
      if (command.isInstanceOf[TGraphEditCommand]) {
        effectCell = command.asInstanceOf[TGraphEditCommand].getEffectCell
      }
      val propertiesPage: TToolWindow = graphicEditorPanel.getPropertyToolWindowManager.getToolWindowContent.getPropertiesPage
      if (effectCell != null)
        propertiesPage.refresh(effectCell)
      else
        propertiesPage.refresh()
    })
    new GraphEditDomain(commandStack)
  }

  /**
   * 创建图形模型处理工厂
   *
   * @return
   */
  def createModelFactory: TModelElementFactory


  /**
   *
   * 子类需要尝试实现设计器面板
   *
   * @param project
   * @param module
   * @param file
   * @return
   */
  def createGraphicEditorPanel(project: Project, module: Module, file: VirtualFile): TGraphicEditPanel

  /**
   * 在当前编辑器中需要显示的界面组件
   *
   * @return
   */
  final def getComponent: JComponent = {
    graphicEditorPanel.asInstanceOf[JComponent]
  }

  @Nullable protected def findModule(project: Project, file: VirtualFile): Module = {
    ModuleUtilCore.findModuleForFile(file, project)
  }

  final def getPreferredFocusedComponent: JComponent = {
    null
  }

  /**
   * 保持与eclipse插件中显示一致
   *
   * @return
   */
  override def getName: String

  def dispose: Unit = {
    graphicEditorPanel.dispose()
  }


  override def selectNotify {
    graphicEditorPanel.activate
  }


  override def deselectNotify {
  }


  def isValid: Boolean = {
     true
  }


  def isModified: Boolean = {
     false
  }

  override def getState(level: FileEditorStateLevel): FileEditorState = {
     super.getState(level)
  }


  def setState(state: FileEditorState) {
  }


  def addPropertyChangeListener(listener: PropertyChangeListener) {
  }


  def removePropertyChangeListener(listener: PropertyChangeListener) {
  }


  def getCurrentLocation: FileEditorLocation = {
    null
  }


  override def getStructureViewBuilder: StructureViewBuilder = {
     null
  }

  override def getAdapter(adapter: Class[_]): Any = {
    if (adapter == classOf[GraphEditDomain]) {
      editDomain
    } else if (adapter == classOf[Project]) {
      project
    } else if (adapter == classOf[TModelElementFactory]) {
      lftModelFactory
    }
  }
}
