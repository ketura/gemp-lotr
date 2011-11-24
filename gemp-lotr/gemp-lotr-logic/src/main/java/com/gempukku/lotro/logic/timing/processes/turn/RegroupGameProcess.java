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
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        _followingGameProcess = new StartOfPhaseGameProcess(Phase.REGROUP,
                new PlayersPlayPhaseActionsInOrderGameProcess(game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true), 0,
                        new CheckForSpecialWinConditionGameProcess(
                                new ShadowPlayersReconcileGameProcess(
                                        new FellowshipPlayerChoosesToMoveOrStayGameProcess()))));

    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }

    private class CheckForSpecialWinConditionGameProcess implements GameProcess {
        private GameProcess _followingProcess;

        private CheckForSpecialWinConditionGameProcess(GameProcess followingProcess) {
            _followingProcess = followingProcess;
        }

        @Override
        public void process(LotroGame game) {
            if (game.getGameState().getCurrentSiteNumber() == 9
                    && game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.WIN_CHECK_AFTER_SHADOW_RECONCILE)) {
                game.playerWon(game.getGameState().getCurrentPlayerId(), "Surviving to Shadow Reconcile on site 9");
            }
        }

        @Override
        public GameProcess getNextProcess() {
            return _followingProcess;
        }
    }
}
