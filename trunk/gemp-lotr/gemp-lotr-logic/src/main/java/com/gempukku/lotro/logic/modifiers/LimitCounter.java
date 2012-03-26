package com.gempukku.lotro.logic.modifiers;

public interface LimitCounter {
    public int incrementToLimit(int limit, int incrementBy);

    public int getUsedLimit();
}
