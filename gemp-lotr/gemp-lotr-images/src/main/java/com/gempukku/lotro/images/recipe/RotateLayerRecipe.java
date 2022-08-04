package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Function;

public class RotateLayerRecipe implements LayerRecipe {
    private final LayerRecipe target;
    private final Function<RenderContext, Integer> angle;
    private final Function<RenderContext, Integer> x;
    private final Function<RenderContext, Integer> y;

    public RotateLayerRecipe(LayerRecipe target, Function<RenderContext, Integer> angle, Function<RenderContext, Integer> x, Function<RenderContext, Integer> y) {
        this.target = target;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        final AffineTransform originalTransform = graphics.getTransform();

        final AffineTransform rotateInstance = AffineTransform.getRotateInstance(
                Math.toRadians(angle.apply(renderContext)), x.apply(renderContext), y.apply(renderContext));
        graphics.setTransform(rotateInstance);
        target.renderLayer(renderContext, graphics);

        graphics.setTransform(originalTransform);
    }
}
