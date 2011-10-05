package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardVisitor;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.List;

public class PlayConditions {
    public static boolean nonPlayZone(Zone zone) {
        return zone != Zone.SHADOW_CHARACTERS && zone != Zone.SUPPORT
                && zone != Zone.SUPPORT && zone != Zone.FREE_CHARACTERS
                && zone != Zone.ATTACHED && zone != Zone.ADVENTURE_PATH;
    }

    public static boolean canPayForShadowCard(LotroGame game, PhysicalCard self, int twilightModifier) {
        return game.getModifiersQuerying().getTwilightCost(game.getGameState(), self) + twilightModifier <= game.getGameState().getTwilightPool();
    }

    private static boolean containsPhase(Phase[] phases, Phase phase) {
        for (Phase phase1 : phases) {
            if (phase1 == phase)
                return true;
        }
        return false;
    }

    public static boolean canPlayCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase)
                && self.getZone() == Zone.HAND
                && (!self.getBlueprint().isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName())));
    }

    public static boolean canPlayCardDuringPhase(LotroGame game, Phase[] phases, PhysicalCard self) {
        return (phases == null || containsPhase(phases, game.getGameState().getCurrentPhase()))
                && self.getZone() == Zone.HAND
                && (!self.getBlueprint().isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName())));
    }

    public static boolean canUseFPCardDuringPhase(GameState gameState, Phase phase, PhysicalCard self) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (self.getZone() == Zone.SUPPORT || self.getZone() == Zone.FREE_CHARACTERS || self.getZone() == Zone.ATTACHED);
    }

    public static boolean canUseShadowCardDuringPhase(GameState gameState, Phase phase, PhysicalCard self, int twilightCost) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (self.getZone() == Zone.SUPPORT || self.getZone() == Zone.SHADOW_CHARACTERS || self.getZone() == Zone.ATTACHED)
                && twilightCost <= gameState.getTwilightPool();
    }

    public static boolean canUseStackedShadowCardDuringPhase(GameState gameState, Phase phase, PhysicalCard self, int twilightCost) {
        return (phase == null || gameState.getCurrentPhase() == phase) && self.getZone() == Zone.STACKED
                && twilightCost <= gameState.getTwilightPool();
    }

    public static boolean canUseSiteDuringPhase(GameState gameState, Phase phase, PhysicalCard self) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (gameState.getCurrentSite() == self);
    }

    public static boolean checkUniqueness(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        return (!blueprint.isUnique()
                || (
                !Filters.canSpot(gameState, modifiersQuerying, Filters.name(blueprint.getName()))
                        && (Filters.filter(gameState.getDeadPile(self.getOwner()), gameState, modifiersQuerying, Filters.name(blueprint.getName())).size() == 0)));
    }

    private static int getTotalCompanions(String playerId, GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.countActive(gameState, modifiersQuerying, Filters.type(CardType.COMPANION))
                + Filters.filter(gameState.getDeadPile(playerId), gameState, modifiersQuerying, Filters.type(CardType.COMPANION)).size();
    }

    public static boolean checkRuleOfNine(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return (getTotalCompanions(self.getOwner(), gameState, modifiersQuerying) < 9);
    }

    public static boolean canHealByDiscarding(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        if (self.getZone() == Zone.HAND
                && (blueprint.getCardType() == CardType.COMPANION || blueprint.getCardType() == CardType.ALLY)
                && gameState.getCurrentPhase() == Phase.FELLOWSHIP
                && blueprint.isUnique()) {
            PhysicalCard matchingName = Filters.findFirstActive(gameState, modifiersQuerying, Filters.name(blueprint.getName()));
            if (matchingName != null)
                return gameState.getWounds(matchingName) > 0;
        }
        return false;
    }

    public static boolean canExert(final PhysicalCard source, final GameState gameState, final ModifiersQuerying modifiersQuerying, Filter... filters) {
        return canExert(source, gameState, modifiersQuerying, 1, filters);
    }

    public static boolean canExertMultiple(final PhysicalCard source, final GameState gameState, final ModifiersQuerying modifiersQuerying, final int times, final int count, Filter... filters) {
        final Filter filter = Filters.and(filters);
        return gameState.iterateActiveCards(
                new PhysicalCardVisitor() {
                    private int _exertableCount;

                    @Override
                    public boolean visitPhysicalCard(PhysicalCard physicalCard) {
                        if (filter.accepts(gameState, modifiersQuerying, physicalCard)
                                && (modifiersQuerying.getVitality(gameState, physicalCard) > times)
                                && modifiersQuerying.canBeExerted(gameState, source, physicalCard))
                            _exertableCount++;
                        return _exertableCount >= count;
                    }
                });
    }

    public static boolean canExert(final PhysicalCard source, final GameState gameState, final ModifiersQuerying modifiersQuerying, final int times, Filter... filters) {
        final Filter filter = Filters.and(filters);
        return gameState.iterateActiveCards(
                new PhysicalCardVisitor() {
                    @Override
                    public boolean visitPhysicalCard(PhysicalCard physicalCard) {
                        return filter.accepts(gameState, modifiersQuerying, physicalCard)
                                && (modifiersQuerying.getVitality(gameState, physicalCard) > times)
                                && modifiersQuerying.canBeExerted(gameState, source, physicalCard);
                    }
                });
    }

    public static boolean canExert(PhysicalCard source, GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return canExert(source, gameState, modifiersQuerying, Filters.sameCard(card));
    }

    public static boolean controllsSite(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        return Filters.findFirstActive(gameState, modifiersQuerying, Filters.siteControlled(playerId)) != null;
    }

    public static boolean winsSkirmish(EffectResult effectResult, PhysicalCard character) {
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            List<PhysicalCard> winners = skirmishResult.getWinners();
            return winners.contains(character);
        }
        return false;
    }

    public static boolean winsSkirmish(GameState gameState, ModifiersQuerying modifiersQuerying, EffectResult effectResult, Filter filter) {
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            return (Filters.filter(skirmishResult.getWinners(), gameState, modifiersQuerying, filter).size() > 0);
        }
        return false;
    }

    public static boolean losesSkirmish(GameState gameState, ModifiersQuerying modifiersQuerying, EffectResult effectResult, Filter filter) {
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            return (Filters.filter(skirmishResult.getLosers(), gameState, modifiersQuerying, filter).size() > 0);
        }
        return false;
    }

    public static boolean isWounded(EffectResult effectResult, PhysicalCard character) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            return ((WoundResult) effectResult).getWoundedCards().contains(character);
        }
        return false;
    }

    public static boolean isGettingWounded(Effect effect, LotroGame game, Filter... filters) {
        if (effect.getType() == EffectResult.Type.WOUND) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            return Filters.filter(woundEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean played(GameState gameState, ModifiersQuerying modifiersQuerying, EffectResult effectResult, Filter filter) {
        if (effectResult.getType() == EffectResult.Type.PLAY) {
            PhysicalCard playedCard = ((PlayCardResult) effectResult).getPlayedCard();
            return filter.accepts(gameState, modifiersQuerying, playedCard);
        }
        return false;
    }

    public static boolean played(GameState gameState, ModifiersQuerying modifiersQuerying, Effect effect, Filter filter) {
        if (effect.getType() == EffectResult.Type.PLAY) {
            PhysicalCard playedCard = ((PlayCardEffect) effect).getPlayedCard();
            return filter.accepts(gameState, modifiersQuerying, playedCard);
        }
        return false;
    }
}
