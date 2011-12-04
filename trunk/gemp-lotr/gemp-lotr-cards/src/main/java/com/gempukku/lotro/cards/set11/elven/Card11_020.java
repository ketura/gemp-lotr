package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make an Elf strength +2 (or +3 at a river site).
 */
public class Card11_020 extends AbstractEvent {
    public Card11_020() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "The Lady's Blessing", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new ConditionEvaluator(2, 3, new LocationCondition(Keyword.RIVER)), Race.ELF));
        return action;
    }
}
