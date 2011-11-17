package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardVisitor;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class PlayConditions {
    public static boolean canPayForShadowCard(LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return game.getModifiersQuerying().getTwilightCost(game.getGameState(), self, ignoreRoamingPenalty) + twilightModifier <= game.getGameState().getTwilightPool();
    }

    private static boolean containsPhase(Phase[] phases, Phase phase) {
        for (Phase phase1 : phases) {
            if (phase1 == phase)
                return true;
        }
        return false;
    }

    public static boolean canDiscardFromHand(LotroGame game, String playerId, int count, Filterable... cardFilter) {
        return Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), cardFilter).size() >= count;
    }

    public static boolean canDiscardCardsFromHandToPlay(PhysicalCard source, LotroGame game, String playerId, int count, Filterable... cardFilter) {
        return Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(cardFilter, Filters.not(source))).size() >= count;
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

    public static boolean canUseFPCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && (self.getZone() == Zone.SUPPORT || self.getZone() == Zone.FREE_CHARACTERS || self.getZone() == Zone.ATTACHED);
    }

    public static boolean canUseShadowCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self, int twilightCost) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && (self.getZone() == Zone.SUPPORT || self.getZone() == Zone.SHADOW_CHARACTERS || self.getZone() == Zone.ATTACHED)
                && twilightCost <= game.getGameState().getTwilightPool();
    }

    public static boolean canUseStackedShadowCardDuringPhase(LotroGame game, Phase phase, PhysicalCard self, int twilightCost) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && self.getZone() == Zone.STACKED
                && twilightCost <= game.getGameState().getTwilightPool();
    }

    public static boolean isPhase(LotroGame game, Phase phase) {
        return (game.getGameState().getCurrentPhase() == phase);
    }

    public static boolean canUseSiteDuringPhase(LotroGame game, Phase phase, PhysicalCard self) {
        return (phase == null || game.getGameState().getCurrentPhase() == phase) && (game.getGameState().getCurrentSite() == self);
    }

    public static boolean stackedOn(PhysicalCard card, LotroGame game, Filterable... filters) {
        return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), card.getStackedOn());
    }

    public static boolean checkUniqueness(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        return (!blueprint.isUnique()
                || (
                !Filters.canSpot(gameState, modifiersQuerying, Filters.name(blueprint.getName()))
                        && (self.getZone() == Zone.DEAD || (Filters.filter(gameState.getDeadPile(self.getOwner()), gameState, modifiersQuerying, Filters.name(blueprint.getName())).size() == 0))));
    }

    private static int getTotalCompanions(String playerId, GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION)
                + Filters.filter(gameState.getDeadPile(playerId), gameState, modifiersQuerying, CardType.COMPANION).size();
    }

    public static boolean checkRuleOfNine(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (self.getZone() == Zone.DEAD)
            return (getTotalCompanions(self.getOwner(), gameState, modifiersQuerying) <= 9);
        else
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

    public static boolean canBeAssignedToSkirmishByEffect(PhysicalCard source, LotroGame game, Filterable... filters) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.and(filters), Filters.canBeAssignedToSkirmishByEffect(source.getBlueprint().getSide()));
    }

    public static boolean canSelfExert(PhysicalCard self, LotroGame game) {
        return canExert(self, game, 1, 1, self);
    }

    public static boolean canSelfExert(PhysicalCard self, int times, LotroGame game) {
        return canExert(self, game, times, 1, self);
    }

    public static boolean canExert(PhysicalCard source, LotroGame game, Filterable... filters) {
        return canExert(source, game, 1, 1, filters);
    }

    public static boolean canExert(PhysicalCard source, LotroGame game, int times, Filterable... filters) {
        return canExert(source, game, times, 1, filters);
    }

    public static boolean canExert(final PhysicalCard source, LotroGame game, final int times, final int count, Filterable... filters) {
        final GameState gameState = game.getGameState();
        final ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
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

    public static boolean canSpot(LotroGame game, Filterable... filters) {
        return canSpot(game, 1, filters);
    }

    public static boolean canSpot(LotroGame game, int count, Filterable... filters) {
        return Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), filters) >= count;
    }

    public static boolean canSpotThreat(LotroGame game, int count) {
        return game.getGameState().getThreats() >= count;
    }

    public static boolean canSpotBurdens(LotroGame game, int count) {
        return game.getGameState().getBurdens() >= count;
    }

    public static boolean hasInitiative(LotroGame game, Side side) {
        return game.getModifiersQuerying().hasInitiative(game.getGameState()) == side;
    }

    public static boolean canAddThreat(LotroGame game, PhysicalCard card, int count) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - game.getGameState().getThreats() >= count;
    }

    public static boolean canRemoveThreat(LotroGame game, PhysicalCard card, int count) {
        return game.getGameState().getThreats() >= count && game.getModifiersQuerying().canRemoveThreat(game.getGameState(), card);
    }

    public static boolean canRemoveBurdens(LotroGame game, PhysicalCard card, int count) {
        return game.getGameState().getBurdens() >= count && game.getModifiersQuerying().canRemoveBurden(game.getGameState(), card);
    }

    public static boolean canWound(PhysicalCard source, final LotroGame game, final int times, final int count, Filterable... filters) {
        final GameState gameState = game.getGameState();
        final ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        final Filter filter = Filters.and(filters);
        return gameState.iterateActiveCards(
                new PhysicalCardVisitor() {
                    private int _woundableCount;

                    @Override
                    public boolean visitPhysicalCard(PhysicalCard physicalCard) {
                        if (filter.accepts(gameState, modifiersQuerying, physicalCard)
                                && modifiersQuerying.getVitality(gameState, physicalCard) > times
                                && modifiersQuerying.canTakeWound(gameState, physicalCard))
                            _woundableCount++;
                        return _woundableCount >= count;
                    }
                });
    }

    public static boolean canHeal(PhysicalCard source, LotroGame game, Filterable... filters) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.wounded, Filters.and(filters),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.canBeHealed(gameState, physicalCard);
                    }
                }) >= 1;
    }

    public static boolean canPlayFromHand(String playerId, LotroGame game, Filterable... filters) {
        return Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game))).size() > 0;
    }

    public static boolean canPlayFromHand(String playerId, LotroGame game, int twilightModifier, Filterable... filters) {
        return Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game, twilightModifier))).size() > 0;
    }

    public static boolean canPlayFromHand(String playerId, LotroGame game, int twilightModifier, boolean ignoreRoamingPenalty, Filterable... filters) {
        return Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game, twilightModifier, ignoreRoamingPenalty))).size() > 0;
    }

    public static boolean canPlayFromDeadPile(String playerId, LotroGame game, Filterable... filters) {
        return Filters.filter(game.getGameState().getDeadPile(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game))).size() > 0;
    }

    public static boolean canPlayFromStacked(String playerId, LotroGame game, Filterable stackedOn, Filterable... filters) {
        final Collection<PhysicalCard> matchingStackedOn = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), stackedOn);
        for (PhysicalCard stackedOnCard : matchingStackedOn) {
            if (Filters.filter(game.getGameState().getStackedCards(stackedOnCard), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game))).size() > 0)
                return true;
        }

        return false;
    }

    public static boolean canPlayFromDiscard(String playerId, LotroGame game, Filterable... filters) {
        if (game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK))
            return false;
        return Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game))).size() > 0;
    }

    public static boolean canPlayFromDiscard(String playerId, LotroGame game, int modifier, Filterable... filters) {
        if (game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK))
            return false;
        return Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters, Filters.playable(game, modifier))).size() > 0;
    }

    public static boolean canDiscardFromPlay(final PhysicalCard source, LotroGame game, final PhysicalCard card) {
        return game.getModifiersQuerying().canBeDiscardedFromPlay(game.getGameState(), card, source);
    }

    public static boolean canSelfDiscard(PhysicalCard source, LotroGame game) {
        return canDiscardFromPlay(source, game, source);
    }

    public static boolean canDiscardFromPlay(final PhysicalCard source, LotroGame game, int count, final Filterable... filters) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(filters,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.canBeDiscardedFromPlay(gameState, physicalCard, source);
                    }
                })) >= count;
    }

    public static boolean canDiscardFromPlay(final PhysicalCard source, LotroGame game, final Filterable... filters) {
        return canDiscardFromPlay(source, game, 1, filters);
    }

    public static boolean controllsSite(LotroGame game, String playerId) {
        return Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId)) != null;
    }

    public static boolean canCardAssignToSkirmish(PhysicalCard source, LotroGame game, PhysicalCard card) {
        return Filters.canBeAssignedToSkirmishByEffect(source.getBlueprint().getSide()).accepts(game.getGameState(), game.getModifiersQuerying(), card);
    }

    public static boolean canCardAssignToSkirmish(PhysicalCard source, LotroGame game, Filterable filter) {
        return Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.canBeAssignedToSkirmishByEffect(source.getBlueprint().getSide()), filter) > 0;
    }

    public static boolean canRemoveAnyCultureTokens(LotroGame game, int count, Filterable... fromFilters) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(fromFilters, Filters.hasAnyCultureTokens(count))).size() > 0;
    }

    public static boolean canRemoveTokens(LotroGame game, PhysicalCard from, Token token) {
        return canRemoveTokens(game, from, token, 1);
    }

    public static boolean canRemoveTokens(LotroGame game, PhysicalCard from, Token token, int count) {
        return game.getGameState().getTokenCount(from, token) >= count;
    }

    public static boolean canRemoveTokens(LotroGame game, Token token, int count, Filterable... fromFilters) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(fromFilters, Filters.hasToken(token, count))).size() > 0;
    }
}
