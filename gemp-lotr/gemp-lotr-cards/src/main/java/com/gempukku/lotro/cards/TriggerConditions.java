package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.*;

import java.util.List;
import java.util.Map;

public class TriggerConditions {
    public static boolean losesInitiative(EffectResult effectResult, Side side) {
        if (effectResult.getType() == EffectResult.Type.INITIATIVE_CHANGE) {
            InitiativeChangeResult initiativeChangeResult = (InitiativeChangeResult) effectResult;
            if (initiativeChangeResult.getSide() != side)
                return true;
        }
        return false;
    }

    public static boolean startOfPhase(LotroGame game, EffectResult effectResult, Phase phase) {
        return (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == phase);
    }

    public static boolean endOfPhase(LotroGame game, EffectResult effectResult, Phase phase) {
        return (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && game.getGameState().getCurrentPhase() == phase);
    }

    public static boolean winsSkirmish(LotroGame game, EffectResult effectResult, Filterable... filters) {
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            return (Filters.filter(skirmishResult.getWinners(), game.getGameState(), game.getModifiersQuerying(), Filters.and(filters)).size() > 0);
        }
        return false;
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

    public static boolean winsSkirmishAgainst(LotroGame game, EffectResult effectResult, Filterable winnerFilter, Filterable loserFilter) {
        GameState gameState = game.getGameState();
        ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            List<PhysicalCard> winners = skirmishResult.getWinners();
            List<PhysicalCard> losers = skirmishResult.getLosers();
            return (Filters.filter(winners, gameState, modifiersQuerying, winnerFilter).size() > 0)
                    && (Filters.filter(losers, gameState, modifiersQuerying, loserFilter).size() > 0);
        }
        return false;
    }

    public static boolean losesSkirmish(LotroGame game, EffectResult effectResult, Filter filter) {
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            return (Filters.filter(skirmishResult.getLosers(), game.getGameState(), game.getModifiersQuerying(), filter).size() > 0);
        }
        return false;
    }

    public static boolean losesSkirmishAgainst(LotroGame game, EffectResult effectResult, Filterable loserFilter, Filterable winnerFilter) {
        GameState gameState = game.getGameState();
        ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        EffectResult.Type effectType = effectResult.getType();
        if (effectType == EffectResult.Type.RESOLVE_SKIRMISH || effectType == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            return (Filters.filter(skirmishResult.getLosers(), gameState, modifiersQuerying, loserFilter).size() > 0)
                    && Filters.filter(skirmishResult.getWinners(), gameState, modifiersQuerying, winnerFilter).size() > 0;
        }
        return false;
    }

    public static boolean addedBurden(LotroGame game, EffectResult effectResult, Filterable... sourceFilters) {
        if (effectResult.getType() == EffectResult.Type.ADD_BURDEN) {
            AddBurdenResult burdenResult = (AddBurdenResult) effectResult;
            return (Filters.and(sourceFilters).accepts(game.getGameState(), game.getModifiersQuerying(), burdenResult.getSource()));
        }
        return false;
    }

    public static boolean addedThreat(LotroGame game, EffectResult effectResult, Filterable... sourceFilters) {
        if (effectResult.getType() == EffectResult.Type.ADD_THREAT) {
            AddThreatResult burdenResult = (AddThreatResult) effectResult;
            return (Filters.and(sourceFilters).accepts(game.getGameState(), game.getModifiersQuerying(), burdenResult.getSource()));
        }
        return false;
    }

    public static boolean assigned(LotroGame game, EffectResult effectResult, Side side, Filterable againstFilter, Filterable... cardFilters) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (side != null) {
                if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                    if (side == Side.SHADOW)
                        return false;
                } else {
                    if (side == Side.FREE_PEOPLE)
                        return false;
                }
            }

            final Map<PhysicalCard, List<PhysicalCard>> assignments = assignmentResult.getAssignments();
            for (PhysicalCard matchingFPCard : Filters.filter(assignments.keySet(), game.getGameState(), game.getModifiersQuerying(), cardFilters)) {
                if (Filters.filter(assignments.get(matchingFPCard), game.getGameState(), game.getModifiersQuerying(), againstFilter).size() > 0)
                    return true;
            }

            for (PhysicalCard matchingAgainstCard : Filters.filter(assignments.keySet(), game.getGameState(), game.getModifiersQuerying(), againstFilter)) {
                if (Filters.filter(assignments.get(matchingAgainstCard), game.getGameState(), game.getModifiersQuerying(), cardFilters).size() > 0)
                    return true;
            }
        }
        return false;
    }

    public static boolean isWounded(EffectResult effectResult, PhysicalCard character) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            return ((WoundResult) effectResult).getWoundedCards().contains(character);
        }
        return false;
    }

    public static boolean isTakingControlOfSite(Effect effect, LotroGame game, Filterable... sourceFilters) {
        if (effect.getType() == Effect.Type.BEFORE_TAKE_CONTROL_OF_A_SITE) {
            TakeControlOfASiteEffect takeControlOfASiteEffect = (TakeControlOfASiteEffect) effect;
            return !takeControlOfASiteEffect.isPrevented() && Filters.and(sourceFilters).accepts(game.getGameState(), game.getModifiersQuerying(), takeControlOfASiteEffect.getSource());
        }
        return false;
    }

    public static boolean isAddingBurden(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_ADD_BURDENS) {
            AddBurdenEffect addBurdenEffect = (AddBurdenEffect) effect;
            return !addBurdenEffect.isPrevented() && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), addBurdenEffect.getSource());
        }
        return false;
    }

    public static boolean isAddingTwilight(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_ADD_TWILIGHT) {
            AddTwilightEffect addTwilightEffect = (AddTwilightEffect) effect;
            return !addTwilightEffect.isPrevented() && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), addTwilightEffect.getSource());
        }
        return false;
    }

    public static boolean isGettingHealed(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_HEALED) {
            HealCharactersEffect healEffect = (HealCharactersEffect) effect;
            return Filters.filter(healEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingKilled(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_KILLED) {
            KillEffect killEffect = (KillEffect) effect;
            return Filters.filter(killEffect.getCharactersToBeKilled(), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingWoundedBy(Effect effect, LotroGame game, Filterable sourceFilter, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_WOUND) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            if (woundEffect.getSources() != null && Filters.filter(woundEffect.getSources(), game.getGameState(), game.getModifiersQuerying(), sourceFilter).size() > 0)
                return Filters.filter(woundEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingExerted(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_EXERT) {
            ExertCharactersEffect woundEffect = (ExertCharactersEffect) effect;
            return Filters.filter(woundEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingWounded(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_WOUND) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            return Filters.filter(woundEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingDiscardedBy(Effect effect, LotroGame game, Filterable sourceFilter, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_DISCARD_FROM_PLAY) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            if (discardEffect.getSource() != null && Filters.and(sourceFilter).accepts(game.getGameState(), game.getModifiersQuerying(), discardEffect.getSource()))
                return Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean isGettingDiscarded(Effect effect, LotroGame game, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_DISCARD_FROM_PLAY) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            return Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), filters).size() > 0;
        }
        return false;
    }

    public static boolean activated(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.ACTIVATE) {
            PhysicalCard source = ((ActivateCardResult) effectResult).getSource();
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), source);
        }
        return false;
    }

    public static boolean played(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.PLAY) {
            PhysicalCard playedCard = ((PlayCardResult) effectResult).getPlayedCard();
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), playedCard);
        }
        return false;
    }

    public static boolean movesTo(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite())) {
            return true;
        }
        return false;
    }

    public static boolean movesFrom(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_FROM
                && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite())) {
            return true;
        }
        return false;
    }
}
