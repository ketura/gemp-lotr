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
    private LotroGame _game;
    private String _playerId;

    private GameProcess _followingGameProcess;
    private GameProcess _nextProcess;

    public PlayerPlaysStartingFellowshipGameProcess(LotroGame game, String playerId, GameProcess followingGameProcess) {
        _game = game;
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        Collection<PhysicalCard> possibleCharacters = getPossibleCharacters(_playerId);
        if (possibleCharacters.size() == 0)
            _nextProcess = _followingGameProcess;
        else
            _game.getUserFeedback().sendAwaitingDecision(_playerId, createChooseNextCharacterDecision(_playerId, possibleCharacters));
    }

    private Collection<PhysicalCard> getPossibleCharacters(final String playerId) {
        return Filters.filter(_game.getGameState().getDeck(playerId), _game.getGameState(), _game.getModifiersQuerying(),
                Filters.type(CardType.COMPANION),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        int twilightCost = modifiersQuerying.getTwilightCost(gameState, physicalCard);
                        return gameState.getTwilightPool() + twilightCost <= 4
                                && physicalCard.getBlueprint().checkPlayRequirements(playerId, _game, physicalCard, 0);
                    }
                });
    }

    private AwaitingDecision createChooseNextCharacterDecision(final String playerId, final Collection<PhysicalCard> possibleCharacters) {
        return new ArbitraryCardsSelectionDecision(1, "Starting fellowship - Choose next character or press DONE",
                new LinkedList<PhysicalCard>(possibleCharacters), 0, 1) {
            @Override
            public void decisionMade(String result) throws DecisionResultInvalidException {
                List<PhysicalCard> selectedCharacters = getSelectedCardsByResponse(result);
                if (selectedCharacters.size() == 0)
                    _nextProcess = _followingGameProcess;
                else {
                    PhysicalCard selectedPhysicalCard = selectedCharacters.get(0);
                    Action playCardAction = selectedPhysicalCard.getBlueprint().getPlayCardAction(playerId, _game, selectedPhysicalCard, 0);
                    _game.getActionsEnvironment().addActionToStack(playCardAction);
                    _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(_game, _playerId, _followingGameProcess);
                }
            }
        };
    }


    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
