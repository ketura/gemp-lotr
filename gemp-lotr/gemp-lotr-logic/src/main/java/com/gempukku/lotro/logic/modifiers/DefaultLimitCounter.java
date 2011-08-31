package com.gempukku.lotro.logic.modifiers;

public class DefaultLimitCounter implements LimitCounter {
    private int _count;

    @Override
    public int incrementCounter() {
        _count++;
        return _count;
    }
}
