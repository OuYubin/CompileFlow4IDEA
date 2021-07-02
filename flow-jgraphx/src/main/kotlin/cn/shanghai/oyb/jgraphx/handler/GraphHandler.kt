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

package cn.shanghai.oyb.jgraphx.handler

import com.intellij.openapi.diagnostic.Logger
import com.mxgraph.swing.handler.mxGraphHandler
import com.mxgraph.swing.mxGraphComponent
import java.awt.dnd.DropTargetDragEvent
import javax.swing.TransferHandler

/**
 *
 * @author ouyubin
 *
 */
open class GraphHandler(graphComponent: mxGraphComponent) : mxGraphHandler(graphComponent) {

    val LOG: Logger = Logger.getInstance(GraphHandler::class.java)


    /**
     * 拖拽正在进行
     */
    override fun dragEnter(event: DropTargetDragEvent) {
        val component = getDropTarget(event)
        val th = component.transferHandler
        val isLocal = th is GraphTransferHandler && th.isLocalDrag

        canImport = if (isLocal) {
            true
        } else {
            graphComponent.isImportEnabled && th.canImport(component, event.currentDataFlavors)
        }

        if (canImport) {
            try {
                //val tfa = event.transferable
                LOG.info("数据传送对象...")
                event.acceptDrag(TransferHandler.COPY_OR_MOVE)
            } catch (ex: Exception) {
                LOG.error("数据传送失败...", ex)
            }

        } else {
            event.rejectDrag()
        }
    }
}