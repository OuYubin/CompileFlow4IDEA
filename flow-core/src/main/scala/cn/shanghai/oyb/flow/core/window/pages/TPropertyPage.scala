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

import cn.shanghai.oyb.flow.core.internal.TAdaptable

import javax.swing.{JComponent, JPanel}
import scala.beans.BeanProperty

/**
  *
  * 属性特征页
  *
  * @author ouyubin
  *
  */
trait TPropertyPage extends JPanel {

  @BeanProperty var myInput: Any = _

  @BeanProperty var adapter: TAdaptable = _



  /**
    * 构建页
    *
    * @param parent
    */
  def createControl(parent: JComponent)


  def setInput(adapter: TAdaptable, input: Any): Unit = {
    this.setInput(input)
    this.adapter = adapter
  }

  /**
    * 注入模型对象
    *
    * @param input
    */
  def setInput(input: Any): Unit = {
    this.myInput = input
  }


  /**
    * 刷新页
    */
  def refresh()
}
