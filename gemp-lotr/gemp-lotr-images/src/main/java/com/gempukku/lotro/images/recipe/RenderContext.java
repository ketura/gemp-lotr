package com.gempukku.lotro.images.recipe;

import org.json.simple.JSONObject;

import java.util.Properties;

public interface RenderContext {
    Properties getProperties();

    JSONObject getCardInfo();
}
