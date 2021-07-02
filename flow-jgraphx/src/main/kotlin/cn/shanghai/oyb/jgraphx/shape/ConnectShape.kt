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

package cn.shanghai.oyb.jgraphx.shape

import com.mxgraph.canvas.mxGraphics2DCanvas
import com.mxgraph.view.mxCellState
import java.awt.BasicStroke
import java.awt.Color
import java.util.*

/**
 * @author ouyubin
 */
open class ConnectShape : IShape {

    override fun paintShape(canvas: mxGraphics2DCanvas, state: mxCellState) {
        if (state.absolutePointCount > 1) {
            val pts = ArrayList(
                state.absolutePoints
            )
            val basicStroke = BasicStroke(1f)
            val gc = canvas.graphics
            gc.color = Color(128, 0, 128)
            gc.stroke = basicStroke

            canvas.paintPolyline(pts.toTypedArray(), false)
        }
    }


}