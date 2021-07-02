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

package cn.shanghai.oyb.compileflow.palette.model

import java.awt.Image
import javax.swing.Icon
import scala.beans.BeanProperty


/**
  * @author ouyubin
  */
class CompileFlowConnectionPaletteItem(@BeanProperty override val title: String, @BeanProperty val desc: String, @BeanProperty override val icon: Icon, @BeanProperty val image: Image, @BeanProperty val banImage: Image, @BeanProperty override val tooltip: String, @BeanProperty val version: String, @BeanProperty override val deprecatedVersion: String, @BeanProperty override val deprecatedHint: String) extends CompileFlowNodePaletteItem(title,title, icon, tooltip, version, deprecatedVersion, deprecatedHint,null) {
}
