package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private int _siteNumber;
    private int _twilight;
    private Direction _siteDirection;

    public AbstractSite(String name, String blueprintId, int siteNumber, int twilight, Direction siteDirection) {
        super(Side.SITE, CardType.SITE, null, name, blueprintId);
        _siteNumber = siteNumber;
        _twilight = twilight;
        _siteDirection = siteDirection;
    }

    @Override
    public int getSiteNumber() {
        return _siteNumber;
    }

    @Override
    public Direction getSiteDirection() {
        return _siteDirection;
    }

    @Override
    public int getTwilightCost() {
        return _twilight;
    }
}
