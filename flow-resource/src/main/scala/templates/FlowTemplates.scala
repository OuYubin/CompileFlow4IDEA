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

package templates

import java.io.{File, FileOutputStream}

import com.intellij.openapi.diagnostic.Logger
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils


/**
  * @author ouyubin
  */
object FlowTemplates {

//  val standardProjectTemplateFile = new ComponentTemplates().load("/templates/standard.zip")
//
//  val extensionProjectTemplateFile = new ComponentTemplates().load("/templates/extension.zip")
//
//  val configFile = new ComponentTemplates().load("/templates/config.zip")

}

class FlowTemplates {

  private val LOG: Logger = Logger.getInstance("#templates.ComponentTemplates")

  def load(path: String): File = {
    LOG.info("归档路径: " + path)
    val resourceURL = classOf[FlowTemplates].getResource(path)
    LOG.info("归档资源: " + resourceURL.toString)
    if (StringUtils.equals(resourceURL.getProtocol, "jar")) {
      LOG.info("在归档文件中搜索模板归档文件...")
      val inputStream = classOf[FlowTemplates].getClassLoader.getResourceAsStream(path)
      val tmpFile = File.createTempFile("temp", ".zip")
      val out = new FileOutputStream(tmpFile)
      IOUtils.copy(inputStream, out)
      LOG.info("生成临时归档文件在: " + tmpFile.getCanonicalFile)
      tmpFile
    } else {
      new File(resourceURL.toURI.getPath)
    }
  }


}