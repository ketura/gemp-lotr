package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ShadowPhaseOfPlayerGameProcess implements GameProcess {
    private PlayOrder _playOrder;
    private String _shadowPlayer;

    private GameProcess _followingGameProcess;

    public ShadowPhaseOfPlayerGameProcess(PlayOrder playOrder, String shadowPlayer) {
        _playOrder = playOrder;
        _shadowPlayer = shadowPlayer;
    }

    @Override
    public void process(LotroGame game) {
        String nextPlayer = _playOrder.getNextPlayer();
        GameProcess afterGameProcess;
        if (nextPlayer == null)
            afterGameProcess = new ManeuverGameProcess();
        else
            afterGameProcess = new ShadowPhaseOfPlayerGameProcess(_playOrder, nextPlayer);

        if (game.getModifiersQuerying().shouldSkipPhase(game.getGameState(), Phase.SHADOW, _shadowPlayer))
            _followingGameProcess = afterGameProcess;
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.SHADOW, _shadowPlayer,
                    new PlayerPlaysPhaseActionsUntilPassesGameProcess(_shadowPlayer,
                            new EndOfPhaseGameProcess(Phase.SHADOW,
                                    afterGameProcess)));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
