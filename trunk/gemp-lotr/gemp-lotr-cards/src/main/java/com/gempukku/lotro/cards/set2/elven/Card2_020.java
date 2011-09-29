package com.gempukku.lotro.cards.set2.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Exert an Elf ally to discard a condition (or 2 conditions if you spot an Orc).
 */
public class Card2_020 extends AbstractEvent {
    public Card2_020() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Secret Sentinels", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.type(CardType.ALLY));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.ELF), Filters.type(CardType.ALLY)));
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ORC)))
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Do you wish to spot an Orc?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (result.equals("Yes")) {
                                        action.appendEffect(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Filters.type(CardType.CONDITION)));
                                    } else {
                                        action.appendEffect(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.type(CardType.CONDITION)));
                                    }
                                }
                            }));
        else
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.type(CardType.CONDITION)));

        return action;
    }
}
