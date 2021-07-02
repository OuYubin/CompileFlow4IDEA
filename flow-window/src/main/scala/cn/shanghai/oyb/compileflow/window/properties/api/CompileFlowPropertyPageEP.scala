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

package cn.shanghai.oyb.compileflow.window.properties.api

import cn.shanghai.oyb.flow.core.window.pages.TPropertyPage
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.{AbstractExtensionPointBean, ExtensionPointName}
import com.intellij.util.xmlb.annotations.Attribute

import scala.beans.BeanProperty

/**
  * 流程属性编辑器页自定义扩展点
  *
  * @author ouyubin
  */
class CompileFlowPropertyPageEP extends AbstractExtensionPointBean {

  @Attribute("id")
  @BeanProperty var id: String = _

  @Attribute("implementClass")
  var implementClass: String = _

  /**
   * 属性页
   */
  @BeanProperty var propertyPage: TPropertyPage = _

  /**
   * 映射节点名称
   */
  @Attribute("mappingName")
  @BeanProperty var mappingName: String = _

  /**
   * 持有父节点
   */
  @Attribute("ownerName")
  @BeanProperty var ownerName: String = _


  def createFlowPropertyPage: TPropertyPage = {
    instantiate(implementClass, ApplicationManager.getApplication.getPicoContainer)
  }
}

object CompileFlowPropertyPageEP {
  val COMPILE_FLOW_PROPERTY_PAGE_EP_NAME: ExtensionPointName[_] = ExtensionPointName.create("cn.shanghai.oyb.compileflow.propertyPage")
}
