package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.WoundAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.List;

public class ShadowPlayerAssignsArcheryDamageGameProcess implements GameProcess {
    private LotroGame _game;
    private String _playerId;
    private int _woundsToAssign;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public ShadowPlayerAssignsArcheryDamageGameProcess(LotroGame game, String playerId, int woundsToAssign, GameProcess followingGameProcess) {
        _game = game;
        _playerId = playerId;
        _woundsToAssign = woundsToAssign;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        if (_woundsToAssign > 0) {
            GameState gameState = _game.getGameState();
            List<PhysicalCard> possibleWoundTargets = Filters.filterActive(gameState, _game.getModifiersQuerying(),
                    Filters.type(CardType.MINION), Filters.owner(_playerId));

            if (possibleWoundTargets.size() > 0) {
                _game.getUserFeedback().sendAwaitingDecision(_playerId,
                        new CardsSelectionDecision(1, "Choose minion to assign archery wound to", possibleWoundTargets, 1, 1) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                PhysicalCard selectedCard = getSelectedCardsByResponse(result).get(0);
                                _game.getActionsEnvironment().addActionToStack(new WoundAction(_playerId, selectedCard, 1));
                                if (_woundsToAssign > 1)
                                    _nextProcess = new ShadowPlayerAssignsArcheryDamageGameProcess(_game, _playerId, _woundsToAssign - 1, _followingGameProcess);
                                else
                                    _nextProcess = _followingGameProcess;
                            }
                        });
            } else {
                _nextProcess = _followingGameProcess;
            }
        } else {
            _nextProcess = _followingGameProcess;
        }

    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
