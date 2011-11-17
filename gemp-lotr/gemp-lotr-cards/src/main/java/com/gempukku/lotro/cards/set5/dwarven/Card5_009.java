package com.gempukku.lotro.cards.set5.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver: Stack the top card from your draw deck here (limit once per phase).
 * Maneuver: Spot a Dwarf and discard a card stacked on a [DWARVEN] condition to take a Free Peoples card stacked here
 * into hand.
 */
public class Card5_009 extends AbstractPermanent {
    public Card5_009() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "More to My Liking", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();

            {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendEffect(
                        new CheckLimitEffect(action, self, 1, Phase.MANEUVER,
                                new StackTopCardsFromDeckEffect(self, playerId, 1, self)));
                actions.add(action);
            }

            if (PlayConditions.canSpot(game, Race.DWARF)
                    && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any)) > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
                action.appendEffect(
                        new ChooseStackedCardsEffect(action, playerId, 1, 1, Filters.sameCard(self), Side.FREE_PEOPLE) {
                            @Override
                            protected void cardsChosen(Collection<PhysicalCard> stackedCards) {
                                for (PhysicalCard stackedCard : stackedCards)
                                    action.insertEffect(
                                            new PutCardFromStackedIntoHandEffect(stackedCard));
                            }
                        });
                actions.add(action);
            }

            return actions;
        }
        return null;
    }
}
