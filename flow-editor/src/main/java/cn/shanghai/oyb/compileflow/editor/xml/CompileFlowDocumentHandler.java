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

package cn.shanghai.oyb.compileflow.editor.xml;

import cn.shanghai.oyb.compileflow.editor.xml.codec.CompileFlowModelCodec;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.xml.XmlFile;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;


/**
 * 组件文档处理对象
 *
 * @author ouyubin
 */
public class CompileFlowDocumentHandler {

    private static final Logger LOG = Logger.getInstance(CompileFlowDocumentHandler.class);

    private XmlFile xmlFile;

    private TAdaptable adapter;

    private CompileFlowModelCodec myFlowModelCodec;

    public CompileFlowDocumentHandler(TAdaptable adapter, XmlFile xmlFile) {
        this.xmlFile = xmlFile;
        this.adapter = adapter;
        //this.myDocument = (Document) FileDocumentManager.getInstance().getDocument(xmlFile);
    }


    public void readFromFile() {
        //LFTCommonGraph graph = (LFTCommonGraph) adapter.getAdapter(LFTMXGraph.class);
        //--构建自定义编码器
        myFlowModelCodec = new CompileFlowModelCodec(adapter);
        myFlowModelCodec.decode(xmlFile);
    }

    public CompileFlowModelCodec getComponentCodec() {
        return myFlowModelCodec;
    }
}
