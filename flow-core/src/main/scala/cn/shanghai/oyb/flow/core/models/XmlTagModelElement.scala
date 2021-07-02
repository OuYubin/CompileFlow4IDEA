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

package cn.shanghai.oyb.flow.core.models

import com.intellij.psi.xml.XmlTag

import java.beans.{PropertyChangeListener, PropertyChangeSupport}
import scala.beans.BeanProperty

/**
 *
 * 封装PsiElement对象
 *
 * 2020年01月19在处理撤销重做时发现必须扩充加强当前模型,以便有效控制当前模型动作
 * 直接通过XmlTag处理受限过多,比如XmlTag在读写时一定要在Save Action动作,自定义动作是无法完成有效操作的
 *
 * @author ouyubin
 *
 */
class XmlTagModelElement(@BeanProperty var xmlTag: XmlTag) extends TModelElement {

  val propertyChangeSupport: PropertyChangeSupport = new PropertyChangeSupport(this)

  def addPropertyChangeListener(listener: PropertyChangeListener): Unit = {
    propertyChangeSupport.addPropertyChangeListener(listener)
  }

  def removePropertyChangeListener(listener: PropertyChangeListener): Unit = {
    propertyChangeSupport.removePropertyChangeListener(listener)
  }

  /**
   * 布局位置附加元素
   */
  var meta: ModelMetaElement = _

  def getMeta = meta


  def setMeta(meta: ModelMetaElement): Unit = {
    this.setMeta(meta, false)
  }

  def setMeta(meta: ModelMetaElement, persistence: Boolean) = {
    this.meta = meta
    if (persistence) setAttribute("g", meta.getX + "," + meta.getY + "," + meta.width + "," + meta.height)
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.SET_META_PROP, null, meta)
  }


  /**
   * 父模型属性
   */
  var myParent: XmlTagModelElement = _

  def setParent(parent: XmlTagModelElement): Unit = {
    this.myParent = parent
  }

  def getParent: XmlTagModelElement = {
    myParent
  }

  /**
   * 子模型集合属性
   */
  var myChildren: java.util.List[XmlTagModelElement] = new java.util.ArrayList()

  /**
   * 获取子元素
   *
   * @return
   */
  def getChildren: java.util.List[XmlTagModelElement] = {
    myChildren
  }

  def addSubTag(newXmlTag: XmlTag, first: Boolean): XmlTag = {
    xmlTag.addSubTag(newXmlTag, first)
  }

  /**
   * 新增子元素
   *
   * @param child
   */
  def addChild(child: XmlTagModelElement): Unit = {
    myChildren.add(child)
    child.setParent(this)
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.ADD_SUB_TAG_PROP, null, xmlTag)
  }

  /**
   * 新增指定索引的子元素
   *
   * @param child
   * @param index
   */
  def addChild(child: XmlTagModelElement, index: Int): Unit = {
    myChildren.add(index, child)
    child.setParent(this)
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.ADD_SUB_TAG_PROP, null, xmlTag)
  }


  def removeChild(child: XmlTagModelElement): Boolean = {
    if (myChildren.remove(child)) {
      propertyChangeSupport.firePropertyChange(XmlTagModelElement.REMOVE_SUB_TAG_PROP, null, xmlTag)
      return true
    }
    false
  }

  def removeSubTag(subTag: XmlTag): Unit = {
    subTag.delete()
  }

  def setAttribute(qname: String, value: String): Unit = {
    if (xmlTag != null) {
      xmlTag.setAttribute(qname, value)
    }
    //       qname match {
    //        case "meta" => {
    //          setMeta(null)
    //        }
    //        case _ =>
    //      }
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.SET_PROP, null, xmlTag)
  }

  def addSourceWire(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.ADD_SOURCE_CONNECTION_PROP, null, xmlTag)
  }

  def addTargetWire(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.ADD_TARGET_CONNECTION_PROP, null, xmlTag)
  }


  def removeSourceTransition(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.REMOVE_SOURCE_CONNECTION_PROP, null, xmlTag)
  }

  def removeTargetTransition(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.REMOVE_TARGET_CONNECTION_PROP, null, xmlTag)
  }

  def cleanTransition(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.CLEAN_TRANSITION_PROP, null, xmlTag)
  }


  def addPromotingService(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.PROMOTING_SERVICE_PROP, null, xmlTag)
  }

  def degradePromotingService(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.DEGRADE_SERVICE_PROP, null, xmlTag)
  }

  def degradePropertyService(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.DEGRADE_PROPERTY_PROP, null, xmlTag)
  }

  def removePromotingService(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.REMOVE_SUB_TAG_PROP, null, xmlTag)
  }

  def removePromotingProperty(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.REMOVE_SUB_TAG_PROP, null, xmlTag)
  }

  def addPromotingProperty(): Unit = {
    propertyChangeSupport.firePropertyChange(XmlTagModelElement.PROMOTING_PROPERTY_PROP, null, xmlTag)
  }

  def setConnectMode(isConnectMode: Boolean): Unit = {
    if (isConnectMode) {
      propertyChangeSupport.firePropertyChange(XmlTagModelElement.CONNECT_MODE_ENABLE, null, xmlTag)
    } else {
      propertyChangeSupport.firePropertyChange(XmlTagModelElement.CONNECT_MODE_DISABLE, null, xmlTag)
    }
  }

}

object XmlTagModelElement {
  val ADD_SUB_TAG_PROP: String = "Add.Sub.Tag"

  val SET_PROP: String = "Set"

  val SET_META_PROP: String = "Set.Meta"

  val PROMOTING_SERVICE_PROP: String = "Promoting.Service"

  val PROMOTING_PROPERTY_PROP: String = "Promoting.Property"

  val DEGRADE_SERVICE_PROP: String = "Degrade.Service"

  val DEGRADE_PROPERTY_PROP: String = "Degrade.Property"

  val REMOVE_SUB_TAG_PROP: String = "Remove.Sub.Tag"

  val ADD_TARGET_CONNECTION_PROP = "Add.Target.Connection"

  val ADD_SOURCE_CONNECTION_PROP = "Add.Source.Connection"

  val REMOVE_SOURCE_CONNECTION_PROP = "Remove.Source.Connection"

  val REMOVE_TARGET_CONNECTION_PROP = "Remove.Target.Connection"

  //--清除所有连线属性,用于删除
  val CLEAN_TRANSITION_PROP = "Clean.Transition"

  val REFRESH_PROP = "Refresh"

  val CONNECT_MODE_ENABLE = "Connect.Mode.Enable"

  val CONNECT_MODE_DISABLE = "Connect.Mode.Disable"
}
