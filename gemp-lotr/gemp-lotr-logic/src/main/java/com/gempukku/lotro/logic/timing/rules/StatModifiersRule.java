package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class StatModifiersRule {
    private ModifiersLogic modifiersLogic;

    public StatModifiersRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new StrengthModifier(null, Filters.character, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int sum = 0;
                                for (PhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getStrength();

                                return sum;
                            }
                        }, true));
        modifiersLogic.addAlwaysOnModifier(
                new VitalityModifier(null, Filters.character,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int sum = 0;
                                for (PhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getVitality();

                                return sum;
                            }
                        }, true));
        modifiersLogic.addAlwaysOnModifier(
                new ResistanceModifier(null, Filters.character, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int sum = 0;
                                for (PhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getResistance();

                                return sum;
                            }
                        }));
    }
}
