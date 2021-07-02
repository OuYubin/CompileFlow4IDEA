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

package cn.shanghai.oyb.compileflow.editor.canvas

import cn.shanghai.oyb.compileflow.editor.cells.CompileFlowBpmCell
import cn.shanghai.oyb.flow.core.swing.Canvas

import java.awt.Rectangle
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.{mxConstants, mxPoint}
import com.mxgraph.view.mxCellState
import org.apache.commons.lang3.StringUtils

import java.awt.geom.GeneralPath

/**
 * @author ouyubin
 */
class CompileFlowCanvas extends Canvas {

  override def intersects(
                           graphComponent: mxGraphComponent, rect: Rectangle,
                           state: mxCellState
                         ): Boolean = {
    if (state != null) {
      val cell = state.getCell
      if (cell.isInstanceOf[CompileFlowBpmCell]) {
        return true
      }
    }
    super.intersects(graphComponent, rect, state)
  }

  /**
   * 绘制多点线条
   * @param points
   * @param rounded
   * @param startOffset
   * @param endOffset
   * @param direction
   */
  def paintPolyline(points: Array[mxPoint], rounded: Boolean, startOffset: Int, endOffset: Int, direction: String): Unit = {
    if (points != null && points.length > 1) {
      var pt = points(0)
      val pe = points(points.length - 1)
      val arcSize = mxConstants.LINE_ARCSIZE * scale
      val path = new GeneralPath
      if (StringUtils.equals(direction, "horizontal")) {
        path.moveTo(pt.getX.toFloat + startOffset, pt.getY.toFloat)
      } else {
        path.moveTo(pt.getX.toFloat, pt.getY.toFloat + startOffset)
      }
      // Draws the line segments
      var i = 1
      while ( {
        i < points.length - 1
      }) {
        var tmp = points(i)
        var dx = pt.getX - tmp.getX
        var dy = pt.getY - tmp.getY
        if ((rounded && i < points.length - 1) && (dx != 0 || dy != 0)) { // Draws a line from the last point to the current
          var dist = Math.sqrt(dx * dx + dy * dy)
          val nx1 = dx * Math.min(arcSize, dist / 2) / dist
          val ny1 = dy * Math.min(arcSize, dist / 2) / dist
          val x1 = tmp.getX + nx1
          val y1 = tmp.getY + ny1
          path.lineTo(x1.toFloat, y1.toFloat)
          var next = points(i + 1)
          while ( {
            i < points.length - 2 && next.getX - tmp.getX.round == 0 && next.getY - tmp.getY.round == 0
          }) {
            next = points(i + 2)
            i += 1
          }
          dx = next.getX - tmp.getX
          dy = next.getY - tmp.getY
          dist = Math.max(1, Math.sqrt(dx * dx + dy * dy))
          val nx2 = dx * Math.min(arcSize, dist / 2) / dist
          val ny2 = dy * Math.min(arcSize, dist / 2) / dist
          val x2 = tmp.getX + nx2
          val y2 = tmp.getY + ny2
          path.quadTo(tmp.getX.toFloat, tmp.getY.toFloat, x2.toFloat, y2.toFloat)
          tmp = new mxPoint(x2, y2)
        }
        else path.lineTo(tmp.getX.toFloat, tmp.getY.toFloat)
        pt = tmp

        i += 1
      }
      if (StringUtils.equals(direction, "horizontal")) {
        path.lineTo(pe.getX.toFloat + endOffset, pe.getY.toFloat)
      } else {
        path.lineTo(pe.getX.toFloat, pe.getY.toFloat + endOffset)
      }
      g.draw(path)
    }
  }
}
