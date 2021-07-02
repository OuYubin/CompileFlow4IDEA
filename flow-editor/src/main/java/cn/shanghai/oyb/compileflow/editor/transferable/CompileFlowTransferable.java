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

package cn.shanghai.oyb.compileflow.editor.transferable;

import com.intellij.util.ArrayUtil;
import cn.shanghai.oyb.compileflow.model.CompileFlowXmlTagModelElement;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * @author ouyubin
 */
public class CompileFlowTransferable implements Transferable {

    private CompileFlowXmlTagModelElement[] myModelElements;

    public static CompileFlowElementDataFlavor myCompileFlowElementDataFlavor;

    static {
        try {
            myCompileFlowElementDataFlavor = new CompileFlowElementDataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + CompileFlowXmlTagModelElement[].class.getName() + "\"", null, CompileFlowXmlTagModelElement.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public CompileFlowTransferable(CompileFlowXmlTagModelElement[] modelElements) throws ClassNotFoundException {
        this.myModelElements = modelElements;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dataFlavors = new DataFlavor[1];
        dataFlavors[0] = myCompileFlowElementDataFlavor;
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ArrayUtil.contains(flavor, getTransferDataFlavors());
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return myModelElements;
    }
}
