package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.*;
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
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. To play, spot a [SAURON] Orc. Plays to your support area. Shadow: Remove (3) to reveal the top
 * card of your draw deck. If it is a [SAURON] card, take it into hand. Otherwise, discard it and one other card
 * from hand.
 */
public class Card1_242 extends AbstractPermanent {
    public Card1_242() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "The Dark Lord's Summons");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC));
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SHADOW);
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            if (cards.size() == 0) {
                                action.appendEffect(
                                        new ChooseAndDiscardCardsFromHandEffect(action, playerId));
                            } else {
                                PhysicalCard topCard = cards.get(0);
                                if (topCard.getBlueprint().getCulture() == Culture.SAURON)
                                    action.appendEffect(
                                            new PutCardFromDeckIntoHandOrDiscardEffect(topCard));
                                else {
                                    action.appendEffect(
                                            new DiscardCardFromDeckEffect(topCard));
                                    action.appendEffect(
                                            new ChooseAndDiscardCardsFromHandEffect(action, playerId));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
