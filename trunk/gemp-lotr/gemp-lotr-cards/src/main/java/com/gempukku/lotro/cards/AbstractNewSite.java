package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Block;

public abstract class AbstractNewSite extends AbstractSite {
    public AbstractNewSite(String name, int twilight, Direction direction) {
        super(name, Block.SHADOWS, 0, twilight, direction);
    }
}
