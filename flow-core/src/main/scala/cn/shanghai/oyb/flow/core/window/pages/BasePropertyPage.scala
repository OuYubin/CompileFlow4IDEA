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

package cn.shanghai.oyb.flow.core.window.pages

import java.awt.Font

import com.intellij.util.ui.{JBUI, UIUtil}
import javax.swing.{JLabel, SwingConstants}


/**
  *
  * 基本组件属性编辑页
  *
  * @author ouyubin
  *
  */
abstract class BasePropertyPage extends TPropertyPage {

  val DEFAULT_PROPERTY_PAGE_COLUMN_SPEC: String = "5px"

  val DEFAULT_PROPERTY_PAGE_ROW_SPEC: String = "5px"

  val DEFAULT_PROPERTY_PAGE_TITLE_ROW_TOP_MARGIN: String = "10px"

  val DEFAULT_PROPERTY_PAGE_TITLE_ROW_SPEC: String = "9px"

  val DEFAULT_PROPERTY_PAGE_COLUMN_PADDING: String = "6px"

  val DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN: String = "10px"


  def createSeparatorLabel(name: String): JLabel = {
    val separatorLabel = new JLabel(name, SwingConstants.LEFT)
    separatorLabel.setFont(separatorLabel.getFont.deriveFont(Font.BOLD, UIUtil.getFontSize(UIUtil.FontSize.NORMAL)))
    separatorLabel
  }

}
