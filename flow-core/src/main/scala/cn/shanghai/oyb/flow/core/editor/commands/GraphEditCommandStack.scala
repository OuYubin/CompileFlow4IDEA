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

import cn.shanghai.oyb.flow.core.editor.commands.listeners.TCommandStackListener

import java.util
import java.util.EventObject
import com.intellij.openapi.command.{CommandProcessor, UndoConfirmationPolicy, WriteCommandAction}
import com.intellij.openapi.diagnostic.Logger

import scala.beans.BeanProperty
import scala.collection.JavaConversions._


/**
 * 命令栈对象,用于执行命令请求的对象
 *
 * @author ouyubin
 */
class GraphEditCommandStack {

  val LOG: Logger = Logger.getInstance(classOf[GraphEditCommandStack])

  var listeners = new util.ArrayList[TCommandStackListener]

  /**
   * 撤销栈
   */
  @BeanProperty var undoCommandStack: util.Stack[TGraphEditCommand] = new util.Stack[TGraphEditCommand]

  /**
   * 重做栈
   */
  @BeanProperty var redoCommandStack: util.Stack[TGraphEditCommand] = new util.Stack[TGraphEditCommand]

  val commandProcessor = CommandProcessor.getInstance()

  val myCommandProcessor = CommandProcessor.getInstance()


  def addListener(listener: TCommandStackListener): Boolean = {
    listeners.add(listener)
  }


  def notifyListeners(command: TGraphEditCommand): Unit = {
    val eventObject = new EventObject(command)
    for (commandStackListener <- listeners) {
      commandStackListener.commandStackChanged(eventObject)
    }
  }

  /**
   *
   * 触发写操作,该动作会在触发宿主命令框架下执行对应动作,所以无需将该动作封装入命令处理器中执行
   * 注意:groupId需要保持唯一
   *
   * @see WriteCommandAction.doExecuteCommand(runnable: Runnable)
   *
   * */

  def execute(command: TGraphEditCommand): Unit = {
    val project = command.getProject
    //LOG.info("\uD83D\uDE33在宿主命令框架下执行命令...")
    myCommandProcessor.executeCommand(project, () => {
      if (command.canExecute) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable {
          override def run(): Unit = {
            if (command != null) {
              command.execute
              //--执行成功后将当前任务压入撤销栈
              undoCommandStack.push(command)
              LOG.info("💣压入撤销栈 => 命令名称: " + command.getName + " 当前撤销栈撤销个数: " + undoCommandStack.size())
              LOG.info(String.format("↩️当前撤销栈撤销命令个数=> %s", undoCommandStack.size().toString))
              LOG.info(String.format("↪️当前重做栈重做命令个数=> %s", redoCommandStack.size().toString))
              //--触发监听
              notifyListeners(command)
            }
          }
        })
      }
    }, command.getName, null, UndoConfirmationPolicy.DEFAULT)
  }

  def canRedo: Boolean = {
    if (redoCommandStack.size == 0)
      false
    else
    //redoable.peek.asInstanceOf[Nothing].canRedo
      true
  }

  def canUndo: Boolean = {
    if (undoCommandStack.size == 0)
      false
    //undoable.peek.asInstanceOf[Nothing].canUndo
    else
      true
  }


  /**
   *
   * 撤销
   *
   */
  def undo(): Unit = {
    if (canUndo) {
      val undoCommand = undoCommandStack.pop()
      LOG.info("💣弹出撤销栈 => 命令名称: " + undoCommand.getName)
      val project = undoCommand.getProject
      //LOG.info("\uD83D\uDE33在宿主命令框架下执行撤销命令...")
      myCommandProcessor.executeCommand(project, () => {
        //        if (command.canExecute) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable {
          override def run(): Unit = {
            if (undoCommand != null) {
              undoCommand.undo
              redoCommandStack.push(undoCommand)
              LOG.info("💣压入重做栈 => 命令名称: " + undoCommand.getName)
              LOG.info(String.format("↩️当前撤销栈撤销命令个数=> %s", undoCommandStack.size().toString))
              LOG.info(String.format("↪️当前重做栈重做命令个数=> %s", redoCommandStack.size().toString))
              //--触发监听
              notifyListeners(undoCommand)
            }
          }
        })
        //        }
      }, undoCommand.getName, null, UndoConfirmationPolicy.DEFAULT)
    }
  }


  /**
   * 重做
   *
   * @return
   */
  def redo(): Unit = {
    if (canRedo) {
      val redoCommand = redoCommandStack.pop()
      LOG.info("💣弹出重做栈 => 命令名称: " + redoCommand.getName)
      val project = redoCommand.getProject
      //LOG.info("\uD83D\uDE33在宿主命令框架下执行重做命令...")
      myCommandProcessor.executeCommand(project, () => {
        //        if (command.canExecute) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable {
          override def run(): Unit = {
            if (redoCommand != null) {
              redoCommand.redo
              undoCommandStack.push(redoCommand)
              LOG.info("💣压入撤销栈 => 命令名称: " + redoCommand.getName)
              LOG.info(String.format("↩️当前撤销栈撤销命令个数=> %s", undoCommandStack.size().toString))
              LOG.info(String.format("↪️当前重做栈重做命令个数=> %s", redoCommandStack.size().toString))
              //--触发监听
              notifyListeners(redoCommand)
            }
          }
        })
        //        }
      }, redoCommand.getName, null, UndoConfirmationPolicy.DEFAULT)

    }
  }

}
