package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect;
import com.gempukku.lotro.logic.modifiers.SpotBurdensCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Skirmish: Make Gandalf strength +2 (or +4 if there are 4 or fewer burdens on the Ring-bearer).
 */
public class Card1_078 extends AbstractEvent {
    public Card1_078() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Mysterious Wizard", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect(
                        self, new ConditionEvaluator(4, 2, new SpotBurdensCondition(5)), Filters.gandalf));
        return action;
    }
}
