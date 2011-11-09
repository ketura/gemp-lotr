package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class AbstractSite extends AbstractLotroCardBlueprint {
    private int _siteNumber;
    private Block _block;
    private int _twilight;
    private Direction _siteDirection;

    public AbstractSite(String name, Block block, int siteNumber, int twilight, Direction siteDirection) {
        super(null, CardType.SITE, null, name);
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
    public CostToEffectAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
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

    @Override
    public List<ActivateCardAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<ActivateCardAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }
}
