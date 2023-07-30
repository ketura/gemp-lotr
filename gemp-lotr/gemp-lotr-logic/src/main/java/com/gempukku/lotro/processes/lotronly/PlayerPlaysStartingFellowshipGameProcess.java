package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.results.FinishedPlayingFellowshipResult;
import com.gempukku.lotro.rules.lotronly.LotroPlayUtils;
import com.gempukku.lotro.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.actions.Action;
import com.gempukku.lotro.processes.GameProcess;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PlayerPlaysStartingFellowshipGameProcess implements GameProcess {
    private final String _playerId;

    private final GameProcess _followingGameProcess;
    private GameProcess _nextProcess;

    public PlayerPlaysStartingFellowshipGameProcess(String playerId, GameProcess followingGameProcess) {
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        Collection<LotroPhysicalCard> possibleCharacters = getPossibleCharacters(game, _playerId);
        if (possibleCharacters.size() == 0) {
            game.getActionsEnvironment().emitEffectResult(new FinishedPlayingFellowshipResult(_playerId));
            _nextProcess = _followingGameProcess;
        } else
            game.getUserFeedback().sendAwaitingDecision(_playerId, createChooseNextCharacterDecision(game, _playerId, possibleCharacters));
    }

    private Collection<LotroPhysicalCard> getPossibleCharacters(final DefaultGame game, final String playerId) {
        return Filters.filter(game.getGameState().getDeck(playerId), game,
                CardType.COMPANION,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        int twilightCost = game.getModifiersQuerying().getTwilightCost(game, physicalCard, null, 0, false);
                        return game.getGameState().getTwilightPool() + twilightCost <= 4
                                && LotroPlayUtils.checkPlayRequirements(game, physicalCard, Filters.any, 0, 0, false, false, true);
                    }
                });
    }

    private AwaitingDecision createChooseNextCharacterDecision(final DefaultGame game, final String playerId, final Collection<LotroPhysicalCard> possibleCharacters) {
        return new ArbitraryCardsSelectionDecision(1, "Starting fellowship - Choose next character or press DONE",
                new LinkedList<>(possibleCharacters), 0, 1) {
            @Override
            public void decisionMade(String result) throws DecisionResultInvalidException {
                List<LotroPhysicalCard> selectedCharacters = getSelectedCardsByResponse(result);
                if (selectedCharacters.size() == 0) {
                    game.getActionsEnvironment().emitEffectResult(new FinishedPlayingFellowshipResult(_playerId));
                    _nextProcess = _followingGameProcess;
                } else {
                    LotroPhysicalCard selectedPhysicalCard = selectedCharacters.get(0);
                    Action playCardAction = LotroPlayUtils.getPlayCardAction(game, selectedPhysicalCard, 0, Filters.any, false);
                    game.getActionsEnvironment().addActionToStack(playCardAction);
                    _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(_playerId, _followingGameProcess);
                }
            }
        };
    }


    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
