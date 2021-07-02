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

package cn.shanghai.oyb.jgraphx.geometry

import com.mxgraph.model.mxGeometry

/**
 *集合图形对象
 *
 * @author ouyubin
 *
 *
 */
open class Geometry : mxGeometry {

    constructor(x: Double, y: Double, width: Double, height: Double) : super(x, y, width, height)

    /**
     * 用于相对位置图元描述
     */
    constructor(width: Double, height: Double):super(0.0,0.0,width,height)

}