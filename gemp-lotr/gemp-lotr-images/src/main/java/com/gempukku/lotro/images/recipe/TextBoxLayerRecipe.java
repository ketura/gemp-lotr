package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.function.Function;

public class TextBoxLayerRecipe implements LayerRecipe {
    private Function<String, Function<RenderContext, Font>> fontStyleProvider;
    private Function<String, String> glyphProvider;
    private Function<RenderContext, Paint> paint;
    private Function<String, Float> yShifts;
    private Function<RenderContext, String[]> text;
    private Function<RenderContext, TextBox> textBox;
    private Function<RenderContext, Float> minYStart;

    public TextBoxLayerRecipe(
            Function<String, Function<RenderContext, Font>> fontStyleProvider,
            Function<String, String> glyphProvider,
            Function<RenderContext, Paint> paint,
            Function<String, Float> yShifts,
            Function<RenderContext, String[]> text,
            Function<RenderContext, TextBox> textBox,
            Function<RenderContext, Float> minYStart) {
        this.fontStyleProvider = fontStyleProvider;
        this.glyphProvider = glyphProvider;
        this.paint = paint;
        this.yShifts = yShifts;
        this.text = text;
        this.textBox = textBox;
        this.minYStart = minYStart;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        final String[] textLines = text.apply(renderContext);
        if (textLines.length > 0) {
            AttributedCharacterIterator[] iteratorLines = new AttributedCharacterIterator[textLines.length];
            for (int i = 0; i < textLines.length; i++) {
                iteratorLines[i] = createAttributedCharacterIterator(textLines[i], renderContext);
            }

            final TextBox box = textBox.apply(renderContext);

//            graphics.setPaint(Color.WHITE);
//            graphics.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
//            graphics.setColor(Color.BLACK);

            graphics.setPaint(paint.apply(renderContext));

            FontRenderContext frc = graphics.getFontRenderContext();

            // Dry run to check for height
            float textHeight = processText((layout, x, y) -> {
            }, iteratorLines, 0, 0, box.getWidth(), box.getHorizontalAlignment(), frc);

            final float boxPercUsed = textHeight / box.getHeight();
            float textAscent = Math.min(minYStart.apply(renderContext), (1 - boxPercUsed) / 2f) * box.getHeight();
            processText((layout, x, y) -> layout.draw(graphics, x, y), iteratorLines, box.getX(), box.getY() + textAscent, box.getWidth(), box.getHorizontalAlignment(), frc);
        }
    }

    private float processText(LineRenderingCallback callback, AttributedCharacterIterator[] iteratorLines, float x, float y, float width, TextBox.HorizontalAlignment horizontalAlignment, FontRenderContext frc) {
        Point2D.Float pen = new Point2D.Float(x, y);

        boolean firstParagraph = true;
        for (AttributedCharacterIterator styledText : iteratorLines) {
            // let styledText be an AttributedCharacterIterator containing at least
            // one character

            // Paragraph break
            if (!firstParagraph) {
                pen.y += 5;
            }

            LineBreakMeasurer measurer = new LineBreakMeasurer(styledText, frc);
            float wrappingWidth = width;

            while (measurer.getPosition() < styledText.getEndIndex()) {
                TextLayout layout = measurer.nextLayout(wrappingWidth);

                pen.y += (layout.getAscent());
                float xShift = horizontalAlignment.getXShift(wrappingWidth, layout.getAdvance());

                callback.drawLine(layout, pen.x + xShift, pen.y);
                pen.y += layout.getDescent() + layout.getLeading();
            }

            firstParagraph = false;
        }
        return pen.y;
    }

    private AttributedCharacterIterator createAttributedCharacterIterator(String text, RenderContext renderContext) {
        StringBuilder resultText = new StringBuilder();
        parseText(text, (text1, style) -> resultText.append(text1));

        AttributedString string = new AttributedString(resultText.toString());
        parseText(text, new TextParsingCallback() {
            private int index = 0;
            private float yShift = 0;

            @Override
            public void appendText(String text, String style) {
                String resultStyle = (style != null) ? style : "default";
                Font font = fontStyleProvider.apply(resultStyle).apply(renderContext);
                final float value = yShifts.apply(resultStyle);
                float result = value - yShift;
                if (result != 0) {
                    font = font.deriveFont(AffineTransform.getTranslateInstance(0, result));
                    yShift = value;
                }
                string.addAttribute(TextAttribute.FONT, font, index, index + text.length());
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

    private interface LineRenderingCallback {
        void drawLine(TextLayout layout, float x, float y);
    }
}
