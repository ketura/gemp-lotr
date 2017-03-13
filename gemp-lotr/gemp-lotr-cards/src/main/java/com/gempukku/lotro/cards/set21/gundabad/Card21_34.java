package com.gempukku.lotro.cards.set21.gundabad;

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
 * Culture: Gundabad
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Discard 3 cards from hand to play an Orc from your discard
 * pile.
 */
public class Card21_34 extends AbstractPermanent {
    public Card21_34() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.GUNDABAD, Zone.SUPPORT, "Hatred Rekindled");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && game.getGameState().getHand(playerId).size() >= 3
                // You have to be able to play an Orc from discard to use it
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
