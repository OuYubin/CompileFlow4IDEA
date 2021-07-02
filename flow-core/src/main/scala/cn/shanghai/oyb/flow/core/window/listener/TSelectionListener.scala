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

package cn.shanghai.oyb.flow.core.window.listener

import cn.shanghai.oyb.flow.core.window.provider.TSelectionListenerProvider

import java.util.EventListener


/**
 * 通用选择监听器,
 * <p>
 * 主要用途:主要用于处理附属调色面板动作后,当前编辑器需要做出对应动作,比如指针变化等,
 * 如当前图形需要建立图元间连线时,调色面板作为选择提供器需要将选择项推送器编辑器,编辑器做出对应变化
 *
 * @author ouyubin
 */

trait TSelectionListener extends EventListener {

    def selectionChanged(selectionListenerProvider:TSelectionListenerProvider )
}
