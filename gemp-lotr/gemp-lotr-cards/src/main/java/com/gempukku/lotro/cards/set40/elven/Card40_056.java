package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Might of the Last Homely House
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event - Skirmish
 * Card Number: 1C56
 * Game Text: Make a minion skirmishing an Elf strength -X, where X is the number of Rivendell allies you can spot.
 */
public class Card40_056 extends AbstractEvent {
    public Card40_056() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Might of the Last Homely House", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId, new MultiplyEvaluator(-1, new CountSpottableEvaluator(CardType.ALLY, Keyword.RIVENDELL)),
                        CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
        return action;
    }
}
