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

package cn.shanghai.oyb.jgraphx.component

import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.JBColor
import cn.shanghai.oyb.flow.core.internal.TAdaptable
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D


/**
 *
 * 编辑器图形面板对象,其实也就是自定义的图形面板控件
 *
 * @author ouyubin
 *
 */

open class GraphComponent : mxGraphComponent {

    private var LOG = Logger.getInstance("#" + GraphComponent::class.java.name)

    var adapter: TAdaptable? = null


    constructor(graph: mxGraph, adapter: TAdaptable) : this(graph) {
        this.adapter=adapter
        super.setDragEnabled(false)
        super.setAntiAlias(true)
        super.setFoldingEnabled(false)
        super.setGridVisible(true)
        super.setGridStyle(GRID_STYLE_DASHED)
        super.setGridColor(JBColor(Color(253, 245, 230), Color(80, 80, 80)))
        //--使其不透明
        super.setOpaque(true)
        //super.setBackground(Color.WHITE)
        super.setAutoScroll(false)
        super.setPreferPageSize(false)
        super.setPreviewAlpha(80f)
        super.setToolTips(true)
        super.setTextAntiAlias(true)
    }

    constructor(graph: mxGraph) : super(graph)

    /**
     * Java Swing基本的绘制入口
     */
    override fun paint(g: Graphics) {
        LOG.info("\uD83C\uDFA8开始图形面板渲染...")
        super.paint(g)
    }

    /**
     * 实际图形控件属性绘制及设定
     */
    override fun paintComponent(g: Graphics) {
        LOG.info("\uD83C\uDFA8实际图形面板渲染...")
        super.paintComponent(g)
    }

    /**
     * 创建图形控件
     */
    override fun createGraphControl(): mxGraphControl {
        return ComponentMXGraphControl()
    }

    /**
     * 复写双击屏蔽
     */
    override fun installDoubleClickHandler() {

    }

    inner class ComponentMXGraphControl : mxGraphControl() {

        /**
         * 重新定义绘制图形动作
         */
        override fun drawGraph(g: Graphics2D, drawLabels: Boolean) {
            LOG.info("😊开始图形绘制...")
            val previousGraphics = canvas.getGraphics()
            val previousDrawLabels = canvas.isDrawLabels()
            val previousTranslate = canvas.getTranslate()
            val previousScale = canvas.getScale()

            try {
                canvas.setScale(graph.getView().getScale())
                canvas.setDrawLabels(false)
                canvas.setTranslate(0.0, 0.0)
                canvas.setGraphics(g)

                drawFromRootCell()
            } finally {
                canvas.setScale(previousScale)
                canvas.setTranslate(previousTranslate.getX(), previousTranslate.getY())
                canvas.setDrawLabels(previousDrawLabels)
                canvas.setGraphics(previousGraphics)
            }
        }

    }
}