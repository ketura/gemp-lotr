package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.FellowshipPlayerChoosesToMoveOrStayGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.ShadowPlayersReconcileGameProcess;

public class RegroupGameProcess implements GameProcess {
    private LotroGame _game;

    public RegroupGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfPhaseGameProcess(_game, Phase.REGROUP,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new CheckForSpecialWinConditionGameProcess(
                                new ShadowPlayersReconcileGameProcess(_game,
                                        new FellowshipPlayerChoosesToMoveOrStayGameProcess(_game)))));
    }

    private class CheckForSpecialWinConditionGameProcess implements GameProcess {
        private GameProcess _followingProcess;

        private CheckForSpecialWinConditionGameProcess(GameProcess followingProcess) {
            _followingProcess = followingProcess;
        }

        @Override
        public void process() {
            if (_game.getGameState().getCurrentSiteNumber() == 9
                    && _game.getModifiersQuerying().hasFlagActive(ModifierFlag.WIN_CHECK_AFTER_SHADOW_RECONCILE)) {
                _game.playerWon(_game.getGameState().getCurrentPlayerId(), "Surviving to Shadow Reconcile on site 9");
            }
        }

        @Override
        public GameProcess getNextProcess() {
            return _followingProcess;
        }
    }
}
