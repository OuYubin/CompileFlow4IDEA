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

package cn.shanghai.oyb.compileflow.common.component;

import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.swing.Canvas;
import cn.shanghai.oyb.flow.core.window.listener.TSelectionListener;
import cn.shanghai.oyb.flow.core.window.provider.TSelectionListenerProvider;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import cn.shanghai.oyb.jgraphx.component.GraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;

/**
 * 编辑器通用图形面板
 *
 * @author ouyubin
 */
public class CommonGraphComponent extends GraphComponent implements TSelectionListener {

    protected TSelectionListenerProvider mySelectionListenerProvider;

    public CommonGraphComponent(Graph graph, TAdaptable adaptable) {
        super(graph, adaptable);
    }

    /**
     * 图形绘制对象，用于绘制图像
     * @return
     */
    @Override
    public mxInteractiveCanvas createCanvas() {
        return new Canvas();
    }

    @Override
    public void selectionChanged(TSelectionListenerProvider selectionListenerProvider) {

    }

    public TSelectionListenerProvider getSelectionListenerProvider() {
        return mySelectionListenerProvider;
    }
}
