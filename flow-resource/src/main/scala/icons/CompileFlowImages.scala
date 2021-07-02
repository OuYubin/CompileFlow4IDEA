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

package icons

import javax.swing.ImageIcon

/**
  *
  * @author ouyubin
  *
  *
  */
class CompileFlowImages {
  def load(path: String): ImageIcon = {
    val imageURL = classOf[CompileFlowImages].getResource(path)
    new ImageIcon(imageURL)
  }
}

object CompileFlowImages {

  val WORK_FLOW_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/compileflow.png")

  val START_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/start.png")

  val APPROVE_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/approve.gif")

  val END_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/end.png")

  //val CONNECTION_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/connection.gif")

  val DECISION_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/decision.png")

  val PROPERTY_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/property.png")

  val TRANSITION_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/transition.gif")

  val PROCESS_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/process.png")

//  val TASK_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/task.svg")

//  val WAIT_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/wait.gif")

//  val SUB_PROCESS_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/sub_process.png")

  val AUTO_TASK_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/task.svg")

//  val SERVICE_CALL_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/service_call.gif")

  val METHOD_IMAGE:ImageIcon=new CompileFlowImages().load("/icons/method.png")

  val NOTE_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/note.png")

  val CONTINUE_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/continue.png")

  val BREAK_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/break.svg")

  val CLASS_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/class.png")

  val SPRING_BEAN_IMAGE:ImageIcon = new CompileFlowImages().load("/icons/spring_bean.png")

  val BAN_IMAGE:ImageIcon = new CompileFlowImages().load("/icons/ban.svg")

  val CONNECTOR_IMAGE:ImageIcon = new CompileFlowImages().load("/icons/connector.svg")

  val SCRIPT_IMAGE:ImageIcon = new CompileFlowImages().load("/icons/scripttask.png")

  val SUB_BPM_IMAGE:ImageIcon = new CompileFlowImages().load("/icons/subbpm.png")

  val UI_PALETTE_CLOSE_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/palette_close.png")

  val UI_PALETTE_OPEN_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/palette_open.png")

  val LOOP_PROCESS_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/loopprocess.svg")

  val OTHERS_IMAGE: ImageIcon = new CompileFlowImages().load("/icons/others.png")
}