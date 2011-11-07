package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.SkirmishGameProcess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayoutSkirmishesGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayoutSkirmishesGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        if (_game.getModifiersQuerying().shouldSkipPhase(_game.getGameState(), Phase.SKIRMISH, null)) {
            _nextProcess = _followingGameProcess;
        } else {
            final GameState gameState = _game.getGameState();
            List<Assignment> assignments = gameState.getAssignments();

            if (assignments.size() > 0) {
                Set<PhysicalCard> fps = new HashSet<PhysicalCard>();
                for (Assignment assignment : assignments)
                    fps.add(assignment.getFellowshipCharacter());

                String playerChoosingSkirmishOrder = gameState.getCurrentPlayerId();
                if (_game.getModifiersQuerying().hasFlagActive(gameState, ModifierFlag.SKIRMISH_ORDER_BY_FIRST_SHADOW_PLAYER))
                    playerChoosingSkirmishOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(playerChoosingSkirmishOrder, false).getNextPlayer();

                SystemQueueAction chooseNextSkirmishAction = new SystemQueueAction();

                ChooseActiveCardEffect chooseNextSkirmish = new ChooseActiveCardEffect(null, playerChoosingSkirmishOrder, "Choose next skirmish to resolve", Filters.in(fps)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        gameState.startSkirmish(card);

                        _nextProcess = new SkirmishGameProcess(_game, new PlayoutSkirmishesGameProcess(_game, _followingGameProcess));
                    }
                };
                chooseNextSkirmish.setUseShortcut(false);

                chooseNextSkirmishAction.appendEffect(chooseNextSkirmish);

                _game.getActionsEnvironment().addActionToStack(chooseNextSkirmishAction);
            } else {
                _nextProcess = _followingGameProcess;
            }
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
