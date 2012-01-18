package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementEffect extends UnrespondableEffect {
    private AtomicInteger _atomicInteger;

    public IncrementEffect(AtomicInteger atomicInteger) {
        _atomicInteger = atomicInteger;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        _atomicInteger.incrementAndGet();
    }
}
