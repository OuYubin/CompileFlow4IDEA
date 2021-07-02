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

package cn.shanghai.oyb.jgraphx.handler

import com.intellij.openapi.diagnostic.Logger
import cn.shanghai.oyb.jgraphx.util.GraphTransferable
import com.mxgraph.swing.handler.mxGraphTransferHandler
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.swing.util.mxGraphTransferable
import com.mxgraph.util.mxRectangle
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import javax.swing.ImageIcon
import javax.swing.JComponent

/**
 * 图形面板传送控制基类
 *
 * @author ouyubin
 *
 */

open class GraphTransferHandler : mxGraphTransferHandler() {

    private val LOG: Logger = Logger.getInstance(GraphTransferHandler::class.java);


    override fun canImport(comp: JComponent, flavors: Array<DataFlavor>): Boolean {
//        for (i in flavors.indices) {
//            if (flavors[i] != null && flavors[i].equals(mxGraphTransferable.dataFlavor)) {
//                return true
//            }
//        }
//        return false
        return true
    }


    override fun createGraphTransferable(
            graphComponent: mxGraphComponent, cells: Array<Any>,
            bounds: mxRectangle, icon: ImageIcon): mxGraphTransferable {
        return GraphTransferable(
            graphComponent.graph.cloneCells(
                cells
            ), bounds, icon
        )
    }


    fun getLocation(): Point {
        return location
    }
}