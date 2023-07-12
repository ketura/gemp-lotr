package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.UnrespondableEffect;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementEffect extends UnrespondableEffect {
    private final AtomicInteger _atomicInteger;

    public IncrementEffect(AtomicInteger atomicInteger) {
        _atomicInteger = atomicInteger;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        _atomicInteger.incrementAndGet();
    }
}
