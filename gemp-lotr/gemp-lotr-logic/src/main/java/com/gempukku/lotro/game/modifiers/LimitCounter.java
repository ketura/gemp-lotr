package com.gempukku.lotro.game.modifiers;

public interface LimitCounter {
    public int incrementToLimit(int limit, int incrementBy);

    public int getUsedLimit();
}
