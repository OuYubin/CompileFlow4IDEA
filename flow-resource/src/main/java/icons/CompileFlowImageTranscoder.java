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


import com.intellij.util.JBHiDPIScaledImage;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

/**
 * @author ouyubin
 */
public class CompileFlowImageTranscoder extends ImageTranscoder {

    private BufferedImage myBufferImage = null;
    @Override
    public BufferedImage createImage(int width, int height) {
        myBufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return myBufferImage;
    }

    @Override
    public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) throws TranscoderException {

    }

    public BufferedImage getImage() {
        return myBufferImage;
    }
}
