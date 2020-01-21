package com.gempukku.lotro.images.recipe;

import java.awt.*;
import java.util.function.Function;

public class ImageLayerRecipe implements LayerRecipe {
    private Function<RenderContext, Image> imageProvider;
    private Function<RenderContext, Integer> leftProvider;
    private Function<RenderContext, Integer> topProvider;
    private Function<RenderContext, Integer> widthProvider;
    private Function<RenderContext, Integer> heightProvider;

    public ImageLayerRecipe(
            Function<RenderContext, Image> imageProvider,
            Function<RenderContext, Integer> leftProvider,
            Function<RenderContext, Integer> topProvider,
            Function<RenderContext, Integer> widthProvider,
            Function<RenderContext, Integer> heightProvider) {

        this.imageProvider = imageProvider;
        this.leftProvider = leftProvider;
        this.topProvider = topProvider;
        this.widthProvider = widthProvider;
        this.heightProvider = heightProvider;
    }

    @Override
    public void renderLayer(RenderContext renderContext, Graphics2D graphics) {
        graphics.drawImage(
                imageProvider.apply(renderContext),
                leftProvider.apply(renderContext),
                topProvider.apply(renderContext),
                widthProvider.apply(renderContext),
                heightProvider.apply(renderContext),
                null);
    }
}
