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

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ouyubin
 */
public class CompileFlowSvgImages {

    public static BufferedImage load(URL url,float width,float height) {
        BufferedImage image = null;
        try {
            CompileFlowImageTranscoder imageTranscoder = new CompileFlowImageTranscoder();
            TranscodingHints hints = new TranscodingHints();
            hints.put(ImageTranscoder.KEY_WIDTH, width);
            hints.put(ImageTranscoder.KEY_HEIGHT, height);
            hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION,
                    SVGDOMImplementation.getDOMImplementation());
            hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
            hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
            hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
            imageTranscoder.setTranscodingHints(hints);
            imageTranscoder.transcode(new TranscoderInput(url.openStream()), null);
            image = imageTranscoder.getImage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (TranscoderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage load(String path,float width,float height) {
        URL url = CompileFlowSvgImages.class.getResource(path);
        return load(url,width,height);
    }

    public static final BufferedImage BAN_IMAGE = load("/icons/ban.svg",16.0F,16.0F);

    public static final BufferedImage CONNECTOR_IMAGE = load("/icons/connector.svg",16.0F,16.0F);

    public static final BufferedImage AUTO_TASK_IMAGE = load("/icons/task.svg",16.0F,16.0F);

    public static final BufferedImage BREAK_IMAGE = load("/icons/break.svg",16.0F,16.0F);

    public static final BufferedImage LOOP_IMAGE = load("/icons/loopprocess.svg",16.0F,16.0F);
}
