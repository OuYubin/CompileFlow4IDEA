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

package cn.shanghai.oyb.jgraphx.view

import cn.shanghai.oyb.jgraphx.model.GraphModel
import com.intellij.openapi.diagnostic.Logger
import com.mxgraph.model.mxGeometry
import com.mxgraph.model.mxGraphModel
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxPoint
import com.mxgraph.util.mxUtils
import com.mxgraph.view.mxCellState
import com.mxgraph.view.mxConnectionConstraint
import com.mxgraph.view.mxGraph
import com.mxgraph.view.mxGraphView

/**
 * @author ouyubin
 */
open class GraphView(graph: mxGraph) : mxGraphView(graph) {

    val LOG = Logger.getInstance(GraphView::class.java)

    override fun updatePoints(
        edge: mxCellState?, points: List<mxPoint>?,
        source: mxCellState?, target: mxCellState?
    ) {
        if (edge != null) {
            val pts = ArrayList<mxPoint>()
            pts.add(edge.getAbsolutePoint(0))
            val edgeStyle = getEdgeStyle(
                edge, points, source,
                target
            )

            if (edgeStyle != null) {
                val src = getTerminalPort(edge, source, true)
                val trg = getTerminalPort(edge, target, false)

                edgeStyle.apply(edge, src, trg, points, pts)
            } else if (points != null) {
                for (i in points.indices) {
                    pts.add(transformControlPoint(edge, points[i]))
                }
            }

            pts.add(edge.getAbsolutePoint(edge.absolutePointCount - 1))
            edge.absolutePoints = pts
        }
    }

    override fun getTerminalPort(
        state: mxCellState?, terminal: mxCellState?,
        source: Boolean
    ): mxCellState? {
        var tmpTerminal = terminal
        val key = if (source)
            mxConstants.STYLE_SOURCE_PORT
        else
            mxConstants.STYLE_TARGET_PORT
        val id = mxUtils.getString(state!!.style, key)

        if (id != null && graph.model is mxGraphModel) {
            val tmp = getState(
                (graph.model as mxGraphModel)
                    .getCell(id)
            )

            // Only uses ports where a cell state exists
            if (tmp != null) {
                tmpTerminal = tmp
            }
        }

        return tmpTerminal
    }

    override fun updateFloatingTerminalPoint(
        edge: mxCellState,
        start: mxCellState?, end: mxCellState?, source: Boolean
    ) {
        var start = getTerminalPort(edge, start, source)
        val next = getNextPoint(edge, end, source)
        var border = mxUtils.getDouble(
            edge.style,
            mxConstants.STYLE_PERIMETER_SPACING
        )
        border += mxUtils.getDouble(
            edge.style,
            if (source) mxConstants.STYLE_SOURCE_PERIMETER_SPACING else mxConstants.STYLE_TARGET_PERIMETER_SPACING
        )
        val pt = getPerimeterPoint(
            start, next, graph.isOrthogonal(edge),
            border
        )
        edge.setAbsoluteTerminalPoint(pt, source)
    }


    override fun getPerimeterPoint(
        terminal: mxCellState?, next: mxPoint?,
        orthogonal: Boolean, border: Double
    ): mxPoint? {
        var point: mxPoint? = null
        if (terminal != null) {
            val perimeter = getPerimeterFunction(terminal)
            if (perimeter != null && next != null) {
                val bounds = getPerimeterBounds(terminal, border)
                if (bounds.width > 0 || bounds.height > 0) {
                    point = perimeter.apply(bounds, terminal, next, orthogonal)
                }
            }
            if (point == null) {
                point = getPoint(terminal)
            }
        }
        return point
    }


    override fun updateEdgeState(state: mxCellState, geo: mxGeometry) {
        val source = state.getVisibleTerminalState(true)
        val target = state.getVisibleTerminalState(false)

        if (graph.model.getTerminal(state.cell, true) != null && source == null
            || source == null && geo.getTerminalPoint(true) == null
            || graph.model.getTerminal(state.cell, false) != null && target == null
            || target == null && geo.getTerminalPoint(false) == null
        ) {
            clear(state.cell, true, true)
        } else {
            updateFixedTerminalPoints(state, source, target)
            updatePoints(state, geo.points, source, target)
            updateFloatingTerminalPoints(state, source, target)
            if (state.cell !== getCurrentRoot()
                && (state.absolutePointCount < 2 || state.getAbsolutePoint(0) == null || state
                    .getAbsolutePoint(state.absolutePointCount - 1) == null)
            ) {
                clear(state.cell, true, true)
            } else {
                updateEdgeBounds(state)
                state.absoluteOffset = getPoint(state, geo)
            }
        }
    }

    override fun updateFixedTerminalPoints(
        edge: mxCellState?, source: mxCellState?,
        target: mxCellState?
    ) {
        updateFixedTerminalPoint(
            edge!!, source, true,
            graph.getConnectionConstraint(edge, source, true)
        )
        updateFixedTerminalPoint(
            edge, target, false,
            graph.getConnectionConstraint(edge, target, false)
        )
    }

    override fun updateFixedTerminalPoint(
        edge: mxCellState,
        terminal: mxCellState?, source: Boolean,
        constraint: mxConnectionConstraint?
    ) {
        var pt: mxPoint? = null
        if (constraint != null) {
            pt = graph.getConnectionPoint(terminal, constraint)
//            var pts = edge.absolutePoints
//            if (pts != null) {
//                LOG.info("当前路点开始...")
//                for (pt in pts) {
//                    if (pt != null)
//                        LOG.info("坐标: [x=" + pt.x + ",y=" + pt.y + "]")
//                }
//                LOG.info("当前路点结束...")
//            }

        }
        if (pt == null && terminal == null) {
            val orig = edge.origin
            val geo = graph.getCellGeometry(edge.cell)
            pt = geo.getTerminalPoint(source)
            if(source&&pt!=null){
                LOG.info("坐标: [x=" + pt.x + ",y=" + pt.y + "]")
            }
            if (pt != null) {

                pt = mxPoint(
                    scale
                            * (translate.x + pt.x + orig.x), (scale
                            * (translate.y + pt.y + orig.y))
                )
//                if(pt!=null){
//                    LOG.info("坐标: [x=" + pt.x + ",y=" + pt.y + "]")
//                }
            }
        }
//        if (pt != null)
//            LOG.info("坐标: [x=" + pt.x + ",y=" + pt.y + "]")

        edge.setAbsoluteTerminalPoint(pt, source)

    }

    override fun updateFloatingTerminalPoints(
        state: mxCellState,
        source: mxCellState?, target: mxCellState?
    ) {
        val p0 = state.getAbsolutePoint(0)
        val pe = state.getAbsolutePoint(state.absolutePointCount - 1)
        if (pe == null && target != null) {
            updateFloatingTerminalPoint(state, target, source, false)
        }
        if (p0 == null && source != null) {
            updateFloatingTerminalPoint(state, source, target, true)
        }
    }

    /**
     * 对图形视图进行模型验证
     */
    override fun revalidate() {
        super.revalidate()
    }
}