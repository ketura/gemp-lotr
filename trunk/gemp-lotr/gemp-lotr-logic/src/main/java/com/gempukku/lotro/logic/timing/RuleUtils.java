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
        int normalArcheryTotal = Filters.countActive(game.getGameState(), game.getModifiersQuerying(),
                Filters.or(
                        CardType.COMPANION,
                        Filters.and(
                                CardType.ALLY,
                                Filters.or(
                                        Filters.and(
                                                Filters.allyAtHome,
                                                new Filter() {
                                                    @Override
                                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                        return !modifiersQuerying.isAllyPreventedFromParticipatingInArcheryFire(gameState, physicalCard);
                                                    }
                                                }),
                                        new Filter() {
                                            @Override
                                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                return modifiersQuerying.isAllyAllowedToParticipateInArcheryFire(gameState, physicalCard);
                                            }
                                        })
                        )
                ),
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.addsToArcheryTotal(gameState, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game.getGameState(), Side.FREE_PEOPLE, normalArcheryTotal);
    }

    public static int calculateShadowArcheryTotal(LotroGame game) {
        int normalArcheryTotal = Filters.countActive(game.getGameState(), game.getModifiersQuerying(),
                CardType.MINION,
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.addsToArcheryTotal(gameState, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game.getGameState(), Side.SHADOW, normalArcheryTotal);
    }

    public static int calculateMoveLimit(LotroGame game) {
        return game.getModifiersQuerying().getMoveLimit(game.getGameState(), 2);
    }

    public static int getFellowshipSkirmishStrength(LotroGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        PhysicalCard fpChar = skirmish.getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        final Evaluator overrideEvaluator = skirmish.getFpStrengthOverrideEvaluator();
        if (overrideEvaluator != null)
            return overrideEvaluator.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), fpChar);

        return game.getModifiersQuerying().getStrength(game.getGameState(), fpChar);
    }

    public static int getShadowSkirmishStrength(LotroGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        final Evaluator overrideEvaluator = skirmish.getShadowStrengthOverrideEvaluator();
        if (overrideEvaluator != null) {
            int total = 0;
            for (PhysicalCard physicalCard : skirmish.getShadowCharacters())
                total += overrideEvaluator.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), physicalCard);
            return total;
        }

        int totalStrength = 0;
        for (PhysicalCard physicalCard : skirmish.getShadowCharacters())
            totalStrength += game.getModifiersQuerying().getStrength(game.getGameState(), physicalCard);

        return totalStrength;
    }

    public static int getFellowshipSkirmishDamageBonus(LotroGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        PhysicalCard fpChar = game.getGameState().getSkirmish().getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        return game.getModifiersQuerying().getKeywordCount(game.getGameState(), fpChar, Keyword.DAMAGE);
    }

    public static int getShadowSkirmishDamageBonus(LotroGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        int totalBonus = 0;

        for (PhysicalCard physicalCard : game.getGameState().getSkirmish().getShadowCharacters())
            totalBonus += game.getModifiersQuerying().getKeywordCount(game.getGameState(), physicalCard, Keyword.DAMAGE);

        return totalBonus;
    }
}
