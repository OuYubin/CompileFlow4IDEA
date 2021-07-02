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

package cn.shanghai.oyb.flow.core.editor.commands

/**
  * @author ouyubin
  */
object GraphEditCommandEnum extends Enumeration {

  val FLOW_CONSTRAINT=Value(GraphEditCommand.CONSTRAINT_GRAPH_COMMAND_NAME)

  val FLOW_ELEMENT_REMOVE=Value(GraphEditCommand.REMOVE_GRAPH_COMMAND_NAME)

  val FLOW_ELEMENT_ADD=Value(GraphEditCommand.ADD_GRAPH_COMMAND_NAME)

  val FLOW_ELEMENT_PASTE=Value(GraphEditCommand.PASTE_GRAPH_COMMAND_NAME)

}
