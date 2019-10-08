package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.function.Function;

public class TextBoxLayerRecipe implements LayerRecipe {
    private Function<String, Function<RenderContext, Font>> fontStyleProvider;
    private Function<String, String> glyphProvider;
    private Function<RenderContext, String[]> text;
    private Function<RenderContext, TextBox> textBox;

    public TextBoxLayerRecipe(
            Function<String, Function<RenderContext, Font>> fontStyleProvider,
            Function<String, String> glyphProvider,
            Function<RenderContext, String[]> text,
            Function<RenderContext, TextBox> textBox) {
        this.fontStyleProvider = fontStyleProvider;
        this.glyphProvider = glyphProvider;
        this.text = text;
        this.textBox = textBox;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        final String[] textLines = text.apply(renderContext);

        AttributedCharacterIterator[] iteratorLines = new AttributedCharacterIterator[textLines.length];
        for (int i = 0; i < textLines.length; i++) {
            iteratorLines[i] = createAttributedCharacterIterator(textLines[i], renderContext);
        }

        final TextBox box = textBox.apply(renderContext);
//        graphics.setPaint(Color.WHITE);
//        graphics.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

        Point2D.Float pen = new Point2D.Float(box.getX(), box.getY());
        FontRenderContext frc = graphics.getFontRenderContext();

        for (AttributedCharacterIterator styledText : iteratorLines) {
            // let styledText be an AttributedCharacterIterator containing at least
            // one character

            LineBreakMeasurer measurer = new LineBreakMeasurer(styledText, frc);
            float wrappingWidth = box.getWidth();

            while (measurer.getPosition() < styledText.getEndIndex()) {
                TextLayout layout = measurer.nextLayout(wrappingWidth);

                pen.y += (layout.getAscent());
                float dx = layout.isLeftToRight() ?
                        0 : (wrappingWidth - layout.getAdvance());

                layout.draw(graphics, pen.x + dx, pen.y);
                pen.y += layout.getDescent() + layout.getLeading();
            }
            // Paragraph break
            pen.y += 2;
        }
    }

    private AttributedCharacterIterator createAttributedCharacterIterator(String text, RenderContext renderContext) {
        StringBuilder resultText = new StringBuilder();
        parseText(text, (text1, style) -> resultText.append(text1));

        AttributedString string = new AttributedString(resultText.toString());
        parseText(text, new TextParsingCallback() {
            private int index = 0;

            @Override
            public void appendText(String text, String style) {
                String resultStyle = (style != null) ? style : "default";
                string.addAttribute(TextAttribute.FONT, fontStyleProvider.apply(resultStyle).apply(renderContext), index, index + text.length());
                string.addAttribute(TextAttribute.FOREGROUND, Color.BLACK, index, index + text.length());
                index += text.length();
            }
        });
        return string.getIterator();
    }

    private void parseText(String text, TextParsingCallback callback) {
        int index = 0;
        while (text.indexOf('[', index) > -1) {
            int openIndex = text.indexOf('[', index);
            if (openIndex > index) {
                String textBefore = text.substring(index, openIndex);
                internalAppendText(callback, textBefore, null);
            }
            int closeIndex = text.indexOf(']', openIndex);
            String styleName = text.substring(openIndex + 1, closeIndex);
            index = closeIndex + 1;
            openIndex = text.indexOf("[/", index);
            if (openIndex > index) {
                String textBefore = text.substring(index, openIndex);
                internalAppendText(callback, textBefore, styleName);
            }
            closeIndex = text.indexOf(']', openIndex);
            String closeStyleName = text.substring(openIndex + 2, closeIndex);
            if (!closeStyleName.equals(styleName))
                throw new ImageGenerationException("Opening and closing tag does not match " + styleName + "!=" + closeStyleName);
            index = closeIndex + 1;
        }
        String textAfter = text.substring(index);
        if (textAfter.length() > 0)
            internalAppendText(callback, textAfter, null);
    }

    private void internalAppendText(TextParsingCallback callback, String text, String style) {
        int index = 0;
        while (text.indexOf("{", index) > -1) {
            int openIndex = text.indexOf('{', index);
            if (openIndex > index) {
                String textBefore = text.substring(index, openIndex);
                callback.appendText(textBefore, style);
            }
            int closeIndex = text.indexOf('}', openIndex);
            String glyphName = text.substring(openIndex + 1, closeIndex);
            callback.appendText(glyphProvider.apply(glyphName), "glyph");
            index = closeIndex + 1;
        }
        String textAfter = text.substring(index);
        if (textAfter.length() > 0)
            callback.appendText(textAfter, style);
    }

    private interface TextParsingCallback {
        void appendText(String text, String style);
    }
}
