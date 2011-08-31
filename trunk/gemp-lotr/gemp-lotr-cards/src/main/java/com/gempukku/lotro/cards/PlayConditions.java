package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.List;

public class PlayConditions {
    private static boolean nonPlayZone(Zone zone) {
        return zone != Zone.SHADOW_CHARACTERS && zone != Zone.SHADOW_SUPPORT
                && zone != Zone.FREE_SUPPORT && zone != Zone.FREE_CHARACTERS
                && zone != Zone.ATTACHED && zone != Zone.ADVENTURE_PATH;
    }

    public static boolean canPlayFPCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && nonPlayZone(self.getZone())
                && (!self.getBlueprint().isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName())));
    }

    public static boolean canPlayShadowCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && nonPlayZone(self.getZone())
                && game.getModifiersQuerying().getTwilightCost(game.getGameState(), self) <= game.getGameState().getTwilightPool()
                && (!self.getBlueprint().isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName())));
    }

    public static boolean canUseFPCardDuringPhase(GameState gameState, Phase phase, PhysicalCard self) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (self.getZone() == Zone.FREE_SUPPORT || self.getZone() == Zone.FREE_CHARACTERS || self.getZone() == Zone.ATTACHED);
    }

    public static boolean canUseShadowCardDuringPhase(GameState gameState, Phase phase, PhysicalCard self, int twilightCost) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (self.getZone() == Zone.SHADOW_SUPPORT || self.getZone() == Zone.SHADOW_CHARACTERS || self.getZone() == Zone.ATTACHED)
                && twilightCost <= gameState.getTwilightPool();
    }

    public static boolean canUseSiteDuringPhase(GameState gameState, Phase phase, PhysicalCard self) {
        return (phase == null || gameState.getCurrentPhase() == phase) && (gameState.getCurrentSite() == self);
    }

    public static boolean canPlayCompanionDuringSetup(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        return (nonPlayZone(self.getZone())
                && self.getBlueprint().getCardType() == CardType.COMPANION
                && gameState.getCurrentPhase() == Phase.GAME_SETUP
                && (!blueprint.isUnique() || !Filters.canSpot(gameState, modifiersQuerying, Filters.name(blueprint.getName())))
                && modifiersQuerying.getTwilightCost(gameState, self) <= (4 - gameState.getTwilightPool()));
    }

    public static boolean canPlayCharacterDuringFellowship(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        return (nonPlayZone(self.getZone())
                && (self.getBlueprint().getCardType() == CardType.COMPANION || self.getBlueprint().getCardType() == CardType.ALLY)
                && gameState.getCurrentPhase() == Phase.FELLOWSHIP
                && (!blueprint.isUnique() || !Filters.canSpot(gameState, modifiersQuerying, Filters.name(blueprint.getName()))));
    }

    public static boolean canPlayMinionDuringShadow(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        return (nonPlayZone(self.getZone())
                && gameState.getCurrentPhase() == Phase.SHADOW
                && gameState.getTwilightPool() >= modifiersQuerying.getTwilightCost(gameState, self)
                && (!blueprint.isUnique() || !Filters.canSpot(gameState, modifiersQuerying, Filters.name(blueprint.getName()))));
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

    public static boolean canExert(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return (gameState.getWounds(card) < modifiersQuerying.getVitality(gameState, card) - 1);
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

    public static boolean isWounded(EffectResult effectResult, PhysicalCard character) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            PhysicalCard woundedCard = ((WoundResult) effectResult).getWoundedCard();
            return (character == woundedCard);
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
}
