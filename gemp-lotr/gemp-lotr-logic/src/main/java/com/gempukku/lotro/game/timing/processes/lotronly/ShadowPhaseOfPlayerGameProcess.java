package com.gempukku.lotro.game.timing.processes.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.EndOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.StartOfPhaseGameProcess;

public class ShadowPhaseOfPlayerGameProcess implements GameProcess {
    private final PlayOrder _playOrder;
    private final String _shadowPlayer;

    private GameProcess _followingGameProcess;

    public ShadowPhaseOfPlayerGameProcess(PlayOrder playOrder, String shadowPlayer) {
        _playOrder = playOrder;
        _shadowPlayer = shadowPlayer;
    }

    @Override
    public void process(DefaultGame game) {
        String nextPlayer = _playOrder.getNextPlayer();
        GameProcess afterGameProcess;
        if (nextPlayer == null)
            afterGameProcess = new ManeuverGameProcess();
        else
            afterGameProcess = new ShadowPhaseOfPlayerGameProcess(_playOrder, nextPlayer);

        if (game.getModifiersQuerying().shouldSkipPhase(game, Phase.SHADOW, _shadowPlayer))
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
