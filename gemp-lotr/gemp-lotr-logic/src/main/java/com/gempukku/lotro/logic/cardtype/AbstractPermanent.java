package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class AbstractPermanent extends AbstractLotroCardBlueprint {
    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, String name) {
        this(side, twilightCost, cardType, culture, name, null, false);
    }

    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, String name, String subTitle, boolean unique) {
        super(twilightCost, side, cardType, culture, name, subTitle, unique);
        if (cardType != CardType.COMPANION && cardType!= CardType.MINION)
            addKeyword(Keyword.SUPPORT_AREA);
    }

    @Override
    public final List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayBeforeActions(playerId, game, effect, self);
        return null;
    }

    @Override
    public final List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayAfterActions(playerId, game, effectResult, self);
        return null;
    }

    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }
}
