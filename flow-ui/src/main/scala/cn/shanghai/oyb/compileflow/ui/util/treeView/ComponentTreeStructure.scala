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

package cn.shanghai.oyb.compileflow.ui.util.treeView

import com.intellij.ide.util.treeView.{AbstractTreeStructure, NodeDescriptor}


/**
  *
  * 树型构造对象
  *
  * @author ouyubin
  */


class ComponentTreeStructure extends AbstractTreeStructure {

  var treeRoot: AnyRef = new Object

  override def getRootElement: AnyRef = {
    treeRoot
  }

  override def getParentElement(element: scala.Any): AnyRef = {
    //    if (element.isInstanceOf[RadComponent]) {
    //      val component = element.asInstanceOf[RadComponent]
    //      return component.getParent()
    //    }
    return null
  }

  override def getChildElements(element: scala.Any): Array[AnyRef] = {
    //        if (element == treeRoot) {
    //          return designer.getTreeRoots
    //        }
    //        if (element.isInstanceOf[RadComponent]) {
    //          val component = element.asInstanceOf[RadComponent]
    //          return component.getTreeChildren()
    //        }
    throw new IllegalArgumentException("Unknown element: " + element)
  }

  override def createDescriptor(element: scala.Any, parentDescriptor: NodeDescriptor[_]): NodeDescriptor[_] = {
    //    if (element == treeRoot || element.isInstanceOf[RadComponent]) {
    //      val descriptor: TreeNodeDescriptor = new TreeNodeDescriptor(parentDescriptor, element)
    //      descriptor.setWasDeclaredAlwaysLeaf(isAlwaysLeaf(element))
    //      return descriptor
    //super.createDescriptor(element, parentDescriptor)
    return null
  }


  //   throw new IllegalArgumentException("Unknown element: " + element)
  // }

  override def hasSomethingToCommit: Boolean = {
    false
  }

  override def commit(): Unit = ???
}