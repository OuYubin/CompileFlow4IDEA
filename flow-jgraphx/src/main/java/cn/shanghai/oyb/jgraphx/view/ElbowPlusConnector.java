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

package cn.shanghai.oyb.jgraphx.view;

import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraphView;

import java.util.List;

/**
 * @author ouyubin
 */
public class ElbowPlusConnector implements mxEdgeStyle.mxEdgeStyleFunction {
    @Override
    public void apply(mxCellState state, mxCellState source, mxCellState target, List<mxPoint> points, List<mxPoint> result) {
        {
            mxPoint pt = (points != null && points.size() > 0) ? points.get(0)
                    : null;

            boolean vertical = false;
            boolean horizontal = false;

            if (source != null && target != null) {
                if (pt != null) {
                    double left = Math.min(source.getX(), target.getX());
                    double right = Math.max(source.getX() + source.getWidth(),
                            target.getX() + target.getWidth());

                    double top = Math.min(source.getY(), target.getY());
                    double bottom = Math.max(
                            source.getY() + source.getHeight(), target.getY()
                                    + target.getHeight());

                    pt = state.getView().transformControlPoint(state, pt);

                    vertical = pt.getY() < top || pt.getY() > bottom;
                    horizontal = pt.getX() < left || pt.getX() > right;
                } else {
                    double left = Math.max(source.getX(), target.getX());
                    double right = Math.min(source.getX() + source.getWidth(),
                            target.getX() + target.getWidth());

                    vertical = left == right;

                    if (!vertical) {
                        double top = Math.max(source.getY(), target.getY());
                        double bottom = Math.min(
                                source.getY() + source.getHeight(),
                                target.getY() + target.getHeight());

                        horizontal = top == bottom;
                    }
                }
            }
            TopToBottom.apply(state, source, target, points,
                    result);

        }
    }

    public static mxEdgeStyle.mxEdgeStyleFunction TopToBottom = new mxEdgeStyle.mxEdgeStyleFunction() {

        public void apply(mxCellState state, mxCellState source,
                          mxCellState target, List<mxPoint> points, List<mxPoint> result) {
            mxGraphView view = state.getView();
            mxPoint pt = ((points != null && points.size() > 0) ? points.get(0)
                    : null);
            mxPoint p0 = state.getAbsolutePoint(0);
            mxPoint pe = state
                    .getAbsolutePoint(state.getAbsolutePointCount() - 1);

            if (pt != null) {
                pt = view.transformControlPoint(state, pt);
            }

            if (p0 != null) {
                source = new mxCellState();
                source.setX(p0.getX());
                source.setY(p0.getY());
            }

            if (pe != null) {
                target = new mxCellState();
                target.setX(pe.getX());
                target.setY(pe.getY());
            }

            if (source != null && target != null) {
                //--最下位置 bottom
                double b = Math.max(source.getY(), target.getY());
                //--最上位置 top
                double t = Math.min(source.getY() + source.getHeight(),
                        target.getY() + target.getHeight());

                //--起点中心位置
                double x = view.getRoutingCenterX(source);

                if (pt != null && pt.getX() >= source.getX()
                        && pt.getX() <= source.getX() + source.getWidth()) {
                    x = pt.getX();
                }

                //--计算第一拐点y坐标位置,其实是计算两个图元的纵向间距的中心位置
                double y = (pt != null) ? pt.getY() : t + (b - t) / 2;

                if (!target.contains(x, y) && !source.contains(x, y)) {
                    //--添加第一拐点位置
                    result.add(new mxPoint(x, y));
                }

                if (pt != null && pt.getX() >= target.getX()
                        && pt.getX() <= target.getX() + target.getWidth()) {
                    x = pt.getX();
                } else {
                    //--计算第二拐点x坐标位置
                    x = view.getRoutingCenterX(target);
                }

                if (!target.contains(x, y) && !source.contains(x, y)) {
                    //--添加第二拐点位置
                    result.add(new mxPoint(x, y));
                }

                if (result.size() == 1) {
                    if (pt != null) {
                        if (!target.contains(pt.getX(), y) && !source.contains(pt.getX(), y)) {
                            result.add(new mxPoint(pt.getX(), y));
                        }
                    } else {
                        double l = Math.max(source.getX(), target.getX());
                        double r = Math.min(source.getX() + source.getWidth(),
                                target.getX() + target.getWidth());
                        result.add(new mxPoint(l + (r - l) / 2, y));
                    }
                }
            }
        }
    };
}
