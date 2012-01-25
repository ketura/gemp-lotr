package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Spell. Exert your [GANDALF] companion X times to make a minion strength -2 until the regroup phase, for
 * each time that [GANDALF] companion exerted.
 */
public class Card15_027 extends AbstractEvent {
    public Card15_027() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Be Gone!", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose your GANDALF companion", Filters.owner(playerId), Culture.GANDALF, CardType.COMPANION, Filters.canExert(self)) {
                    @Override
                    protected void cardSelected(final LotroGame game, final PhysicalCard card) {
                        int vitality = game.getModifiersQuerying().getVitality(game.getGameState(), card);
                        action.appendCost(
                                new PlayoutDecisionEffect(playerId,
                                        new IntegerAwaitingDecision(1, "Choose how many times you wish to exert that companion", 0, vitality - 1) {
                                            @Override
                                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                                int exertCount = getValidatedResult(result);
                                                action.insertCost(
                                                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, exertCount, card));
                                                for (int i = 0; i < exertCount; i++)
                                                    action.appendEffect(
                                                            new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION) {
                                                                @Override
                                                                protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                                                    action.appendEffect(
                                                                            new AddUntilStartOfPhaseModifierEffect(
                                                                                    new StrengthModifier(self, minion, -2), Phase.REGROUP));
                                                                }
                                                            });
                                            }
                                        }));
                    }
                });
        return action;
    }
}
