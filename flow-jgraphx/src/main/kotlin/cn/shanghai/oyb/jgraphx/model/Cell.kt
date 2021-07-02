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

package cn.shanghai.oyb.jgraphx.model

import com.mxgraph.model.mxCell
import com.mxgraph.model.mxGeometry
import com.mxgraph.model.mxICell

/**
 * 图元对象,超类在定义过程中缺乏已编程方式自定义样式组件的扩展,style一直为null,除连线外,连线直接采用缺省模式完成构建
 *
 * @author ouyubin
 */
open class Cell(value: Any?, geometry: mxGeometry?, style: String?) : mxCell(value, geometry, style) {

    constructor() : this(null, null, null)

    //--自行控制连线模型
    var myEdges: MutableList<Any>? = ArrayList()

    open fun insertLftEdge(edge: mxICell) {
        super.insert(edge)
        myEdges?.add(edge)
    }

    open fun insertLftEdge(edge: mxICell, isOutgoing: Boolean) {
        super.insertEdge(edge, isOutgoing)
        myEdges?.add(edge)
    }


    open fun removeLftEdge(edge: mxICell) {
        super.remove(edge)
        myEdges?.remove(edge)
    }

}