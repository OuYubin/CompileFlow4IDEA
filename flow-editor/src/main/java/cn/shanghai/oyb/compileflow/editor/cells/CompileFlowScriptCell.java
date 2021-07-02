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

package cn.shanghai.oyb.compileflow.editor.cells;

import cn.shanghai.oyb.compileflow.editor.shapes.CompileFlowScriptShape;
import cn.shanghai.oyb.jgraphx.geometry.Geometry;
import cn.shanghai.oyb.jgraphx.shape.IShape;

/**
 * @author ouyubin
 */
public class CompileFlowScriptCell extends CompileFlowElementCell {

    public CompileFlowScriptCell(Object value, Geometry geometry) {
        super(value, geometry);
        setConnectable(true);
    }

    /**
     * 构建形状
     *
     * @return
     */
    @Override
    public IShape getShape() {
        return new CompileFlowScriptShape(getValue());
    }


}
