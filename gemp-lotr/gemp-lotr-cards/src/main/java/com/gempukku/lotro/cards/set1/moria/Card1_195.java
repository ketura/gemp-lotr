package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
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
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Remove (2) to play a [MORIA] possession from your discard pile.
 */
public class Card1_195 extends AbstractPermanent {
    public Card1_195() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Relics of Moria");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)
                // You have to be able to play the MORIA possession from your discard pile to use this card
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.POSSESSION), Filters.playable(game, 2)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.and(Filters.culture(Culture.MORIA), Filters.type(CardType.POSSESSION))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
