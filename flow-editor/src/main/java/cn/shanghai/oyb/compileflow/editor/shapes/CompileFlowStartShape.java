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

import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.shape.Shape;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import icons.CompileFlowImages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.NAME_ATTR_NAME;

/**
 * 开始图形对象
 *
 * @author ouyubin
 */
public class CompileFlowStartShape extends Shape {


    public CompileFlowStartShape(@NotNull Object value) {
        super(value);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
        String id = "开始";
        Object model = getValue();
        if (model instanceof XmlTagModelElement) {
            XmlTag xmlTag = ((XmlTagModelElement) model).getXmlTag();
            if (xmlTag.isValid())
                id = xmlTag.getAttributeValue(NAME_ATTR_NAME)==null?"开始":xmlTag.getAttributeValue(NAME_ATTR_NAME);
            else
                super.paintShape(canvas, state);
        }
        Rectangle rect = state.getRectangle();
        int x = rect.x;
        int y = rect.y;
        int height = rect.height;
        int width = rect.width;
        BasicStroke basicStroke = new BasicStroke(1F);
        Graphics2D gc = canvas.getGraphics();
        gc.setColor(new JBColor(JBColor.darkGray, JBColor.white));
        gc.setStroke(basicStroke);
        int shadowWidth = 2;

        //--渐进着色
        JBColor base = new JBColor(new Color(152, 251, 152), new Color(152, 251, 152));
        gc.setColor(base);
        gc.drawRoundRect(x, y, width, height - shadowWidth - 1, 30, 30);
        Point2D start = new Point2D.Float(x - 1, y - 1);
        Point2D end = new Point2D.Float(x - 1, y + height - 1);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(152, 251, 152), Color.WHITE};
        if (UIUtil.isUnderDarcula()) {
            colors = new Color[]{Gray._122, Gray._122};
        }
        Paint paint = new LinearGradientPaint(start, end, dist, colors);
        gc.setPaint(paint);
        gc.fillRoundRect(x + 1, y + 1, width - 1, height - shadowWidth - 2, 30, 30);

        if (!UIUtil.isUnderDarcula()) {
            int[] pointX = {x + 15, x + width - 15, x + width - 5, x + 5};
            int[] pointY = {y + height - shadowWidth, y + height - shadowWidth, y + height, y + height};
            gc.setColor(new JBColor(JBColor.lightGray, JBColor.DARK_GRAY));
            gc.fillPolygon(pointX, pointY, 4);
        }

        int halfWidth = width / 2;
        gc.setColor(new JBColor(JBColor.darkGray, JBColor.gray));

        FontMetrics fontMetrics = gc.getFontMetrics();
        ImageIcon imageIcon = CompileFlowImages.START_IMAGE();
        int labelWidth = imageIcon.getIconWidth() + 5 + fontMetrics.stringWidth(id == null ? "" : id);
        //--需要对显示字符进行处理
        if (labelWidth > width) {
            id = StringUtils.substring(id, 0, 4) + "...";
            labelWidth = imageIcon.getIconWidth() + 5 + fontMetrics.stringWidth(id);
        }
        int positionX = x + halfWidth - labelWidth / 2;
        gc.drawImage(imageIcon.getImage(), positionX, y + height / 2 - imageIcon.getIconHeight() / 2, 16, 16, null);
        gc.setColor(new JBColor(Color.BLACK, Color.WHITE));
        gc.drawString(id, positionX + imageIcon.getIconWidth() + 5, y + height / 2 + imageIcon.getIconHeight() / 3 - shadowWidth);

    }
}
