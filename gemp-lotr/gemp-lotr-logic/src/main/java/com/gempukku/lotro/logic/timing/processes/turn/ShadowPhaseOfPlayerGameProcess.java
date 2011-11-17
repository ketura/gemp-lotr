package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.CanSpotGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ShadowPhaseOfPlayerGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;
    private String _shadowPlayer;

    public ShadowPhaseOfPlayerGameProcess(LotroGame game, PlayOrder playOrder, String shadowPlayer) {
        _game = game;
        _playOrder = playOrder;
        _shadowPlayer = shadowPlayer;
    }

    @Override
    public void process(LotroGame game) {

    }

    @Override
    public GameProcess getNextProcess() {
        String nextPlayer = _playOrder.getNextPlayer();

        GameProcess followingGameProcess;
        if (nextPlayer == null)
            followingGameProcess = new CanSpotGameProcess(_game, CardType.MINION, new ManeuverGameProcess(_game), new RegroupGameProcess(_game));
        else
            followingGameProcess = new ShadowPhaseOfPlayerGameProcess(_game, _playOrder, nextPlayer);

        return new StartOfPhaseGameProcess(_game, Phase.SHADOW,
                new PlayerPlaysPhaseActionsUntilPassesGameProcess(_game, _shadowPlayer,
                        new EndOfPhaseGameProcess(_game, Phase.SHADOW,
                                followingGameProcess)));
    }
}
