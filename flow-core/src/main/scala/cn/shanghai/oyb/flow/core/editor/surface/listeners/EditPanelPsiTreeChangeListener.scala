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

package cn.shanghai.oyb.flow.core.editor.surface.listeners

import cn.shanghai.oyb.flow.core.editor.surface.TGraphicEditPanel
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.{PsiFile, PsiManager, PsiTreeChangeAdapter, PsiTreeChangeEvent}
import com.intellij.util.Alarm

import java.awt.Component
import scala.beans.BeanProperty

/**
 *
 * @author ouyubin
 * @param editPanel
 * @param file
 * @param delayMillis
 * @param runnable
 */
class EditPanelPsiTreeChangeListener(val editPanel: TGraphicEditPanel, val file: PsiFile, val delayMillis: Int, val runnable: Runnable) extends PsiTreeChangeAdapter {

  val LOG = Logger.getInstance(classOf[EditPanelPsiTreeChangeListener])

  private final val alarm: Alarm = new Alarm
  protected var myRunState: Boolean = false
  private var myInitialize: Boolean = false
  private var myContent: String = null
  protected var myUpdateRenderer: Boolean = false

  @BeanProperty var event: PsiTreeChangeEvent = null

  //--挂接PSI结构变化监听器
  PsiManager.getInstance(editPanel.getProject).addPsiTreeChangeListener(this, editPanel)

  def initialize {
    myInitialize = true
  }

  def start {
    if (!myRunState) {
      myRunState = true
    }
  }

  def dispose {
    PsiManager.getInstance(editPanel.getProject).removePsiTreeChangeListener(this)
    stop
  }

  def stop {
    if (myRunState) {
      myRunState = false
      cancel
    }
  }

  def activate {
    if (!myRunState) {
      start
      //if (!ComparatorUtil.equalsNullable(myContent, designer.getEditorText) || designer.getRootComponent == null) {
      myUpdateRenderer = false
      addRequest
      // }
      myContent = null
    }
  }

  def deactivate {
    if (myRunState) {
      stop
    }
    myUpdateRenderer = false
  }

  def addRequest {
    addRequest(runnable)
  }

  def isActive: Boolean = {
    myRunState
  }

  def isUpdateRenderer: Boolean = {
    myUpdateRenderer
  }

  def ensureUpdateRenderer: Boolean = {
    if (myRunState) {
      return myInitialize
    }
    myUpdateRenderer = true
    false
  }

  def addRequest(runnable: Runnable) {
    cancel
    alarm.addRequest(new Runnable {
      def run {
        if (myRunState && myInitialize) {
          runnable.run
        }
      }
    }, delayMillis, ModalityState.stateForComponent(editPanel.asInstanceOf[Component]))
  }


  def cancel {
    alarm.cancelAllRequests
  }

  /**
   * 过滤PSI变换事件
   *
   * @param event
   * @return
   */
  def filter(event: PsiTreeChangeEvent): Boolean = {
    if (event == null) {
      return true
    }
    val parent = event.getParent
    val child = event.getChild
    if (parent != null && child != null) {
      if (parent.isInstanceOf[XmlTag] && child.isInstanceOf[XmlTag]) {
        this.event = event
        return true
      }
    }
    false
  }

  protected def updatePsi(event: PsiTreeChangeEvent) {
    //--守护是当前文件PSI结构发生变化
    if (myRunState && file == event.getFile && filter(event)) {
      addRequest
    }
  }

  override def childAdded(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childRemoved(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childReplaced(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childMoved(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def childrenChanged(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }

  override def propertyChanged(event: PsiTreeChangeEvent) {
    updatePsi(event)
  }
}
