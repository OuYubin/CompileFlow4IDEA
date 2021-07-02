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

package cn.shanghai.oyb.jgraphx.graph

import cn.shanghai.oyb.flow.core.internal.TAdapter
import cn.shanghai.oyb.jgraphx.view.GraphView
import com.mxgraph.canvas.mxICanvas
import com.mxgraph.model.mxIGraphModel
import com.mxgraph.util.mxEvent
import com.mxgraph.view.mxGraph
import com.mxgraph.view.mxGraphView
import com.mxgraph.view.mxStylesheet

/**
 * 构建图形的顶级API,处理所有图形绘制事务
 *
 * @author ouyubin
 */
open class Graph : mxGraph, TAdapter {

    private var mxGraphView: GraphView? = null

    constructor(model: mxIGraphModel, styleSheet: mxStylesheet?) : super(model, styleSheet) {
        super.setVertexLabelsMovable(false)
        super.setGridEnabled(true)
        super.setGridSize(20)
        super.setAutoSizeCells(true)
    }


    /**
     * 用于记录cell单元状态的对象
     */
    override fun createGraphView(): mxGraphView {
        this.mxGraphView = GraphView(this)
        return mxGraphView as GraphView
    }

    override fun drawCell(canvas: mxICanvas?, cell: Any?) {
        super.drawCell(canvas, cell)
    }

    /**
     * 注入模型时对模型加入监听
     */
    override fun setModel(value: mxIGraphModel) {
        if (model != null) {
            model.removeListener(graphModelChangeHandler)
        }

        val oldModel = model
        model = value

        if (view != null) {
            view.revalidate()
        }

        //--加入监听
        model.addListener(mxEvent.CHANGE, graphModelChangeHandler)
        changeSupport.firePropertyChange("model", oldModel, model)
        repaint()
    }


    override fun convertValueToString(cell: Any): String? {
        return ""
    }


    override fun getLabel(cell: Any?): String? {
        return ""
    }

    override fun notifyChanged() {
        mxGraphView?.revalidate()
        repaint()
    }


}