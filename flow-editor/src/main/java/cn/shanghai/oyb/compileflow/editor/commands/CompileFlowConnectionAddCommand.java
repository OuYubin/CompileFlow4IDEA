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
import com.intellij.psi.xml.XmlTag;
import cn.shanghai.oyb.flow.core.editor.commands.AbstractGraphEditCommand;
import cn.shanghai.oyb.flow.core.editor.editpart.TGraphEditPart;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.ID_ATTR_NAME;
import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.TO_ATTR_NAME;

/**
 * 流程节点连接添加
 *
 * @author ouyubin
 */
public class CompileFlowConnectionAddCommand extends AbstractGraphEditCommand {

    static private final Logger LOG = Logger.getInstance(CompileFlowConnectionAddCommand.class);

    static private final String TRANSITION_NAME = "transition";

    private BaseCell source;

    private BaseCell target;

    public CompileFlowConnectionAddCommand(TAdaptable adapter, String commandName) {
        super(adapter,commandName);
    }

    public BaseCell getSource() {
        return source;
    }

    public void setSource(BaseCell source) {
        this.source = source;
    }

    public BaseCell getTarget() {
        return target;
    }

    public void setTarget(BaseCell target) {
        this.target = target;
    }

    @Override
    public void execute() {
        TGraphEditPart sourceEditPart = source.getMyEditPart();
        XmlTagModelElement sourceXmlTagElement = ((XmlTagModelElement) sourceEditPart.getModel());
        XmlTag sourceXmlTag = sourceXmlTagElement.getXmlTag();
        LOG.info("获取到的起点标签" + sourceXmlTag.getName());

        TGraphEditPart targetEditPart = target.getMyEditPart();
        XmlTagModelElement targetXmlTagElement = ((XmlTagModelElement) targetEditPart.getModel());
        XmlTag targetXmlTag = targetXmlTagElement.getXmlTag();
        LOG.info("获取到的终点标签" + targetXmlTag.getName());

        String transitionName = TRANSITION_NAME;
        for (int i = 1; findTransitionTag(sourceXmlTag, transitionName); i++) {
            transitionName = TRANSITION_NAME + i;
        }

        XmlTag transitionXmlTag = sourceXmlTag.createChildTag(CompileFlowXmlTagConstants.TRANSITION_TAG_NAME, null, null, false);
        transitionXmlTag.setAttribute(TO_ATTR_NAME, targetXmlTag.getAttributeValue(ID_ATTR_NAME));

        sourceXmlTag.addSubTag(transitionXmlTag, false);
        sourceXmlTagElement.addSourceWire();
        targetXmlTagElement.addTargetWire();
    }

    private boolean findTransitionTag(XmlTag parentXmlTag, String transitionName) {
        return Arrays.stream(parentXmlTag.findSubTags(CompileFlowXmlTagConstants.TRANSITION_TAG_NAME)).filter(xmlTag -> StringUtils.equals(xmlTag.getAttributeValue(ID_ATTR_NAME), transitionName)).count() > 0;

    }


}
