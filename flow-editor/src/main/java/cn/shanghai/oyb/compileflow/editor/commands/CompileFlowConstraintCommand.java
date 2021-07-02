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

package cn.shanghai.oyb.compileflow.editor.commands;

import com.intellij.openapi.diagnostic.Logger;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.geometry.BaseGeometry;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.ModelMetaElement;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.graph.Graph;

/**
 * 移动图形命令
 *
 * @author ouyubin
 */
public class CompileFlowConstraintCommand extends AbstractGraphEditCommand {

    static private final Logger LOG = Logger.getInstance(CompileFlowConstraintCommand.class);

    private ModelMetaElement oldModelMetaElement;

    private BaseGeometry geometry;


    public CompileFlowConstraintCommand(TAdaptable adaptable, String commandName) {
        super(adaptable, commandName);
    }


    public boolean canExecute() {
        Object editPart = getEffectCell().getMyEditPart();
        if (editPart instanceof TGraphEditPart) {
            this.geometry = (BaseGeometry) getEffectCell().getGeometry().clone();
            this.oldModelMetaElement = ((XmlTagModelElement) ((TGraphEditPart) editPart).getModel()).getMeta();
        }
        return super.canExecute();
    }


    /**
     * 手动触发图形操作时动作需要自主持久化
     */
    @Override
    public void execute() {
        LOG.info("🧲开始执行定制大小命令...");
        double x = geometry.getX();
        double y = geometry.getY();
        double width = geometry.getWidth();
        double height = geometry.getHeight();
        Object object = getEffectCell().getValue();
        if (object instanceof XmlTagModelElement) {
            ModelMetaElement flowElementMeta = new ModelMetaElement(x, y, width, height);
            //--持久化写入
            ((XmlTagModelElement) object).setMeta(flowElementMeta, true);
        }
    }

    @Override
    public void undo() {
        double x = oldModelMetaElement.getX();
        double y = oldModelMetaElement.getY();
        double width = oldModelMetaElement.getWidth();
        double height = oldModelMetaElement.getHeight();
        Object object = getEffectCell().getValue();
        if (object instanceof XmlTagModelElement) {
            ModelMetaElement flowElementMeta = new ModelMetaElement(x, y, width, height);
            ((XmlTagModelElement) object).setMeta(flowElementMeta,false);
        }
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
    }

    @Override
    public void redo() {
        double x = geometry.getX();
        double y = geometry.getY();
        double width = geometry.getWidth();
        double height = geometry.getHeight();
        Object object = getEffectCell().getValue();
        if (object instanceof XmlTagModelElement) {
            ModelMetaElement flowElementMeta = new ModelMetaElement(x, y, width, height);
            ((XmlTagModelElement) object).setMeta(flowElementMeta,false);
        }
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
    }
}
