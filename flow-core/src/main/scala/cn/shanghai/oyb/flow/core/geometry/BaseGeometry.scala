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

package cn.shanghai.oyb.flow.core.geometry

import cn.shanghai.oyb.jgraphx.geometry.Geometry


/**
  *几何信息对象缺省对象
 *
  * @author ouyubin
  * @param x
  * @param y
  * @param width
  * @param height
  */
class BaseGeometry(x: Double, y: Double, width: Double, height: Double) extends Geometry(x, y, width, height) {

  def this(width: Double, height: Double) {
    this(0.0, 0.0, width, height)
  }

}
