package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class RuleUtils {
    public static int calculateFellowshipArcheryTotal(LotroGame game) {
        int normalArcheryTotal = Filters.countActive(game,
                Filters.or(
                        CardType.COMPANION,
                        Filters.and(
                                CardType.ALLY,
                                Filters.or(
                                        Filters.and(
                                                Filters.allyAtHome,
                                                new Filter() {
                                                    @Override
                                                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                                        return !game.getModifiersQuerying().isAllyPreventedFromParticipatingInArcheryFire(game, physicalCard);
                                                    }
                                                }),
                                        new Filter() {
                                            @Override
                                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                                return game.getModifiersQuerying().isAllyAllowedToParticipateInArcheryFire(game, physicalCard);
                                            }
                                        })
                        )
                ),
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return game.getModifiersQuerying().addsToArcheryTotal(game, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game, Side.FREE_PEOPLE, normalArcheryTotal);
    }

    public static int calculateShadowArcheryTotal(LotroGame game) {
        int normalArcheryTotal = Filters.countActive(game,
                CardType.MINION,
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return game.getModifiersQuerying().addsToArcheryTotal(game, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game, Side.SHADOW, normalArcheryTotal);
    }

    public static int calculateMoveLimit(LotroGame game) {
        return game.getModifiersQuerying().getMoveLimit(game, 2);
    }

    public static int getFellowshipSkirmishStrength(LotroGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        PhysicalCard fpChar = skirmish.getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        final Evaluator fpStrengthOverrideEvaluator = game.getModifiersQuerying().getFpStrengthOverrideEvaluator(game, fpChar);
        if (fpStrengthOverrideEvaluator != null)
            return fpStrengthOverrideEvaluator.evaluateExpression(game, fpChar);

        final Evaluator overrideEvaluator = skirmish.getFpStrengthOverrideEvaluator();
        if (overrideEvaluator != null)
            return overrideEvaluator.evaluateExpression(game, fpChar);

        return game.getModifiersQuerying().getStrength(game, fpChar);
    }

    public static int getShadowSkirmishStrength(LotroGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        final Evaluator overrideEvaluator = skirmish.getShadowStrengthOverrideEvaluator();
        if (overrideEvaluator != null) {
            int total = 0;
            for (PhysicalCard physicalCard : skirmish.getShadowCharacters())
                total += overrideEvaluator.evaluateExpression(game, physicalCard);
            return total;
        }

        int totalStrength = 0;
        for (PhysicalCard physicalCard : skirmish.getShadowCharacters())
            totalStrength += game.getModifiersQuerying().getStrength(game, physicalCard);

        return totalStrength;
    }

    public static int getFellowshipSkirmishDamageBonus(LotroGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        PhysicalCard fpChar = game.getGameState().getSkirmish().getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        return game.getModifiersQuerying().getKeywordCount(game, fpChar, Keyword.DAMAGE);
    }

    public static int getShadowSkirmishDamageBonus(LotroGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        int totalBonus = 0;

        for (PhysicalCard physicalCard : game.getGameState().getSkirmish().getShadowCharacters())
            totalBonus += game.getModifiersQuerying().getKeywordCount(game, physicalCard, Keyword.DAMAGE);

        return totalBonus;
    }
}
