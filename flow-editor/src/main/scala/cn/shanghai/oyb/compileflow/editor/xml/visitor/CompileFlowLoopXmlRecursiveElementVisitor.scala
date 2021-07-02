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

package cn.shanghai.oyb.compileflow.editor.xml.visitor

import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement
import cn.shanghai.oyb.flow.core.psi.xml.TXmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlTag

import scala.beans.BeanProperty

/**
 * Loop XML节点递归元素访问器
 *
 * @author ouyubin
 */
class CompileFlowLoopXmlRecursiveElementVisitor(var model: XmlTagModelElement) extends {
  override val element = model
} with TXmlRecursiveElementVisitor {


  @BeanProperty var tagNames: Array[String] = Array(CompileFlowXmlTagConstants.DECISION_TAG_NAME, CompileFlowXmlTagConstants.SUB_PROCESS_TAG_NAME, CompileFlowXmlTagConstants.AUTO_TASK_TAG_NAME, CompileFlowXmlTagConstants.NOTE_TAG_NAME, CompileFlowXmlTagConstants.CONTINUE_TAG_NAME, CompileFlowXmlTagConstants.BREAK_TAG_NAME, CompileFlowXmlTagConstants.SCRIPT_TAG_NAME, CompileFlowXmlTagConstants.SUB_BPM_TAG_NAME)


  /**
   * 搜索有效节点信息
   *
   * @param xmlTag
   */
  override def visitXmlTag(xmlTag: XmlTag): Unit = {
    val name = xmlTag.getName
    if (getTagNames.contains(name)) {
      xmlTags.add(xmlTag)
    }
    if (xmlTag.getParentTag == element.getXmlTag) {
      return
    }
    super.visitXmlTag(xmlTag)
  }

}
