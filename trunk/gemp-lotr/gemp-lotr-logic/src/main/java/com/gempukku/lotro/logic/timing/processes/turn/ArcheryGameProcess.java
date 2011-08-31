package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.archery.ArcheryFireGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.CanSpotGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ArcheryGameProcess implements GameProcess {
    private LotroGame _game;

    public ArcheryGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        if (_game.getModifiersQuerying().shouldSkipPhase(_game.getGameState(), Phase.ARCHERY))
            new CanSpotGameProcess(_game, Filters.type(CardType.MINION), new AssignmentGameProcess(_game), new RegroupGameProcess(_game));

        return new StartOfPhaseGameProcess(_game, Phase.ARCHERY,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new ArcheryFireGameProcess(_game,
                                new EndOfPhaseGameProcess(_game, Phase.ARCHERY,
                                        new CanSpotGameProcess(_game, Filters.type(CardType.MINION), new AssignmentGameProcess(_game), new RegroupGameProcess(_game))))));
    }
}
