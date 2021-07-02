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

package cn.shanghai.oyb.compileflow.window.palette.layout

import cn.shanghai.oyb.compileflow.window.palette.{CompileFlowPaletteGroupComponent, CompileFlowPaletteItemsComponent}

import java.awt.{Component, Container, Dimension, LayoutManager}

/**
 * @author ouyubin
 */
class CompileFlowPaletteContainerLayout extends LayoutManager {

  def layoutContainer(parent: Container) {
    val width: Int = parent.getWidth
    var height: Int = 0
    for (component <- parent.getComponents) {
      if (component.isInstanceOf[CompileFlowPaletteGroupComponent]) {
        val groupComponent: CompileFlowPaletteGroupComponent = component.asInstanceOf[CompileFlowPaletteGroupComponent]
        groupComponent.setLocation(0, height)
        if (groupComponent.isVisible) {
          val groupHeight: Int = groupComponent.getPreferredSize.height
          groupComponent.setSize(width, groupHeight)
          height += groupHeight
        }
        else {
          groupComponent.setSize(0, 0)
        }
        if (groupComponent.isSelected || !groupComponent.isVisible) {
          val itemsComponent: CompileFlowPaletteItemsComponent = groupComponent.getItemsComponent
          val itemsHeight: Int = itemsComponent.getPreferredSize.height
          itemsComponent.setBounds(0, height, width, itemsHeight)
          height += itemsHeight
        }
      }
    }
  }

  def preferredLayoutSize(parent: Container): Dimension = {
    val width: Int = parent.getWidth
    var height: Int = 0
    for (component <- parent.getComponents) {
      if (component.isInstanceOf[CompileFlowPaletteGroupComponent]) {
        val groupComponent: CompileFlowPaletteGroupComponent = component.asInstanceOf[CompileFlowPaletteGroupComponent]
        height += groupComponent.getHeight
        if (groupComponent.isSelected) {
          height += groupComponent.getItemsComponent.getPreferredHeight(width)
        }
      }
    }
    return new Dimension(10, height)
  }

  def minimumLayoutSize(parent: Container): Dimension = {
    return new Dimension
  }

  def addLayoutComponent(name: String, comp: Component) {
  }

  def removeLayoutComponent(comp: Component) {
  }

}
