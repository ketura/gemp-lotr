package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Regroup: Spot a [MEN] minion in your discard pile and discard this minion to place that minion on your
 * [MEN] support area possession.
 */
public class Card17_050 extends AbstractMinion {
    public Card17_050() {
        super(2, 7, 1, 4, Race.MAN, Culture.MEN, "Stampeding Hillsman");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.MEN, CardType.MINION).size() > 0
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, Culture.MEN, CardType.MINION) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (final PhysicalCard card : cards) {
                                action.appendCost(
                                        new SelfDiscardEffect(self));
                                action.appendEffect(
                                        new ChooseActiveCardEffect(self, playerId, "Choose possession", Filters.owner(playerId), Culture.MEN, CardType.POSSESSION, Keyword.SUPPORT_AREA) {
                                            @Override
                                            protected void cardSelected(LotroGame game, PhysicalCard possession) {
                                                action.appendEffect(
                                                        new StackCardFromDiscardEffect(card, possession));
                                            }
                                        });
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
