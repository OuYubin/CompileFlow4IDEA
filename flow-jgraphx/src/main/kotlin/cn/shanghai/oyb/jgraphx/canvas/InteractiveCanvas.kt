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

package cn.shanghai.oyb.jgraphx.canvas

import cn.shanghai.oyb.jgraphx.model.Cell
import com.mxgraph.shape.mxIShape
import com.mxgraph.swing.view.mxInteractiveCanvas
import com.mxgraph.view.mxCellState
import java.awt.AlphaComposite
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxUtils
import java.awt.Graphics2D
import com.mxgraph.util.mxRectangle


/**
 * 自定义组件抽象画布,重写绘制动作
 *
 * @author ouyubin
 */
abstract class InteractiveCanvas : mxInteractiveCanvas() {


    /**
     * 覆写绘制实际动作
     *
     * @param state
     * @return
     */
    override fun drawCell(state: mxCellState): Any {
        val cell = state.cell
        var shape: mxIShape? = null
        if (cell is Cell) {
            shape = createComponentShape(cell)
        }
        if (shape != null) {
            val previousGraphics = g
            g = createTemporaryGraphics(null, 100f, state)

            // Paints the shape and restores the graphics object
            //--绘制图元形状
            shape.paintShape(this, state)
            g.dispose()
            g = previousGraphics
        } else {
            return super.drawCell(state)
        }
        return shape
    }

    abstract fun createComponentShape(cell: Cell): mxIShape?

    override fun createTemporaryGraphics(style: Map<String, Any>?,
                                         opacity: Float, bounds: mxRectangle?): Graphics2D {
        val temporaryGraphics = g.create() as Graphics2D

        // Applies the default translate
        temporaryGraphics.translate(translate.x, translate.y)


        // Applies the rotation on the graphics object
        if (bounds != null) {
            if (style != null) {
                val rotation = mxUtils.getDouble(style,
                        mxConstants.STYLE_ROTATION, 0.0)

                if (rotation != 0.0) {
                    temporaryGraphics.rotate(Math.toRadians(rotation),
                            bounds.centerX, bounds.centerY)
                }
            }
        }

        // Applies the opacity to the graphics object
        if (opacity != 100f) {
            temporaryGraphics.composite = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, opacity / 100)
        }

        return temporaryGraphics
    }




}
