package com.gempukku.lotro.images;

import org.json.simple.JSONObject;

import java.awt.image.BufferedImage;
import java.util.Properties;

public interface ImageRecipe {
    int getWidth();

    int getHeight();

    void renderImage(Properties properties, JSONObject cardInfo, BufferedImage image);
}
