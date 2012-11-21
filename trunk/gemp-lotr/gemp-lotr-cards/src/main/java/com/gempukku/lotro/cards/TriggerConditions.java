package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.*;

public class TriggerConditions {
    public static boolean losesInitiative(EffectResult effectResult, Side side) {
        if (effectResult.getType() == EffectResult.Type.INITIATIVE_CHANGE) {
            InitiativeChangeResult initiativeChangeResult = (InitiativeChangeResult) effectResult;
            if (initiativeChangeResult.getSide() != side)
                return true;
        }
        return false;
    }

    public static boolean takenControlOfASite(EffectResult effectResult, String playerId) {
        if (effectResult.getType() == EffectResult.Type.TAKE_CONTROL_OF_SITE) {
            TakeControlOfSiteResult takeResult = (TakeControlOfSiteResult) effectResult;
            return takeResult.getPlayerId().equals(playerId);
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

    public static boolean endOfTurn(LotroGame game, EffectResult effectResult) {
        return effectResult.getType() == EffectResult.Type.END_OF_TURN;
    }

    public static boolean winsSkirmish(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.CHARACTER_WON_SKIRMISH) {
            CharacterWonSkirmishResult wonResult = (CharacterWonSkirmishResult) effectResult;
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), wonResult.getWinner());
        }
        return false;
    }

    public static boolean winsSkirmishInvolving(LotroGame game, EffectResult effectResult, Filterable winnerFilter, Filterable involvingFilter) {
        if (effectResult.getType() == EffectResult.Type.CHARACTER_WON_SKIRMISH) {
            CharacterWonSkirmishResult wonResult = (CharacterWonSkirmishResult) effectResult;
            return Filters.and(winnerFilter).accepts(game.getGameState(), game.getModifiersQuerying(), wonResult.getWinner())
                    && Filters.filter(wonResult.getInvolving(), game.getGameState(), game.getModifiersQuerying(), involvingFilter).size() > 0;
        }
        return false;
    }

    public static boolean losesSkirmish(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.CHARACTER_LOST_SKIRMISH) {
            CharacterLostSkirmishResult wonResult = (CharacterLostSkirmishResult) effectResult;
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), wonResult.getLoser());
        }
        return false;
    }

    public static boolean losesSkirmishInvolving(LotroGame game, EffectResult effectResult, Filterable loserFilter, Filterable involvingFilter) {
        if (effectResult.getType() == EffectResult.Type.CHARACTER_LOST_SKIRMISH) {
            CharacterLostSkirmishResult wonResult = (CharacterLostSkirmishResult) effectResult;
            return Filters.and(loserFilter).accepts(game.getGameState(), game.getModifiersQuerying(), wonResult.getLoser())
                    && Filters.filter(wonResult.getInvolving(), game.getGameState(), game.getModifiersQuerying(), involvingFilter).size() > 0;
        }
        return false;
    }

    public static boolean sidePlayerAddedBurden(LotroGame game, EffectResult effectResult, Side side) {
        if (effectResult.getType() == EffectResult.Type.ADD_BURDEN) {
            AddBurdenResult burdenResult = (AddBurdenResult) effectResult;
            String fpPlayer = game.getGameState().getCurrentPlayerId();
            if ((side == Side.FREE_PEOPLE && fpPlayer.equals(burdenResult.getPerformingPlayer()))
                || (side == Side.SHADOW && !fpPlayer.equals(burdenResult.getPerformingPlayer())))
                return true;
            else
                return false;
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

    public static boolean assignedAgainst(LotroGame game, EffectResult effectResult, Side side, Filterable againstFilter, Filterable... assignedFilters) {
        if (effectResult.getType() == EffectResult.Type.CHARACTER_ASSIGNED) {
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

            return Filters.and(assignedFilters).accepts(game.getGameState(), game.getModifiersQuerying(), assignmentResult.getAssignedCard())
                    && Filters.filter(assignmentResult.getAgainst(), game.getGameState(), game.getModifiersQuerying(), againstFilter).size() > 0;
        }
        return false;
    }

    public static boolean forEachCardDrawnOrPutIntoHandByOpponent(LotroGame game, EffectResult effectResult, String playerId) {
        if (effectResult.getType() == EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND) {
            DrawCardOrPutIntoHandResult drawResult = (DrawCardOrPutIntoHandResult) effectResult;
            return !drawResult.getPlayerId().equals(playerId);
        }
        return false;
    }

    public static boolean forEachCardDrawnOrPutIntoHand(LotroGame game, EffectResult effectResult, String playerId) {
        if (effectResult.getType() == EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND) {
            DrawCardOrPutIntoHandResult drawResult = (DrawCardOrPutIntoHandResult) effectResult;
            return drawResult.getPlayerId().equals(playerId);
        }
        return false;
    }

    public static boolean forEachCardDrawn(LotroGame game, EffectResult effectResult, String playerId) {
        if (effectResult.getType() == EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND) {
            DrawCardOrPutIntoHandResult drawResult = (DrawCardOrPutIntoHandResult) effectResult;
            return drawResult.isDraw() && drawResult.getPlayerId().equals(playerId);
        }
        return false;
    }

    public static boolean forEachDiscardedFromPlay(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY)
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), ((DiscardCardsFromPlayResult) effectResult).getDiscardedCard());
        return false;
    }

    public static boolean forEachWounded(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_WOUNDED)
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), ((WoundResult) effectResult).getWoundedCard());
        return false;
    }

    public static boolean forEachHealed(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_HEALED)
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), ((HealResult) effectResult).getHealedCard());
        return false;
    }

    public static boolean forEachExerted(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_EXERTED)
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), ((ExertResult) effectResult).getExertedCard());
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
            return !addTwilightEffect.isPrevented() && addTwilightEffect.getSource() != null && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), addTwilightEffect.getSource());
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

    public static boolean isDrawingACard(Effect effect, LotroGame game, String playerId) {
        if (effect.getType() == Effect.Type.BEFORE_DRAW_CARD) {
            DrawOneCardEffect drawEffect = (DrawOneCardEffect) effect;
            if (drawEffect.getPlayerId().equals(playerId) && drawEffect.canDrawCard(game))
                return true;
        }
        return false;
    }

    public static boolean forEachKilled(LotroGame game, EffectResult effectResult, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_KILLED) {
            ForEachKilledResult killResult = (ForEachKilledResult) effectResult;
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), killResult.getKilledCard());
        }
        return false;
    }

    public static boolean forEachKilledBy(LotroGame game, EffectResult effectResult, Filterable killedBy, Filterable... killed) {
        return forEachKilledInASkirmish(game, effectResult, killedBy, killed);
    }

    public static boolean forEachKilledInASkirmish(LotroGame game, EffectResult effectResult, Filterable killedBy, Filterable... killed) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_KILLED
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, killedBy)) {
            ForEachKilledResult killResult = (ForEachKilledResult) effectResult;

            return Filters.and(killed).accepts(game.getGameState(), game.getModifiersQuerying(), killResult.getKilledCard());
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

    public static boolean isGettingDiscardedByOpponent(Effect effect, LotroGame game, String playerId, Filterable... filters) {
        if (effect.getType() == Effect.Type.BEFORE_DISCARD_FROM_PLAY) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            if (discardEffect.getSource() != null && !discardEffect.getPerformingPlayer().equals(playerId))
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

    public static boolean playedFromZone(LotroGame game, EffectResult effectResult, Zone zone, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.PLAY) {
            final PlayCardResult playResult = (PlayCardResult) effectResult;
            PhysicalCard playedCard = playResult.getPlayedCard();
            return (playResult.getPlayedFrom() == zone && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), playedCard));
        }
        return false;
    }

    public static boolean playedOn(LotroGame game, EffectResult effectResult, Filterable targetFilter, Filterable... filters) {
        if (effectResult.getType() == EffectResult.Type.PLAY) {
            final PlayCardResult playResult = (PlayCardResult) effectResult;
            final PhysicalCard attachedTo = playResult.getAttachedTo();
            if (attachedTo == null)
                return false;
            PhysicalCard playedCard = playResult.getPlayedCard();
            return Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), playedCard)
                    && Filters.and(targetFilter).accepts(game.getGameState(), game.getModifiersQuerying(), attachedTo);
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
                && Filters.and(filters).accepts(game.getGameState(), game.getModifiersQuerying(), ((WhenMoveFromResult) effectResult).getSite())) {
            return true;
        }
        return false;
    }

    public static boolean moves(LotroGame game, EffectResult effectResult) {
        return effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES;
    }

    public static boolean transferredCard(LotroGame game, EffectResult effectResult, Filterable transferredCard, Filterable transferredFrom, Filterable transferredTo) {
        if (effectResult.getType() == EffectResult.Type.CARD_TRANSFERRED) {
            CardTransferredResult transferResult = (CardTransferredResult) effectResult;
            return (Filters.and(transferredCard).accepts(game.getGameState(), game.getModifiersQuerying(), transferResult.getTransferredCard())
                    && (transferredFrom == null || (transferResult.getTransferredFrom() != null && Filters.and(transferredFrom).accepts(game.getGameState(), game.getModifiersQuerying(), transferResult.getTransferredFrom())))
                    && (transferredTo == null || (transferResult.getTransferredTo() != null && Filters.and(transferredTo).accepts(game.getGameState(), game.getModifiersQuerying(), transferResult.getTransferredTo()))));
        }
        return false;
    }
}
