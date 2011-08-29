package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.CanSpotGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ManeuverGameProcess implements GameProcess {
    private LotroGame _game;

    public ManeuverGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfPhaseGameProcess(_game, Phase.MANEUVER,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new EndOfPhaseGameProcess(_game, Phase.MANEUVER,
                                new CanSpotGameProcess(_game, Filters.type(CardType.MINION), new ArcheryGameProcess(_game), new RegroupGameProcess(_game)))));
    }
}
