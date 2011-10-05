package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private int _siteNumber;
    private Block _block;
    private int _twilight;
    private Direction _siteDirection;

    public AbstractSite(String name, Block block, int siteNumber, int twilight, Direction siteDirection) {
        super(Side.SITE, CardType.SITE, null, name);
        _block = block;
        _siteNumber = siteNumber;
        _twilight = twilight;
        _siteDirection = siteDirection;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return null;
    }

    @Override
    public Block getSiteBlock() {
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

    @Override
    public int getTwilightCost() {
        return _twilight;
    }
}
