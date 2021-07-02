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

package cn.shanghai.oyb.flow.core.editor.editpart

import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.flow.core.internal.{TAdaptable, TAdapter, TNotifier}
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement
import cn.shanghai.oyb.jgraphx.geometry.Geometry

import java.util
import scala.beans.BeanProperty

/**
  * 编辑部件图形,作为主题(提醒者TNotifier)需要向绑定的观察者(适配者TAdapter)列表发出变化通知
  *
  * @author ouyubin
  */
trait TGraphEditPart extends TNotifier {


  @BeanProperty var isConnectMode = false
  /**
    * 子编辑部件集合
    */
  @BeanProperty var childrenEditPart: java.util.List[TGraphEditPart] = new util.ArrayList[TGraphEditPart]

  /**
    * 父编辑部件
    */
  @BeanProperty var parentEditPart: TGraphEditPart = _

  /**
    * 观察者列表
    */
  @BeanProperty var adapters = new util.ArrayList[TAdapter]

  /**
    * 子模型列表
    */
  @BeanProperty val childrenModel:java.util.List[XmlTagModelElement]=new util.ArrayList[XmlTagModelElement]

  /**
    * 定义jGraphx图形单元
    */
  @BeanProperty var myCell: BaseCell = _

  /**
    * 创建组件图元
    */
  def createCell(geometry: Geometry): BaseCell

  /**
    * 获取图形几何信息(包括坐标,大小)
    */
  def getGeometry: Geometry

  /**
    * 获取绑定模型
    */
  def getModel: Any


  def addChild(componentChild: TGraphEditPart, index: Int): Unit = {
    childrenEditPart.add(index, componentChild)
  }

  def addChild(childEditPart: TGraphEditPart): Unit = {
    childEditPart.setParentEditPart(this)
    if (childrenEditPart == null) {
      childrenEditPart = new util.ArrayList[TGraphEditPart]()
    }
    childrenEditPart.add(childEditPart)
    addChildCell(childEditPart)
    childEditPart.refreshVisual()
  }

  def removeChild(componentChild: TGraphEditPart): Unit = {
    childrenEditPart.remove(componentChild)
    componentChild.refreshVisual()
    removeChildCell(componentChild)
  }

  def addChildModel(lftXmlTagModelElement: XmlTagModelElement): Unit ={
    childrenModel.add(lftXmlTagModelElement)
  }


  def getChildAt(componentChild: TGraphEditPart): Int = {
    childrenEditPart.indexOf(componentChild)
  }

  def addSourceConnection()

  def addTargetConnection()

  def removeSourceConnection()

  def removeTargetConnection()

  /**
    * 刷新自身部件
    */
  def refreshVisual()

  /**
    * 刷新子部件
    */
  def refreshChildren()

  def addChildCell(part: TGraphEditPart)

  def removeChildCell(part: TGraphEditPart)

  /**
    * 创建缩略子编辑单元
    */
  def createThumbnailChild()

  def getRootEditPart(): TGraphEditPart

  /**
    * 控制子几何图形布局约束
    *
    */
  def setLayoutConstraint(componentEditPart: TGraphEditPart): Unit

  def getAdapter(): TAdaptable
}
