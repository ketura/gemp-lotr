package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PlayerPlaysStartingFellowshipGameProcess implements GameProcess {
    private String _playerId;

    private GameProcess _followingGameProcess;
    private GameProcess _nextProcess;

    public PlayerPlaysStartingFellowshipGameProcess(String playerId, GameProcess followingGameProcess) {
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        Collection<PhysicalCard> possibleCharacters = getPossibleCharacters(game, _playerId);
        if (possibleCharacters.size() == 0)
            _nextProcess = _followingGameProcess;
        else
            game.getUserFeedback().sendAwaitingDecision(_playerId, createChooseNextCharacterDecision(game, _playerId, possibleCharacters));
    }

    private Collection<PhysicalCard> getPossibleCharacters(final LotroGame game, final String playerId) {
        return Filters.filter(game.getGameState().getDeck(playerId), game.getGameState(), game.getModifiersQuerying(),
                CardType.COMPANION,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        int twilightCost = modifiersQuerying.getTwilightCost(gameState, physicalCard, false);
                        return gameState.getTwilightPool() + twilightCost <= 4
                                && physicalCard.getBlueprint().checkPlayRequirements(playerId, game, physicalCard, 0, false, false);
                    }
                });
    }

    private AwaitingDecision createChooseNextCharacterDecision(final LotroGame game, final String playerId, final Collection<PhysicalCard> possibleCharacters) {
        return new ArbitraryCardsSelectionDecision(1, "Starting fellowship - Choose next character or press DONE",
                new LinkedList<PhysicalCard>(possibleCharacters), 0, 1) {
            @Override
            public void decisionMade(String result) throws DecisionResultInvalidException {
                List<PhysicalCard> selectedCharacters = getSelectedCardsByResponse(result);
                if (selectedCharacters.size() == 0)
                    _nextProcess = _followingGameProcess;
                else {
                    PhysicalCard selectedPhysicalCard = selectedCharacters.get(0);
                    Action playCardAction = selectedPhysicalCard.getBlueprint().getPlayCardAction(playerId, game, selectedPhysicalCard, 0, false);
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
