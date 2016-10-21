package com.gempukku.lotro.cards.set22.troll;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Main Deck
 * Side: Shadow
 * Culture: Troll
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Discard 2 cards from hand to play a minion from your discard
 * pile.
 */
public class Card21_34 extends AbstractPermanent {
    public Card21_34() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.TROLL, Zone.SUPPORT, "Troll Campfire", null, true);
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && game.getGameState().getHand(playerId).size() >= 2
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
