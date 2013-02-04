package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * They Are Coming
 * Condition • Support Area
 * Shadow: Discard 3 cards from hand to play a [Moria] Goblin from your discard pile.
 */
public class Card20_280 extends AbstractPermanent {
    public Card20_280() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "They Are Coming");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canDiscardFromHand(game, playerId, 3, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
