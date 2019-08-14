package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, "They Are Coming");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 3, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
