package com.gempukku.lotro.images.recipe;

public interface TextBox {
    int getX();

    int getY();

    int getWidth();

    int getHeight();

    HorizontalAlignment getHorizontalAlignment();

    interface HorizontalAlignment {
        float getXShift(float availableWidth, float usedWidth);
    }
}
