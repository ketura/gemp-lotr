package com.gempukku.lotro.cards.set40.moria;

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
 * Title: They Are Coming
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Condition - Support Area
 * Card Number: 1C180
 * Game Text: Shadow: Discard 3 cards from hand to play a [MORIA] Goblin from your discard pile.
 */
public class Card40_180 extends AbstractPermanent {
    public Card40_180() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, "They Are Coming");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 3, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.and(Culture.MORIA, Race.GOBLIN)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
