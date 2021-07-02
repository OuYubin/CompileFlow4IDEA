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

import cn.shanghai.oyb.flow.core.editor.editpart.factory.TGraphEditPartFactory
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell
import cn.shanghai.oyb.flow.core.geometry.BaseGeometry
import cn.shanghai.oyb.flow.core.internal.TAdaptable
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement
import cn.shanghai.oyb.flow.core.models.factory.TModelElementFactory
import cn.shanghai.oyb.flow.core.psi.xml.TXmlRecursiveElementVisitor
import cn.shanghai.oyb.jgraphx.geometry.Geometry
import cn.shanghai.oyb.jgraphx.graph.Graph
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.xml.XmlTag

import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.util
import java.util.Collections
import scala.beans.BeanProperty
import scala.collection.JavaConversions._


/**
 *
 * 抽象图形编辑部件主类
 * 将jGraphx中描述坐标及大小的geometry对象及geometry中的图元cell对象进行封装,通过接入模型生成对应信息
 *
 * @author ouyubin
 *
 */
abstract class AbstractGraphEditPart(@BeanProperty val model: Any, @BeanProperty val adapter: TAdaptable) extends TGraphEditPart with PropertyChangeListener {

  private final val LOG: Logger = Logger.getInstance(classOf[AbstractGraphEditPart])

  attachOnAdapter()

  hookInModel(model)

  //--构建几何图形,其实就是是约束区域,包括坐标,大小
  var lftGeometry: BaseGeometry = createGeometry()

  //--构建渲染单元
  myCell = createCell(lftGeometry: BaseGeometry)

  //--将自身关联至cell对象之上
  myCell.setMyEditPart(this)

  /**
   * 挂接模型到当前编辑部件
   *
   * @param model
   */
  def hookInModel(model: Any): Unit = {
    model match {
      case model: XmlTagModelElement => model.asInstanceOf[XmlTagModelElement].addPropertyChangeListener(this)
      case _ =>
    }
  }

  /**
   * 附着编辑部件到当前jGraphx图像对象上,在调用编辑图元进行刷新时调用,基本设计思想使用观察者模式完成,
   *
   * @see AbstractLFTGraphEditPart.refreshVisual()
   */
  override def attachOnAdapter(): Unit = {
    val componentMXGraph = adapter.getAdapter(classOf[Graph]).asInstanceOf[Graph]
    if (componentMXGraph != null)
      adapters.add(componentMXGraph)
  }

  /**
   * 构建组件几何信息,其实就是几何位置及约束
   */
  def createGeometry(): BaseGeometry = {
    val x = 0
    val y = 0
    val width = 0
    val height = 0
    new BaseGeometry(x, y, width, height)
  }

  /**
   * 子类必须完成覆盖
   *
   * @param geometry 组件几何图形
   * @return
   */
  override def createCell(geometry: Geometry): BaseCell = {
    null
  }


  /**
   * 获取几何信息,jGraphx在移动几何图形时会将单元内的几何图形进行clone操作,在编辑原件中获取几何信息时需要在当前单元中获取
   *
   * @see com.mxgraph.view.mxGraph.translateCell(cell: Any, dx: Double, dy: Double)
   * @return
   */
  def getGeometry: BaseGeometry = {
    this.lftGeometry = myCell.getGeometry.asInstanceOf[BaseGeometry]
    lftGeometry
  }


  /**
   * 刷新面板
   */
  override def refreshVisual(): Unit = {
    adapters.forEach(adapter => adapter.notifyChanged())
  }


  /**
   * 子控件布局约束
   *
   * @param componentEditPart 组件编辑元件
   */
  override def setLayoutConstraint(componentEditPart: TGraphEditPart): Unit = {

  }

  /**
   * 属性触发变化动作
   *
   * @param event
   */
  override def propertyChange(event: PropertyChangeEvent): Unit = {


  }

  /**
   * 仅在图形编辑过程中别调用,用于刷新新增或删除子编辑部件,在构建模型过程中严禁调用刷新子部件的动作
   */
  override def refreshChildren(): Unit = {
    model match {
      case _: XmlTagModelElement =>
        //--编辑部件集合
        val partChildren: util.List[TGraphEditPart] = this.getChildrenEditPart
        val modelToEditPart: util.Map[Any, TGraphEditPart] = new util.HashMap[Any, TGraphEditPart]()
        if (partChildren != null) {
          for (child <- partChildren) {
            val model = child.getModel
            //val xmlTag = model.asInstanceOf[XmlTagModelElement].getXmlTag
            modelToEditPart.put(model, child)
          }
        }
        val modelChildren = getModelChildren()

        modelChildren.toStream.foreach(child => {
          val editPart = modelToEditPart.get(child)
          //--不存在时构建并进行叠加
          if (editPart == null) {
            val editPart = createEditPart(child)
            if (editPart != null) {
              addChild(editPart)
              //editPart.addSourceConnection()
            }
          } else {
            editPart.refreshVisual()
          }
        })
        if (partChildren != null) {
          val size = partChildren.size()
          val i = modelChildren.size()
          //--存在删除
          if (i < size) {
            val trashEditParts = new util.ArrayList[TGraphEditPart]()
            for (child <- partChildren) {
              val count = modelChildren.stream().filter(modelChild => child.getModel.asInstanceOf[XmlTagModelElement] == modelChild).count()
              if (count == 0) {
                trashEditParts.add(child)
              }
            }
            for (editPart <- trashEditParts) {
              editPart.removeSourceConnection()
              removeChild(editPart)
            }
          }
        }

      case _ =>
    }
  }


  /**
   * 编辑部件子模型获取
   */
  def getModelChildren(): util.List[XmlTagModelElement] = {
    Collections.emptyList()
  }

  /**
   * 构建编辑部件
   *
   * @param xmlTag
   * @return
   */
  def createEditPart(modelElement: XmlTagModelElement): TGraphEditPart = {
    //    val modelElementFactory = adapter.getAdapter(classOf[TLFTModelElementFactory])
    //    val model = modelElementFactory.asInstanceOf[TLFTModelElementFactory].getModel(xmlTag)
    val xmlTag = modelElement.getXmlTag
    if (!xmlTag.isValid) {
      val xmlTags = this.getModel.asInstanceOf[XmlTagModelElement].getXmlTag.getSubTags
    }
    val editPartFactory = adapter.getAdapter(classOf[TGraphEditPartFactory])
    //val componentXmlTag = new LFTXmlTagModelElement(xmlTag)
    val editPart = editPartFactory.asInstanceOf[TGraphEditPartFactory].getGraphEditPart(modelElement)
    editPart.createThumbnailChild()
    editPart
  }

  /**
   * 增加子图形单元,针对jGraphx框架
   *
   * @param editPart
   */
  def addChildCell(editPart: TGraphEditPart): Unit = {
    val lftCell = editPart.getMyCell
    val parentLftCell = this.getMyCell
    lftCell.setVertex(true)
    parentLftCell.insert(lftCell)
    lftCell.setParent(parentLftCell)
  }

  def removeChildCell(editPart: TGraphEditPart): Unit = {
    val childCell = editPart.getMyCell
    val parentCell = this.getMyCell
    parentCell.remove(childCell)
  }

  override def addSourceConnection(): Unit = {}

  override def addTargetConnection(): Unit = {}

  override def removeSourceConnection(): Unit = {}

  override def removeTargetConnection(): Unit = {}

  def createRecursiveElementVisitor(): TXmlRecursiveElementVisitor

  override def createThumbnailChild(): Unit = {}

  def getRootEditPart(): TGraphEditPart = {
    if (getParentEditPart == null)
      null
    else
      getParentEditPart.getRootEditPart()
  }


}
