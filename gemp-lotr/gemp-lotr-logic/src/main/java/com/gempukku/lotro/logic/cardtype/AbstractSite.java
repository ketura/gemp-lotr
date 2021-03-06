package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private int _siteNumber;
    private SitesBlock _block;
    private Direction _siteDirection;

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
