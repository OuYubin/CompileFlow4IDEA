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

import cn.shanghai.oyb.compileflow.window.palette.layout.CompileFlowPaletteContainerLayout

import java.awt._
import javax.swing.{JPanel, Scrollable}

/**
  * @author ouyubin
  */
class CompileFlowPaletteContainer extends JPanel(new CompileFlowPaletteContainerLayout()) with Scrollable {

  setBorder(null)

  def getPreferredScrollableViewportSize: Dimension = {
    getPreferredSize
  }


  def getScrollableUnitIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
    20
  }


  def getScrollableBlockIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int = {
    100
  }


  def getScrollableTracksViewportWidth: Boolean = {
    true
  }


  def getScrollableTracksViewportHeight: Boolean = {
    false
  }


}
