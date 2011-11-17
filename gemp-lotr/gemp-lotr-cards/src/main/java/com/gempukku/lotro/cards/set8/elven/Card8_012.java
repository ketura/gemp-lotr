package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.RuleUtils;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Archery
 * Game Text: Exert an Elf companion and make the fellowship archery total -X (to a minimum of 0) to wound a minion
 * X times. You cannot use archery special abilities.
 */
public class Card8_012 extends AbstractEvent {
    public Card8_012() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Reckless We Rode", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF, CardType.COMPANION));
        int archeryTotal = RuleUtils.calculateFellowshipArcheryTotal(game);
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose X (reduce fellowship archery total)", 0, archeryTotal) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int reduce = getValidatedResult(result);
                                action.appendCost(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -reduce), Phase.ARCHERY));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, reduce, CardType.MINION));
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new AbstractModifier(self, null, null, ModifierEffect.ACTION_MODIFIER) {
                                                    @Override
                                                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                                                        if (performingPlayer.equals(playerId)
                                                                && action.getActionTimeword() == Phase.ARCHERY
                                                                && action.getActionSource() != null
                                                                && action.getActionSource().getZone().isInPlay())
                                                            return false;
                                                        return true;
                                                    }
                                                }, Phase.ARCHERY));
                            }
                        }));

        return action;
    }
}
