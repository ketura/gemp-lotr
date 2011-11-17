package com.gempukku.lotro.cards.set2.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
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
public class Card2_020 extends AbstractOldEvent {
    public Card2_020() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Secret Sentinels", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.ELF, CardType.ALLY);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF, CardType.ALLY));
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.ORC))
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Do you wish to spot an Orc?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (result.equals("Yes")) {
                                        action.appendEffect(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, CardType.CONDITION));
                                    } else {
                                        action.appendEffect(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
                                    }
                                }
                            }));
        else
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));

        return action;
    }
}
