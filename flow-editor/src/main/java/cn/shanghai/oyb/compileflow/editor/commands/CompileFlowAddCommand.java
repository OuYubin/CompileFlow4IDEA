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

import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import cn.shanghai.oyb.compileflow.model.factory.CompileFlowModelElementFactory;
import cn.shanghai.oyb.compileflow.palette.model.CompileFlowNodePaletteItem;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.*;

/**
 * 元素添加命令,在进行正向命令操作时,直接利用自定义模型触发对XmlTag对象进行操作。
 * <p>
 * 在Redo,Undo操作时由于无法全面控制编辑器撤销/重做动作,当实际动作完成后，XmlTag结构已发生变化，
 * 所以不能对XmlTag对象再进行操作，而只是通知自定义模型进行触发即可。
 *
 * @author ouyubin
 */
public class CompileFlowAddCommand extends AbstractGraphEditCommand {

    private static final Logger LOG = Logger.getInstance(CompileFlowAddCommand.class);

    private Object value;

    private Point point;

    private XmlTagModelElement oldXmlTagModelElement;

    public CompileFlowAddCommand(TAdaptable adaptable, Object value, Point point, String commandName) {
        super(adaptable, commandName);
        this.value = value;
        this.point = point;
    }

    @Override
    public void execute() {
        Object object = getEffectCell().getValue();
        if (object instanceof XmlTagModelElement) {
            XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
            if (value instanceof CompileFlowNodePaletteItem) {
                String titleName = ((CompileFlowNodePaletteItem) value).getTitle();
                XmlTag childXmlTag = null;
                String name = null;
                switch (titleName) {
                    case START_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(START_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "开始");
                        break;
                    case AUTO_TASK_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(AUTO_TASK_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "任务");
                        XmlTag actionXmlTag = childXmlTag.createChildTag(CompileFlowXmlTagConstants.ACTION_TAG_NAME, null, null, false);
                        actionXmlTag.setAttribute("type", "java");
                        childXmlTag.addSubTag(actionXmlTag, false);
                        break;
                    case DECISION_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(DECISION_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        name = "选择";
                        for (int i = 1; findElement(xmlTag, DECISION_TAG_NAME, "name", name); i++) {
                            name = "选择" + i;
                        }
                        childXmlTag.setAttribute("name", name);
                        XmlTag decisionActionXmlTag = childXmlTag.createChildTag(CompileFlowXmlTagConstants.ACTION_TAG_NAME, null, null, false);
                        decisionActionXmlTag.setAttribute("type", "java");
                        childXmlTag.addSubTag(decisionActionXmlTag, false);
                        break;
                    case NOTE_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.NOTE_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("comment", "注释信息");
                        break;
                    case CONTINUE_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.CONTINUE_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "继续");
                        break;
                    case BREAK_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.BREAK_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "中止");
                        break;
                    case SCRIPT_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.SCRIPT_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "脚本任务");
                        break;
                    case SUB_BPM_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.SUB_BPM_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "子流程");
                        break;
                    case END_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.END_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "结束");
                        break;
                    case LOOP_PROCESS_TAG_NAME:
                        childXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.LOOP_PROCESS_TAG_NAME, null, null, false);
                        childXmlTag.setAttribute("g", point.getX() + "," + point.getY());
                        childXmlTag.setAttribute("name", "循环");
                        break;
                    default:
                        break;
                }
                if (childXmlTag != null) {
                    childXmlTag.setAttribute(ID_ATTR_NAME, String.valueOf(System.currentTimeMillis()));
                    XmlTag subXmlTag = ((XmlTagModelElement) object).addSubTag(childXmlTag, false);
                    CompileFlowModelElementFactory modelElementFactory = new CompileFlowModelElementFactory();
                    oldXmlTagModelElement = modelElementFactory.getModel(subXmlTag);
                    ((XmlTagModelElement) object).addChild(oldXmlTagModelElement);
                }
            }
        }
    }

    private boolean findElement(XmlTag parentXmlTag, String childTagName, String attributeName, String attributeValue) {
        XmlTag[] tags = parentXmlTag.findSubTags(childTagName);
        if (tags.length > 0) {
            for (XmlTag tag : tags) {
                String value = tag.getAttributeValue(attributeName);
                if (StringUtils.equals(value, attributeValue)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean canExecute() {
        return super.canExecute();
    }

    /**
     * 重做动作执行
     */
    @Override
    public void redo() {
        super.redo();
        XmlTagModelElement parentModelElement = (XmlTagModelElement) getEffectCell().getValue();
        parentModelElement.addChild(oldXmlTagModelElement);
    }

    /**
     * 撤销动作执行
     */
    @Override
    public void undo() {
        super.undo();
        XmlTagModelElement parentModelElement = (XmlTagModelElement) getEffectCell().getValue();
        parentModelElement.removeChild(oldXmlTagModelElement);
        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
        graph.clearSelection();
        setEffectCell(getEffectCell());
    }
}
