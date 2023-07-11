package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.SitesBlock;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private final int _siteNumber;
    private final SitesBlock _block;
    private final Direction _siteDirection;

    public AbstractSite(String name, SitesBlock block, int siteNumber, int twilight, Direction siteDirection) {
        super(twilight, null, CardType.SITE, null, name);
        _block = block;
        _siteNumber = siteNumber;
        _siteDirection = siteDirection;
    }

    @Override
    public SitesBlock getSiteBlock() {
        return _block;
    }

    @Override
    public int getSiteNumber() {
        return _siteNumber;
    }

    @Override
    public Direction getSiteDirection() {
        return _siteDirection;
    }
}
