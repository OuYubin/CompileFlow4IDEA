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
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.Gray
import com.intellij.util.ui.UIUtil
import icons.CompileFlowImages

import java.awt._
import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import javax.swing._

/**
 * @author ouyubin
 */
class CompileFlowPaletteGroupComponent(group: CompileFlowPaletteGroup) extends JCheckBox {
  private var myItemsComponent: CompileFlowPaletteItemsComponent = null

  setText(group.getName)
  setSelected(true)
  setIcon(AllIcons.Nodes.Desktop)
  setSelectedIcon(AllIcons.Modules.SourceRoot)
  setFont(getFont.deriveFont(Font.BOLD))
  setFocusPainted(false)
  setMargin(new Insets(0, 3, 0, 3))
  setOpaque(true)
  addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      myItemsComponent.setVisible(isSelected)
    }
  })
  initActions

  override def getBackground: Color = {
    if (isFocusOwner) {
      return UIUtil.getListSelectionBackground
    }
    if (UIUtil.isUnderDarcula) {
      return Gray._100
    }
    super.getBackground
  }

  override def getForeground: Color = {
    if (isFocusOwner) {
      return UIUtil.getListSelectionForeground
    }
    return super.getForeground
  }

  def getItemsComponent: CompileFlowPaletteItemsComponent = {
    myItemsComponent
  }

  def setItemsComponent(itemsComponent: CompileFlowPaletteItemsComponent) {
    myItemsComponent = itemsComponent
  }

  private def initActions {
    val inputMap: InputMap = getInputMap(JComponent.WHEN_FOCUSED)
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "moveFocusDown")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "moveFocusUp")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "collapse")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "expand")
    val actionMap: ActionMap = getActionMap
    actionMap.put("moveFocusDown", new MoveFocusAction(true))
    actionMap.put("moveFocusUp", new MoveFocusAction(false))
    actionMap.put("collapse", new ExpandAction(false))
    actionMap.put("expand", new ExpandAction(true))
  }

  private class MoveFocusAction(moveDown: Boolean) extends AbstractAction {

    def actionPerformed(e: ActionEvent) {
      val kfm: KeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager
      val container: Container = kfm.getCurrentFocusCycleRoot
      var policy: FocusTraversalPolicy = container.getFocusTraversalPolicy
      if (policy == null) {
        policy = kfm.getDefaultFocusTraversalPolicy
      }
      var next: Component = if (moveDown) policy.getComponentAfter(container, CompileFlowPaletteGroupComponent.this) else policy.getComponentBefore(container, CompileFlowPaletteGroupComponent.this)
      if (next.isInstanceOf[CompileFlowPaletteItemsComponent]) {
        val list: CompileFlowPaletteItemsComponent = next.asInstanceOf[CompileFlowPaletteItemsComponent]
        if (list.getModel.getSize != 0) {
          list.takeFocusFrom(if (list eq myItemsComponent) 0 else -1)
          return
        }
        else {
          next = if (moveDown) policy.getComponentAfter(container, next) else policy.getComponentBefore(container, next)
        }
      }
      if (next.isInstanceOf[CompileFlowPaletteGroupComponent]) {
        next.requestFocus
      }
    }

    private final val myMoveDown: Boolean = false
  }

  private class ExpandAction(expand: Boolean) extends AbstractAction {

    def actionPerformed(e: ActionEvent) {
      if (expand != isSelected) {
        setSelected(expand)
        if (myItemsComponent != null) {
          myItemsComponent.setVisible(isSelected)
        }
      }
    }
  }

  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    var xInset = 2
    var yInset = 1
    if (SystemInfo.isWindows) {
      xInset = 1
    } else if (SystemInfo.isMac) {
      if (UIUtil.isUnderDarcula) {
        xInset = 2
        yInset = 2
      } else if (UIUtil.isUnderIntelliJLaF) {
        if(UIUtil.isUnderDefaultMacTheme){
          xInset = 3
          yInset = 4
        }else {
          xInset = 2
          yInset = 2
        }
      }
    }

    if (isSelected)
      g.drawImage(CompileFlowImages.UI_PALETTE_OPEN_IMAGE.getImage, xInset, yInset, this)
    else
      g.drawImage(CompileFlowImages.UI_PALETTE_CLOSE_IMAGE.getImage, xInset, yInset, this)
  }

}
