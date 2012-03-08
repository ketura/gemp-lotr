package com.gempukku.lotro.cards.set18.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event • Assignment
 * Game Text: Remove a burden to choose one: assign a [WRAITH] minion to a companion who has resistance 0;
 * or have a [WRAITH] minion lose fierce and gain hunter 2 until the regroup phase.
 */
public class Card18_133 extends AbstractEvent {
    public Card18_133() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Pull of the Ring", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canRemoveBurdens(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RemoveBurdenEffect(playerId, self, 1));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose a WRAITH minion", Culture.WRAITH, CardType.MINION, Filters.assignableToSkirmishAgainst(Side.SHADOW, Filters.and(CardType.COMPANION, Filters.maxResistance(0)))) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard minion) {
                        action.appendEffect(
                                new ChooseAndAssignCharacterToMinionEffect(action, playerId, minion, CardType.COMPANION, Filters.maxResistance(0)));
                    }

                    @Override
                    public String getText(LotroGame game) {
                        return "Assign a WRAITH minion to a companion who has resistance 0";
                    }
                });
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose a WRAITH minion", Culture.WRAITH, CardType.MINION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard minion) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new RemoveKeywordModifier(self, minion, Keyword.FIERCE), Phase.REGROUP));
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, minion, Keyword.HUNTER, 2), Phase.REGROUP));
                    }

                    @Override
                    public String getText(LotroGame game) {
                        return "Make a WRAITH minion lose fierce and gain hunter 2 until the regroup phase";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
