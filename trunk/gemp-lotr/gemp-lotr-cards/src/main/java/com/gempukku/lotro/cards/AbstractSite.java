package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private int _siteNumber;
    private int _twilight;
    private Direction _siteDirection;

    public AbstractSite(String name, int siteNumber, int twilight, Direction siteDirection) {
        super(Side.SITE, CardType.SITE, null, name);
        _siteNumber = siteNumber;
        _twilight = twilight;
        _siteDirection = siteDirection;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        return null;
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
