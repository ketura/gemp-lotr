package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert a ranger to make each roaming minion strength -3 until the regroup phase.
 */
public class Card1_104 extends AbstractEvent {
    public Card1_104() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Eregion's Trails", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.RANGER));
        action.appendEffect(
                new SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect(
                        self, new ConstantEvaluator(-3), Phase.REGROUP, CardType.MINION, Keyword.ROAMING));
        return action;
    }
}
