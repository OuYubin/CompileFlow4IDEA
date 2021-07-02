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

package cn.shanghai.oyb.compileflow.model

import cn.shanghai.oyb.flow.core.models.{ModelMetaElement, XmlTagModelElement}
import com.intellij.psi.xml.{XmlAttribute, XmlTag}


/**
 *
 * 通用流程模型
 *
 * @author ouyubin
 */
class CompileFlowXmlTagModelElement(xmlTag: XmlTag) extends XmlTagModelElement(xmlTag) with TCompileFlowXmlTagModelElement {

  val metaAttribute: XmlAttribute = xmlTag.getAttribute("g")
  if (metaAttribute != null) {
    val meta: String = metaAttribute.getValue
    if (meta != null) {
      val metas: Array[String] = meta.split(",")
      val x: Double = if (metas.apply(0) == null) 0 else metas.apply(0).toDouble
      val y: Double = if (metas.apply(1) == null) 0 else metas.apply(1).toDouble
      var metaElement = new ModelMetaElement(x, y, 0.0D, 0.0D)
      if (metas.length == 4) {
        val width = metas.apply(2).toDouble
        val height = metas.apply(3).toDouble
        metaElement.width = width;
        metaElement.height = height
      }
      this.setMeta(metaElement)
    }
    val nameAttribute: XmlAttribute = xmlTag.getAttribute("name")
    if (nameAttribute != null) {
      val name = nameAttribute.getValue
      setName(name)
    }
  }

}
