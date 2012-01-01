package com.gempukku.lotro.logic.modifiers;

public interface LimitCounter {
    public int incrementCounter();

    public int incrementToLimit(int limit, int incrementBy);
}
