package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Exert Gandalf to make a minion skirmishing an unbound companion strength -1 for each Gandalf signet
 * you spot.
 */
public class Card8_016 extends AbstractEvent {
    public Card8_016() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Let Us Not Tarry", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new NegativeEvaluator(new CountSpottableEvaluator(Signet.GANDALF)), CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)));
        return action;
    }
}
