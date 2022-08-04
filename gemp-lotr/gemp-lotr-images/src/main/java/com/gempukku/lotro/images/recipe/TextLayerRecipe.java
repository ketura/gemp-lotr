package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.util.function.Function;

public class TextLayerRecipe implements LayerRecipe {
    private final Function<RenderContext, Font> fontProvider;
    private final Function<RenderContext, String> textProvider;
    private final Function<RenderContext, Paint> paintProvider;
    private final Function<RenderContext, TextBox> textBoxProvider;
    private final Function<RenderContext, Boolean> dropShadow;

    public TextLayerRecipe(Function<RenderContext, Font> fontProvider, Function<RenderContext, String> textProvider,
                           Function<RenderContext, Paint> paintProvider, Function<RenderContext, TextBox> textBoxProvider,
                           Function<RenderContext, Boolean> dropShadow) {
        this.fontProvider = fontProvider;
        this.textProvider = textProvider;
        this.paintProvider = paintProvider;
        this.textBoxProvider = textBoxProvider;
        this.dropShadow = dropShadow;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        final String text = textProvider.apply(renderContext);
        if (text != null) {
            final TextBox textBox = textBoxProvider.apply(renderContext);

//            graphics.setPaint(Color.WHITE);
//            graphics.fillRect(textBox.getX(), textBox.getY(), textBox.getWidth(), textBox.getHeight());
//            graphics.setColor(Color.BLACK);

            final Font font = fontProvider.apply(renderContext);
            graphics.setFont(font);

            final FontMetrics fontMetrics = graphics.getFontMetrics();

            int dropShadowDistance = 3;

            int yMargin = (textBox.getHeight() - fontMetrics.getHeight()) / 2;

            final TextBox.HorizontalAlignment horizontalAlignment = textBox.getHorizontalAlignment();

            if (dropShadow.apply(renderContext)) {
                graphics.setPaint(Color.BLACK);
                drawText(graphics, text, textBox, fontMetrics, yMargin, horizontalAlignment, dropShadowDistance);
            }
            graphics.setPaint(paintProvider.apply(renderContext));
            drawText(graphics, text, textBox, fontMetrics, yMargin, horizontalAlignment, 0);
        }
    }

    private void drawText(Graphics2D graphics, String text, TextBox textBox, FontMetrics fontMetrics, int yMargin, TextBox.HorizontalAlignment horizontalAlignment,
                          int distance) {
        final int width = fontMetrics.stringWidth(text);
        float xShift = horizontalAlignment.getXShift(textBox.getWidth(), width);

        graphics.drawString(text, xShift + distance + textBox.getX(), distance + textBox.getY() + yMargin + fontMetrics.getAscent());
    }
}
