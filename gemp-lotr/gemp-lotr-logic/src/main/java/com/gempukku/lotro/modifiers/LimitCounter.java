package com.gempukku.lotro.modifiers;

public interface LimitCounter {
    public int incrementToLimit(int limit, int incrementBy);

    public int getUsedLimit();
}
