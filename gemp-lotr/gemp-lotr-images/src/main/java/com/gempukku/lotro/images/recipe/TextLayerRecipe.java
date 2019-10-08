package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.util.function.Function;

public class TextLayerRecipe implements LayerRecipe {
    private Function<RenderContext, Font> fontProvider;
    private Function<RenderContext, String> textProvider;
    private Function<RenderContext, Paint> paintProvider;
    private Function<RenderContext, TextBox> textBoxProvider;

    public TextLayerRecipe(Function<RenderContext, Font> fontProvider, Function<RenderContext, String> textProvider, Function<RenderContext, Paint> paintProvider, Function<RenderContext, TextBox> textBoxProvider) {
        this.fontProvider = fontProvider;
        this.textProvider = textProvider;
        this.paintProvider = paintProvider;
        this.textBoxProvider = textBoxProvider;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        final String text = textProvider.apply(renderContext);
        if (text != null) {
            final Font font = fontProvider.apply(renderContext);
            graphics.setFont(font);
            graphics.setPaint(paintProvider.apply(renderContext));

            final FontMetrics fontMetrics = graphics.getFontMetrics();

            final TextBox textBox = textBoxProvider.apply(renderContext);

//            graphics.setPaint(Color.WHITE);
//            graphics.fillRect(textBox.getX(), textBox.getY(), textBox.getWidth(), textBox.getHeight());

            final String horizontalAlignment = textBox.getHorizontalAlignment();
            if (horizontalAlignment == null) {
                graphics.drawString(text, textBox.getX(), textBox.getY() + fontMetrics.getAscent());
            } else if (horizontalAlignment.equals("center")) {
                final int width = fontMetrics.stringWidth(text);
                graphics.drawString(text, textBox.getX() + (textBox.getWidth() - width) / 2, textBox.getY() + fontMetrics.getAscent());
            } else {
                throw new ImageGenerationException("Unable to recognize horizontal alignment: " + horizontalAlignment);
            }
        }
    }
}
