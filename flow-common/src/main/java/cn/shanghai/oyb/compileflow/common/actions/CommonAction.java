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

package cn.shanghai.oyb.compileflow.common.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import cn.shanghai.oyb.flow.core.editor.models.cells.BaseCell;
import cn.shanghai.oyb.flow.core.internal.TAdaptable;
import cn.shanghai.oyb.jgraphx.graph.Graph;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.RefreshQueue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyubin
 */
public class CommonAction extends AnAction {

    public static final String COMMON_ACTION_PLACE = "COMMON_ACTION";

    public static final DataKey<List<BaseCell>> DEL_KEY = DataKey.create("DEL");

    public static final DataKey<List<BaseCell>> CUT_KEY = DataKey.create("CUT");

    public static final DataKey<List<BaseCell>> COPY_KEY = DataKey.create("COPY");

    public static final DataKey<List<BaseCell>> PASTE_KEY = DataKey.create("PASTE");


    private BaseCell myCell;

    private TAdaptable myAdapter;


    public CommonAction(BaseCell cell, TAdaptable adapter, String text, String description, Icon icon) {
        super(text, description, icon);
        this.myCell = cell;
        this.myAdapter = adapter;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFileFile = (VirtualFile) getAdapter().getAdapter(VirtualFile.class);
        List<VirtualFile> files = new ArrayList<>();
        files.add(virtualFileFile);
        RefreshQueue.getInstance().refresh(true, true, null, files);
//        Graph graph = (Graph) getAdapter().getAdapter(Graph.class);
//        graph.clearSelection();
    }

    public BaseCell getCell() {
        return myCell;
    }

    public TAdaptable getAdapter() {
        return myAdapter;
    }

}
