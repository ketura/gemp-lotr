package com.gempukku.lotro.rules;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.Skirmish;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RuleUtils {
    public static int calculateArcheryTotal(DefaultGame game, Side side) {
        if (side == Side.FREE_PEOPLE)
            return calculateFellowshipArcheryTotal(game);
        else
            return calculateShadowArcheryTotal(game);
    }

    public static int calculateFellowshipArcheryTotal(DefaultGame game) {
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
                                                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                                                        return !game.getModifiersQuerying().isAllyPreventedFromParticipatingInArcheryFire(game, physicalCard);
                                                    }
                                                }),
                                        new Filter() {
                                            @Override
                                            public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                                                return game.getModifiersQuerying().isAllyAllowedToParticipateInArcheryFire(game, physicalCard);
                                            }
                                        })
                        )
                ),
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        return game.getModifiersQuerying().addsToArcheryTotal(game, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game, Side.FREE_PEOPLE, normalArcheryTotal);
    }

    public static int calculateShadowArcheryTotal(DefaultGame game) {
        int normalArcheryTotal = Filters.countActive(game,
                CardType.MINION,
                Keyword.ARCHER,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        return game.getModifiersQuerying().addsToArcheryTotal(game, physicalCard);
                    }
                });

        return game.getModifiersQuerying().getArcheryTotal(game, Side.SHADOW, normalArcheryTotal);
    }

    public static int calculateMoveLimit(DefaultGame game) {
        return game.getModifiersQuerying().getMoveLimit(game, 2);
    }

    public static int getFellowshipSkirmishStrength(DefaultGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        LotroPhysicalCard fpChar = skirmish.getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        final Evaluator fpStrengthOverrideEvaluator = game.getModifiersQuerying().getFPStrengthOverrideEvaluator(game, fpChar);
        if (fpStrengthOverrideEvaluator != null)
            return fpStrengthOverrideEvaluator.evaluateExpression(game, fpChar);

        final Evaluator overrideEvaluator = skirmish.getFpStrengthOverrideEvaluator();
        if (overrideEvaluator != null)
            return overrideEvaluator.evaluateExpression(game, fpChar);

        return game.getModifiersQuerying().getStrength(game, fpChar);
    }

    public static int getShadowSkirmishStrength(DefaultGame game) {
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish == null)
            return 0;

        int total = 0;
        final Evaluator overrideEvaluator = skirmish.getShadowStrengthOverrideEvaluator();
        for (LotroPhysicalCard minion : skirmish.getShadowCharacters()) {
            final Evaluator modifierOverrideEvaluator = game.getModifiersQuerying().getShadowStrengthOverrideEvaluator(game, minion);
            if(modifierOverrideEvaluator != null) {
                total += modifierOverrideEvaluator.evaluateExpression(game, minion);
            }
            else if(overrideEvaluator != null) {
                total += overrideEvaluator.evaluateExpression(game, minion);
            }
            else {
                total += game.getModifiersQuerying().getStrength(game, minion);
            }
        }

        return total;
    }

    public static int getFellowshipSkirmishDamageBonus(DefaultGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        LotroPhysicalCard fpChar = game.getGameState().getSkirmish().getFellowshipCharacter();
        if (fpChar == null)
            return 0;

        return game.getModifiersQuerying().getKeywordCount(game, fpChar, Keyword.DAMAGE);
    }

    public static int getShadowSkirmishDamageBonus(DefaultGame game) {
        if (game.getGameState().getSkirmish() == null)
            return 0;

        int totalBonus = 0;

        for (LotroPhysicalCard physicalCard : game.getGameState().getSkirmish().getShadowCharacters())
            totalBonus += game.getModifiersQuerying().getKeywordCount(game, physicalCard, Keyword.DAMAGE);

        return totalBonus;
    }

    public static Filter getFullValidTargetFilter(String playerId, final DefaultGame game, final LotroPhysicalCard self) {
        final LotroCardBlueprint blueprint = self.getBlueprint();
        return Filters.and(blueprint.getValidTargetFilter(playerId, game, self),
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        final CardType thisType = blueprint.getCardType();
                        if (thisType == CardType.POSSESSION || thisType == CardType.ARTIFACT) {
                            final CardType targetType = physicalCard.getBlueprint().getCardType();
                            return targetType == CardType.COMPANION || targetType == CardType.ALLY
                                    || targetType == CardType.MINION;
                        }
                        return true;
                    }
                },
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard attachedTo) {
                        Set<PossessionClass> possessionClasses = blueprint.getPossessionClasses();
                        if (possessionClasses != null) {
                            for (PossessionClass possessionClass : possessionClasses) {
                                List<LotroPhysicalCard> attachedCards = game.getGameState().getAttachedCards(attachedTo);

                                Collection<LotroPhysicalCard> matchingClassPossessions = Filters.filter(attachedCards, game, Filters.or(CardType.POSSESSION, CardType.ARTIFACT), possessionClass);
                                if (matchingClassPossessions.size() > 1)
                                    return false;

                                boolean extraPossessionClass = self.getBlueprint().isExtraPossessionClass(game, self, attachedTo);
                                if (!extraPossessionClass && matchingClassPossessions.size() == 1) {
                                    final LotroPhysicalCard attachedPossession = matchingClassPossessions.iterator().next();
                                    if (!attachedPossession.getBlueprint().isExtraPossessionClass(game, attachedPossession, attachedTo))
                                        return false;
                                }
                            }
                        }
                        return true;
                    }
                });
    }

    public static boolean isAllyAtHome(LotroPhysicalCard ally, int siteNumber, SitesBlock siteBlock) {
        final SitesBlock allySiteBlock = ally.getBlueprint().getAllyHomeSiteBlock();
        final int[] allyHomeSites = ally.getBlueprint().getAllyHomeSiteNumbers();
        if (allySiteBlock != siteBlock)
            return false;
        for (int number : allyHomeSites)
            if (number == siteNumber)
                return true;
        return false;
    }

    public static boolean isAllyInRegion(LotroPhysicalCard ally, int regionNumber, SitesBlock siteBlock) {
        final SitesBlock allySiteBlock = ally.getBlueprint().getAllyHomeSiteBlock();
        final int[] allyHomeSites = ally.getBlueprint().getAllyHomeSiteNumbers();
        if (allySiteBlock != siteBlock)
            return false;
//        for (int number : allyHomeSites)
//            if (regionNumber == GameUtils.getRegion(number))
//                return true;
//        return false;

        return Arrays.stream(ally.getBlueprint().getAllyHomeSiteNumbers()).anyMatch(x -> regionNumber == LotroGameUtils.getRegion(x));
    }

}
