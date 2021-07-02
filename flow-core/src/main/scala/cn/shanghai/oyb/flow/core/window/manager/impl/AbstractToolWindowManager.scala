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

package cn.shanghai.oyb.flow.core.window.manager.impl

import cn.shanghai.oyb.flow.core.editor.TGraphFileEditor
import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import cn.shanghai.oyb.flow.core.window.TToolWindowContent
import cn.shanghai.oyb.flow.core.window.impl.BaseToolWindow
import cn.shanghai.oyb.flow.core.window.manager.TToolWindowManager
import cn.shanghai.oyb.flow.core.window.manager.actions.EditorToggleAction
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.{ActionGroup, AnAction, DefaultActionGroup}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.{FileEditor, FileEditorManager, FileEditorManagerEvent, FileEditorManagerListener}
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ex.ToolWindowEx
import com.intellij.openapi.wm.{ToolWindow, ToolWindowAnchor}
import com.intellij.util.ParameterizedRunnable
import com.intellij.util.messages.MessageBusConnection
import com.intellij.util.ui.update.{MergingUpdateQueue, Update}

import javax.swing.{Icon, JComponent}
import org.jetbrains.annotations.Nullable

/**
  *
  * 子窗口管理器抽象基类,利用scala改写自AbstractToolWindowManager
  *
  */
abstract class AbstractToolWindowManager(val project: Project, val fileEditorManager: FileEditorManager) extends TToolWindowManager {

  final val EDITOR_MODE: String = "FLOW_EDITOR_MODE."
  val myWindowQueue: MergingUpdateQueue = new MergingUpdateQueue(getComponentName, 200, true, null)
  @volatile
  var myToolWindowReady: Boolean = false
  @volatile
  var myToolWindowDisposed: Boolean = false
  private var leftEditorModeAction: EditorToggleAction = null
  private var rightEditorModeAction: EditorToggleAction = null
  private var myConnection: MessageBusConnection = null

  val myPropertiesComponent: PropertiesComponent = PropertiesComponent.getInstance(project)
  val myEditorModeKey: String = EDITOR_MODE + getComponentName + ".STATE"


  private final val myListener: FileEditorManagerListener = new FileEditorManagerListener {
    override def fileOpened(source: FileEditorManager, file: VirtualFile) {
      bindToEditPanel(getActiveEditPanel)
    }

    override def fileClosed(source: FileEditorManager, file: VirtualFile) {
      ApplicationManager.getApplication.invokeLater(new Runnable {
        def run {
          bindToEditPanel(getActiveEditPanel)
        }
      })
    }

    override def selectionChanged(event: FileEditorManagerEvent) {
      bindToEditPanel(getEditPanel(event.getNewEditor))
    }
  }


  override def projectOpened {
    initToolWindow
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable {
      def run {
        myToolWindowReady = true
        //if (getEditorMode == null) {
          initListeners
          bindToEditPanel(getActiveEditPanel)
        //}
      }
    })
  }

  override def projectClosed {
    if (!myToolWindowDisposed) {
      disposeComponent
      myToolWindowDisposed = true
      toolWindow = null
    }
  }

  private def initListeners {
    myConnection = project.getMessageBus.connect(project)
    myConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, myListener)
  }

  private def removeListeners {
    myConnection.disconnect
    myConnection = null
  }


  /**
    * 获取当前编辑器中的编辑面板
    *
    * @param editor
    * @return
    */
  @Nullable private def getEditPanel(editor: FileEditor): TGraphicEditPanel = {
    if (editor.isInstanceOf[TGraphFileEditor]) {
      val designerEditor: TGraphFileEditor = editor.asInstanceOf[TGraphFileEditor]
      return designerEditor.getGraphicEditorPanel
    }
    return null
  }

  /**
    * 获取当前激活的设计器
    *
    * @return
    */
  @Nullable def getActiveEditPanel: TGraphicEditPanel = {
    for (editor <- fileEditorManager.getSelectedEditors) {
      val designer: TGraphicEditPanel = getEditPanel(editor)
      if (designer != null) {
        return designer
      }
    }
    return null
  }

//  @Nullable protected def getCustomizations: DesignerCustomizations = {
//    DesignerCustomizations.EP_NAME.findExtension(classOf[DesignerCustomizations])
//  }

  def bindToEditPanel(editPanel: TGraphicEditPanel) {
    myWindowQueue.cancelAllUpdates
    myWindowQueue.queue(new Update(("update")) {
      def run {
//        if (!myToolWindowReady || myToolWindowDisposed) {
//          return
//        }
        if (toolWindow == null) {
          if (editPanel == null) {
            return
          }
          initToolWindow
        }
        updateToolWindow(editPanel)
      }
    })
  }

  protected def initToolWindow

  protected def updateToolWindow(@Nullable designer: TGraphicEditPanel)

  protected final def initGearActions {
//    val toolWindow: ToolWindowEx = toolWindow.asInstanceOf[ToolWindowEx]
//    toolWindow.setAdditionalGearActions(new DefaultActionGroup(createGearActions))
  }

  protected def getAnchor: ToolWindowAnchor

  override def initComponent {
  }

  override def disposeComponent {
  }

  /**
    * 嵌入到编辑器当中
    *
    * @return
    */
  final def createGearActions: ActionGroup = {
    val group: DefaultActionGroup = new DefaultActionGroup("In Editor Mode", true)
    if (leftEditorModeAction == null) {
      leftEditorModeAction = createComponentEditorLeftToggleAction
    }
    group.add(leftEditorModeAction)
    if (rightEditorModeAction == null) {
      rightEditorModeAction = createComponentEditorRightToggleAction
    }
    group.add(rightEditorModeAction)
    return group
  }

  def createComponentEditorLeftToggleAction: EditorToggleAction

  def createComponentEditorRightToggleAction: EditorToggleAction


  final def bind(editorPanel: TGraphicEditPanel) {
    if (isEditorMode) {
      createAction.run(editorPanel)
    }
  }

  final def dispose(editorPanel: TGraphicEditPanel) {
    if (isEditorMode) {
      disposeToolWindow(editorPanel)
    }
  }

  protected final def getContent(designer: TGraphicEditPanel): AnyRef = {
    val toolWindow: BaseToolWindow = designer.asInstanceOf[JComponent].getClientProperty(getComponentName).asInstanceOf[BaseToolWindow]
    return toolWindow.getWindowContent
  }

  def createToolWindow(designer: TGraphicEditPanel): BaseToolWindow


  protected final def createToolWindow(designer: TGraphicEditPanel, content: TToolWindowContent, title: String, icon: Icon, component: JComponent, focusedComponent: JComponent, defaultWidth: Int, actions: Array[AnAction]): BaseToolWindow = {
    new BaseToolWindow(content, title, icon, component, focusedComponent, designer.getContentSplitter, getEditorMode, this, project, myPropertiesComponent, getComponentName, defaultWidth, actions)
  }

  protected final def disposeToolWindow(designer: TGraphicEditPanel) {
    val key: String = getComponentName
    val toolWindow: BaseToolWindow = designer.asInstanceOf[JComponent].getClientProperty(key).asInstanceOf[BaseToolWindow]
    designer.asInstanceOf[JComponent].putClientProperty(key, null)
    toolWindow.dispose
  }

  /**
    * 编辑器开启时进行绑定动作时进行触发
    * 将编辑面板名称属性名称与内容进行绑定
    */
  private final val createAction: ParameterizedRunnable[TGraphicEditPanel] = new ParameterizedRunnable[TGraphicEditPanel] {
    def run(designer: TGraphicEditPanel) {
      designer.asInstanceOf[JComponent].putClientProperty(getComponentName, createToolWindow(designer))
    }
  }
  private final val myUpdateAnchorAction: ParameterizedRunnable[TGraphicEditPanel] = new ParameterizedRunnable[TGraphicEditPanel] {
    def run(designer: TGraphicEditPanel) {
      val toolWindow: BaseToolWindow = designer.asInstanceOf[JComponent].getClientProperty(getComponentName).asInstanceOf[BaseToolWindow]
      toolWindow.updateAnchor(getEditorMode)
    }
  }
  private final val myDisposeAction: ParameterizedRunnable[TGraphicEditPanel] = new ParameterizedRunnable[TGraphicEditPanel] {
    def run(designer: TGraphicEditPanel) {
      disposeToolWindow(designer)
    }
  }

  private def runUpdateContent(action: ParameterizedRunnable[TGraphicEditPanel]) {
    for (editor <- fileEditorManager.getAllEditors) {
      val designer: TGraphicEditPanel = getEditPanel(editor)
      if (designer != null) {
        action.run(designer)
      }
    }
  }

  protected final def isEditorMode: Boolean = {
    return getEditorMode != null
  }

  def getEditorMode: ToolWindowAnchor = {
    val value: String = myPropertiesComponent.getValue(myEditorModeKey)
    if (value == null) {
      return getAnchor
    }
    return if ((value == "ToolWindow")) null else ToolWindowAnchor.fromText(value)
  }

  def setEditorMode(newState: ToolWindowAnchor) {
    val oldState: ToolWindowAnchor = getEditorMode
    myPropertiesComponent.setValue(myEditorModeKey, if (newState == null) "ToolWindow" else newState.toString)
    if (oldState != null && newState != null) {
      runUpdateContent(myUpdateAnchorAction)
    }
    else if (newState != null) {
      removeListeners
      updateToolWindow(null)
      runUpdateContent(createAction)
    }
    else {
      runUpdateContent(myDisposeAction)
      initListeners
      bindToEditPanel(getActiveEditPanel)
    }
  }

//  final def getToolWindow: ToolWindow = {
//    return toolWindow
//  }
}
