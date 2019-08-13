package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Title: Crushing Blow
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1C8
 * Game Text: Make a Dwarf strength +2 (or +3 if you can spot a [DWARVEN] condition with a stacked card).
 */
public class Card40_008 extends AbstractEvent {
    public Card40_008() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Crushing Blow", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, self.getOwner(), new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return Filters.canSpot(game, Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any)) ? 3 : 2;
                    }
                }, Race.DWARF));
        return action;
    }
}
