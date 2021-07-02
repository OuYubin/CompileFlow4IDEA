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

package cn.shanghai.oyb.compileflow.common.model.query.utils

import java.util
import java.util.stream.Collectors

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import org.apache.commons.lang.StringUtils

/**
  *
  * 组件PSI元素搜索工具对象
  *
  * @author ouyubin
  */
object PsiElementQueryUtil {

  def getPsiElementByTagName(owner: PsiElement, tagName: String): java.util.List[PsiElement] = {
    util.Arrays.stream(owner.asInstanceOf[XmlTag].getChildren).filter((element: PsiElement) => element.isInstanceOf[XmlTag] && StringUtils.equals(element.asInstanceOf[XmlTag].getName, tagName)).collect(Collectors.toList[PsiElement])

  }

}
