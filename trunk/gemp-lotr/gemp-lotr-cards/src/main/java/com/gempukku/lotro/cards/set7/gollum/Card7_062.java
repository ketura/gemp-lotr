package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Shadow: Remove (1) and exert Gollum to discard the top card of your draw deck. If that card is a [GOLLUM]
 * or [WRAITH] card, take it into hand.
 */
public class Card7_062 extends AbstractPermanent {
    public Card7_062() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "It's Mine");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 1)
                && PlayConditions.canExert(self, game, Filters.gollum)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollum));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                final Culture culture = card.getBlueprint().getCulture();
                                if (culture == Culture.GOLLUM || culture == Culture.WRAITH)
                                    action.appendEffect(
                                            new PutCardFromDeckIntoHandOrDiscardEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
