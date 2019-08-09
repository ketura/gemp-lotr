package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
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
 * Game Text: Glorfindel may not be overwhelmed unless his strength is tripled.
 * Each time a Nazgul is assigned to skirmish Glorfindel, that Nazgul is prevented from being fierce until the regroup phase.
 */
public class Card40_047 extends AbstractCompanion{
    public Card40_047() {
        super(4, 9, 3, 9, Culture.ELVEN, Race.ELF, null, "Glorfindel", "Emissary of the Valar", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        OverwhelmedByMultiplierModifier modifier = new OverwhelmedByMultiplierModifier(self, self, 3);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, self, Race.NAZGUL)) {
            AssignAgainstResult assignedAgainst = (AssignAgainstResult) effectResult;
            PhysicalCard nazgul = assignedAgainst.getAssignedCard();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new RemoveKeywordModifier(self, nazgul, Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
