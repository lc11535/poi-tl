/*
 * Copyright 2014-2020 Sayi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deepoove.poi.policy;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.deepoove.poi.data.ParagraphRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.ParagraphStyle;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.util.ParagraphUtils;
import com.deepoove.poi.util.StyleUtils;
import com.deepoove.poi.xwpf.XWPFParagraphWrapper;

public class ParagraphRenderPolicy extends AbstractRenderPolicy<ParagraphRenderData> {

    @Override
    protected boolean validate(ParagraphRenderData data) {
        return null != data && !data.getContents().isEmpty();
    }

    @Override
    protected void afterRender(RenderContext<ParagraphRenderData> context) {
        clearPlaceholder(context, false);
    }

    @Override
    public void doRender(RenderContext<ParagraphRenderData> context) throws Exception {
        Helper.renderParagraph(context.getRun(), context.getData());

    }

    public static class Helper {

        public static void renderParagraph(XWPFRun run, ParagraphRenderData data) throws Exception {
            renderParagraph(run, data, null);
        }

        public static void renderParagraph(XWPFRun run, ParagraphRenderData data,
                List<ParagraphStyle> defaultControlStyles) throws Exception {
            List<RenderData> contents = data.getContents();
            XWPFParagraph paragraph = (XWPFParagraph) run.getParent();
            if (null != defaultControlStyles) {
                defaultControlStyles.stream().forEach(style -> StyleUtils.styleParagraph(paragraph, style));
            }
            StyleUtils.styleParagraph(paragraph, data.getParagraphStyle());
            Style defaultTextStyle = null == data.getParagraphStyle() ? null
                    : data.getParagraphStyle().getDefaultTextStyle();
            XWPFParagraphWrapper parentContext = new XWPFParagraphWrapper(paragraph);
            for (RenderData content : contents) {
                XWPFRun fragment = parentContext.insertNewRun(ParagraphUtils.getRunPos(run));
                StyleUtils.styleRun(fragment, run);
                if (content instanceof TextRenderData) {
                    if (null != defaultControlStyles) {
                        defaultControlStyles.stream().forEach(style -> {
                            if (null != style) {
                                StyleUtils.styleRun(fragment, style.getDefaultTextStyle());
                            }
                        });
                    }
                    StyleUtils.styleRun(fragment, defaultTextStyle);
                    TextRenderPolicy.Helper.renderTextRun(fragment, content);
                } else if (content instanceof PictureRenderData) {
                    PictureRenderPolicy.Helper.renderPicture(fragment, (PictureRenderData) content);
                }
            }

        }
    }

}
