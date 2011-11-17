package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Discard 3 cards from hand to play a [MORIA] Orc from your discard
 * pile.
 */
public class Card1_196 extends AbstractPermanent {
    public Card1_196() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "They Are Coming");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && game.getGameState().getHand(playerId).size() >= 3
                // You have to be able to play a MORIA Orc from discard to use it
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.MORIA, Filters.race(Race.ORC), Filters.playable(game)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.and(Culture.MORIA, Filters.race(Race.ORC))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
