package com.gempukku.lotro.rules;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.modifiers.*;

public class StatModifiersRule {
    private final ModifiersLogic modifiersLogic;

    public StatModifiersRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new StrengthModifier(null, Filters.and(Filters.inPlay, Filters.character, Filters.hasAttached(Filters.any)), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
                                int sum = 0;
                                for (LotroPhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected)) {
                                    final int strength = attachedCard.getBlueprint().getStrength();
                                    if (strength <= 0 || modifiersLogic.appliesStrengthBonusModifier(game, attachedCard, cardAffected))
                                        sum += strength;
                                }

                                return sum;
                            }
                        }, true));
        modifiersLogic.addAlwaysOnModifier(
                new VitalityModifier(null, Filters.and(Filters.inPlay, Filters.character, Filters.hasAttached(Filters.any)),
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
                                int sum = 0;
                                for (LotroPhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getVitality();

                                return sum;
                            }
                        }, true));
        modifiersLogic.addAlwaysOnModifier(
                new ResistanceModifier(null, Filters.and(Filters.inPlay, Filters.character, Filters.hasAttached(Filters.any)), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
                                int sum = 0;
                                for (LotroPhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getResistance();

                                return sum;
                            }
                        }, true));
        modifiersLogic.addAlwaysOnModifier(
                new MinionSiteNumberModifier(null, Filters.and(Filters.inPlay, CardType.MINION, Filters.hasAttached(Filters.any)), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
                                int sum = 0;
                                for (LotroPhysicalCard attachedCard : game.getGameState().getAttachedCards(cardAffected))
                                    sum += attachedCard.getBlueprint().getSiteNumber();

                                return sum;
                            }
                        }, true));
    }
}
