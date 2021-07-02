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

package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ouyubin
 */
public class CompileFlowIcons {

    public static Icon FLOW_FILE_ICON=load("/icons/compileflow.svg");
    public static Icon PALETTE_ICON=load("/icons/palette.png");
    public static Icon START_ICON=load("/icons/start.svg");
    public static Icon END_ICON=load("/icons/end.svg");
    public static Icon PROPERTIES_ICON=load("/icons/properties.svg");
    public static Icon DECISION_ICON=load("/icons/decision.png");
    public static Icon COMMON_ICON =load("/icons/common.svg");
    public static Icon TASK_ICON=load("/icons/task.svg");
    public static Icon ADD_ICON=load("/icons/add.svg");
    public static Icon REMOVE_ICON=load("/icons/remove.svg");
    public static Icon NOTE_ICON=load("/icons/note.png");
    public static Icon CONTINUE_ICON=load("/icons/continue.png");
    public static Icon SCRIPT_TASK_ICON =load("/icons/scripttask.png");
    public static Icon BREAK_ICON=load("/icons/break.svg");
    public static Icon SUB_BPM_ICON=load("/icons/subbpm.png");
    public static Icon LOOP_PROCESS_ICON =load("/icons/loopprocess.svg");
    public static Icon CONNECTOR_ICON=load("/icons/connector.svg");
    public static Icon DELETE_ICON=load("/icons/delete.svg");
    public static Icon OTHERS_ICON=load("/icons/others.png");
    public static Icon ZOOM_IN_ICON=load("/icons/zoomin.svg");
    public static Icon ZOOM_OUT_ICON=load("/icons/zoomout.svg");
    public static Icon EXPORT_IMAGE_ICON=load("/icons/export.svg");
    public static Icon HOME_ICON=load("/icons/home.svg");
    public static Icon COPY_ICON=load("/icons/copy.png");
    public static Icon CUT_ICON=load("/icons/cut.png");
    public static Icon PASTE_ICON=load("/icons/paste.png");

    private static Icon load(String path) {
        return IconLoader.getIcon(path, CompileFlowIcons.class);
    }

    public static Icon load(File file) {
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return IconLoader.findIcon(url);
    }

}