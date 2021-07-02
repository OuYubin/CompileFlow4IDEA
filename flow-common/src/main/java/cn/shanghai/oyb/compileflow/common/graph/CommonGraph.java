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

package cn.shanghai.oyb.compileflow.common.graph;

import cn.shanghai.oyb.flow.core.editor.editpart.factory.TGraphEditPartFactory;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.jgraphx.model.GraphModel;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxStylesheet;
import org.jetbrains.annotations.Nullable;

/**
 *
 * 对原有图形API进行封装,增加编辑部件工厂,增加连线部件工厂
 *
 * @author ouyubin
 *
 */
public class CommonGraph extends Graph {

    private TGraphEditPartFactory editPartFactory;

    public CommonGraph(GraphModel model, mxStylesheet stylesheet) {
        super(model, stylesheet);
    }

    public TGraphEditPartFactory getEditPartFactory() {
        return editPartFactory;
    }

    public void setEditPartFactory(TGraphEditPartFactory editPartFactory) {
        this.editPartFactory = editPartFactory;
    }

    @Override
    public void drawCell(@Nullable mxICanvas canvas, @Nullable Object cell) {
        super.drawCell(canvas, cell);
    }

    /**
     * 组件图元图形绘制关键方法,开始绘制Cell时就会调用该方法
     * @param canvas
     * @param state
     * @param drawLabel
     */
    @Override
    public void drawState(mxICanvas canvas, mxCellState state, boolean drawLabel) {
        super.drawState(canvas, state, drawLabel);
    }

}
