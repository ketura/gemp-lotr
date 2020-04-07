package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.SitesBlock;

public abstract class AbstractShadowsSite extends AbstractSite {
    public AbstractShadowsSite(String name, int twilight, Direction direction) {
        super(name, SitesBlock.SHADOWS, 0, twilight, direction);
    }
}
