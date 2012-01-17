package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Shadow: Spot a Nazgul and reveal a Nazgul from hand to place the revealed card beneath your draw deck
 * and draw a card.
 */
public class Card11_213 extends AbstractPermanent {
    public Card11_213() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Moving This Way");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Race.NAZGUL)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, Race.NAZGUL) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.appendCost(
                                    new RevealCardsFromHandEffect(self, playerId, selectedCards));
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendCost(
                                        new PutCardsFromHandBeneathDrawDeckEffect(action, playerId, selectedCard));
                                action.appendEffect(
                                        new DrawCardsEffect(action, playerId, 1));
                            }

                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
