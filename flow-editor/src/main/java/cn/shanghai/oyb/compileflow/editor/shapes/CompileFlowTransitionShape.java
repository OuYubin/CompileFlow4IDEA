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

package cn.shanghai.oyb.compileflow.editor.shapes;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.shape.ConnectShape;
import cn.shanghai.oyb.compileflow.editor.canvas.CompileFlowCanvas;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.NAME_ATTR_NAME;

/**
 * 传送配线图形对象
 *
 * @author ouyubin
 */
public class CompileFlowTransitionShape extends ConnectShape {

    Object value;

    public static Logger LOG = Logger.getInstance(CompileFlowTransitionShape.class);

    public CompileFlowTransitionShape() {
    }


    public CompileFlowTransitionShape(Object value) {
        this.value = value;
    }

    /**
     * 绘制动作
     *
     * @param canvas
     * @param state
     */
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
        if (state.getAbsolutePointCount() > 1) {
            List<mxPoint> pts = state.getAbsolutePoints();
            Graphics2D gc = canvas.getGraphics();
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            String name = "";
            if (value instanceof XmlTagModelElement) {
                XmlTag xmlTag = ((XmlTagModelElement) value).getXmlTag();
                name = xmlTag.getAttributeValue(NAME_ATTR_NAME) == null ? "" : xmlTag.getAttributeValue(NAME_ATTR_NAME);
                BasicStroke basicStroke = new BasicStroke(1f);
                if (StringUtils.equals(xmlTag.getParentTag().getName(), CompileFlowXmlTagConstants.NOTE_TAG_NAME)) {
                    gc.setColor(new JBColor(Color.ORANGE, Color.YELLOW));
                    basicStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[]{2,2}, 2f);
                } else {
                    gc.setColor(new JBColor(new Color(128, 0, 128), new Color(88, 157, 246)));
                }
                gc.setStroke(basicStroke);
            }
            mxPoint[] points = pts.toArray(new mxPoint[0]);
            mxPoint firstPoint = points[0];
            mxPoint finalPoint = points[pts.size() - 1];
            GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            path.moveTo(finalPoint.getX(), finalPoint.getY());

            gc.setFont(gc.getFont().deriveFont(UIUtil.getFontSize(UIUtil.FontSize.MINI)));
            int labelWidth = gc.getFontMetrics().stringWidth(name);
            int labelHeight=gc.getFontMetrics().getHeight();

            //--终端样式绘制定义,绘制规则为:如果当前路由存在4个拐点，仅仅处理Y坐标位置，如果当前路由存在3个拐点，比较X坐标即可
            if (points.length >= 4) {
                if (finalPoint.getY() > firstPoint.getY()) {
                    path.lineTo(finalPoint.getX() - 6, finalPoint.getY() - 7);
                    path.lineTo(finalPoint.getX(), finalPoint.getY());
                    path.lineTo(finalPoint.getX() + 6, finalPoint.getY() - 7);
                    gc.draw(path);
                    ((CompileFlowCanvas) canvas).paintPolyline(pts.toArray(new mxPoint[0]), true, 0, 0, "vertical");
                    gc.setColor(new JBColor(Color.BLUE, Color.LIGHT_GRAY));
                    gc.setBackground(new JBColor(Color.LIGHT_GRAY,Color.DARK_GRAY));
                    gc.drawString(name, (float) (finalPoint.getX() - labelWidth/2), (float) (finalPoint.getY() - 15));
                } else if (finalPoint.getY() < firstPoint.getY()) {
                    path.lineTo(finalPoint.getX() - 6, finalPoint.getY() + 7);
                    path.lineTo(finalPoint.getX(), finalPoint.getY());
                    path.lineTo(finalPoint.getX() + 6, finalPoint.getY() + 7);
                    gc.draw(path);
                    ((CompileFlowCanvas) canvas).paintPolyline(pts.toArray(new mxPoint[0]), true, 0, 0, "vertical");
                    gc.setColor(new JBColor(Color.BLUE, Color.LIGHT_GRAY));
                    gc.setBackground(new JBColor(Color.LIGHT_GRAY,Color.DARK_GRAY));
                    gc.drawString(name, (float) (finalPoint.getX() - labelWidth/2), (float) (finalPoint.getY() + 15));
                }
            }
            if (points.length == 3) {
                if (finalPoint.getX() > firstPoint.getX()) {
                    path.lineTo(finalPoint.getX() - 7, finalPoint.getY() + 6);
                    path.lineTo(finalPoint.getX(), finalPoint.getY());
                    path.lineTo(finalPoint.getX() - 7, finalPoint.getY() - 6);
                    gc.draw(path);
                    ((CompileFlowCanvas) canvas).paintPolyline(pts.toArray(new mxPoint[0]), true, 0, 0, "vertical");
                    gc.setColor(new JBColor(Color.BLUE, Color.LIGHT_GRAY));
                    gc.setBackground(new JBColor(Color.LIGHT_GRAY,Color.DARK_GRAY));
                    gc.drawString(name, (float) (finalPoint.getX() - labelWidth-15), (float) (finalPoint.getY()-labelHeight/2));
                } else {
                    path.lineTo(finalPoint.getX() + 7, finalPoint.getY() + 6);
                    path.lineTo(finalPoint.getX(), finalPoint.getY());
                    path.lineTo(finalPoint.getX() + 7, finalPoint.getY() - 6);
                    gc.draw(path);
                    ((CompileFlowCanvas) canvas).paintPolyline(pts.toArray(new mxPoint[0]), true, 0, 0, "vertical");
                    gc.setColor(new JBColor(Color.BLUE, Color.LIGHT_GRAY));
                    gc.setBackground(new JBColor(Color.LIGHT_GRAY,Color.DARK_GRAY));
                    gc.drawString(name, (float) (finalPoint.getX() + 15), (float) (finalPoint.getY()-labelHeight/2));
                }
            }
        }
    }

}