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

package cn.shanghai.oyb.compileflow.editor.graph;

import cn.shanghai.oyb.compileflow.common.graph.CommonGraph;
import cn.shanghai.oyb.jgraphx.model.GraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxConnectionConstraint;
import com.mxgraph.view.mxStylesheet;
import org.apache.log4j.Logger;

/**
 * @author ouyubin
 */
public class CompileFlowGraph extends CommonGraph {

    private static final Logger LOG= Logger.getLogger(CompileFlowGraph.class);

    public CompileFlowGraph(GraphModel model, mxStylesheet stylesheet) {
        super(model, stylesheet);
    }

    @Override
    public boolean isCellResizable(Object cell) {
//        if (cell instanceof CompileFlowLoopCell) {
//            return true;
//        }
//        if(cell instanceof CompileFlowElementCell){
//            return false;
//        }
        return super.isCellResizable(cell);
    }

    /**
     * 重新构建拐点
     *
     * @param vertex
     * @param constraint
     * @return
     */
    public mxPoint getConnectionPoint(mxCellState vertex,
                                      mxConnectionConstraint constraint) {
        mxPoint point = null;
        if (vertex != null && constraint.getPoint() != null) {
            mxRectangle bounds = this.view.getPerimeterBounds(vertex, 0);
            mxPoint cx = new mxPoint(bounds.getCenterX(), bounds.getCenterY());
            String direction = mxUtils.getString(vertex.getStyle(),
                    mxConstants.STYLE_DIRECTION);

            double r1 = 0;

            if (direction != null) {
                if (direction.equals(mxConstants.DIRECTION_NORTH)) {
                    r1 += 270;
                } else if (direction.equals(mxConstants.DIRECTION_WEST)) {
                    r1 += 180;
                } else if (direction.equals(mxConstants.DIRECTION_SOUTH)) {
                    r1 += 90;
                }
                if (direction == mxConstants.DIRECTION_NORTH
                        || direction == mxConstants.DIRECTION_SOUTH) {
                    bounds.rotate90();
                }
            }
            point = new mxPoint(
                    bounds.getX() + constraint.getPoint().getX() * bounds.getWidth(),
                    bounds.getY()
                            + constraint.getPoint().getY() * bounds.getHeight());
            double r2 = mxUtils.getDouble(vertex.getStyle(),
                    mxConstants.STYLE_ROTATION);

            if (constraint.isPerimeter()) {
                if (r1 != 0) {
                    double cos = 0;
                    double sin = 0;

                    if (r1 == 90) {
                        sin = 1;
                    } else if (r1 == 180) {
                        cos = -1;
                    } else if (r1 == 270) {
                        sin = -1;
                    }

                    point = mxUtils.getRotatedPoint(point, cos, sin, cx);
                }

                point = this.view.getPerimeterPoint(vertex, point, false);
            } else {
                r2 += r1;

                if (this.getModel().isVertex(vertex.getCell())) {
                    boolean flipH = mxUtils.getString(vertex.getStyle(),
                            mxConstants.STYLE_FLIPH).equals(1);
                    boolean flipV = mxUtils.getString(vertex.getStyle(),
                            mxConstants.STYLE_FLIPV).equals(1);

                    if (flipH) {
                        point.setX(2 * bounds.getCenterX() - point.getX());
                    }

                    if (flipV) {
                        point.setY(2 * bounds.getCenterY() - point.getY());
                    }
                }

            }
            // Generic rotation after projection on perimeter
            if (r2 != 0 && point != null) {
                double rad = Math.toRadians(2);
                double cos = Math.cos(rad);
                double sin = Math.sin(rad);

                point = mxUtils.getRotatedPoint(point, cos, sin, cx);
            }
        }

        if (point != null) {
            point.setX(Math.round(point.getX()));
            point.setY(Math.round(point.getY()));
        }

        return point;
    }


    public mxConnectionConstraint getConnectionConstraint(mxCellState edge,
                                                          mxCellState terminal, boolean source) {
        mxPoint point = null;
        //--重新对当前边样式进行重新设定
        Object x = edge.getStyle().get((source) ? mxConstants.STYLE_EXIT_X
                : mxConstants.STYLE_ENTRY_X);

        if (x != null) {
            Object y = edge.getStyle().get((source) ? mxConstants.STYLE_EXIT_Y
                    : mxConstants.STYLE_ENTRY_Y);
            if (y != null) {
                point = new mxPoint(Double.parseDouble(x.toString()),
                        Double.parseDouble(y.toString()));
            }
        }

        boolean perimeter = false;

        if (point != null) {
            perimeter = mxUtils.isTrue(edge.getStyle(),
                    (source) ? mxConstants.STYLE_EXIT_PERIMETER
                            : mxConstants.STYLE_ENTRY_PERIMETER,
                    true);
        }

        return new mxConnectionConstraint(point, perimeter);
    }
}
