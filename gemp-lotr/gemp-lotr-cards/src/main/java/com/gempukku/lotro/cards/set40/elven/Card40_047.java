package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Glorfindel, Emissary of the Valar
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Companion - Elf
 * Strength: 9
 * Vitality: 3
 * Resistance: 9
 * Card Number: 1R47
 * Game Text: To play, spot 3 Elves.
 * While skirmishing a Nazgul, Glorfindel may not be overwhelmed unless his strength is tripled.
 * Skirmish: Exert Glorfindel to make a Nazgul he is skirmishing lose fierce and unable to gain fierce until the regroup phase.
 */
public class Card40_047 extends AbstractCompanion {
    public Card40_047() {
        super(4, 9, 3, 9, Culture.ELVEN, Race.ELF, null, "Glorfindel", "Emissary of the Valar", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, 3, Race.ELF);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        OverwhelmedByMultiplierModifier modifier = new OverwhelmedByMultiplierModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Race.NAZGUL)), 3);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Nazgul", Race.NAZGUL, Filters.inSkirmishAgainst(self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new RemoveKeywordModifier(self, card, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
