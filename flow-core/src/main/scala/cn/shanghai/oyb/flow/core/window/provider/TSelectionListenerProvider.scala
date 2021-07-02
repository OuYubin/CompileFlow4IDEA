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

package cn.shanghai.oyb.flow.core.window.provider

import cn.shanghai.oyb.flow.core.window.listener.TSelectionListener


/**
 *
 * 选择监听提供器
 *
 * @author ouyubin
 */
trait TSelectionListenerProvider {

  /**
   * 加入监听对象
   *
   */
  def addSelectionListener(selectionListener: TSelectionListener)

  /**
   * 获取选择对象
   *
   */
  def getSelection: Object

  /**
   * 重置选择
   */
  def resetSelection(): Unit={

  }
}
