package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.assign.FreePeoplePlayerAssignsMinionsGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.assign.ShadowPlayersAssignTheirMinionsGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.CanSpotGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.skirmish.AfterSkirmishesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.skirmish.PlayoutSkirmishesGameProcess;

public class AssignmentGameProcess implements GameProcess {
    private LotroGame _game;

    public AssignmentGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        if (_game.getModifiersQuerying().shouldSkipPhase(_game.getGameState(), Phase.ASSIGNMENT))
            return new CanSpotGameProcess(_game, Filters.type(CardType.MINION), new PlayoutSkirmishesGameProcess(_game, new AfterSkirmishesGameProcess(_game)), new RegroupGameProcess(_game));

        return new StartOfPhaseGameProcess(_game, Phase.ASSIGNMENT,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new FreePeoplePlayerAssignsMinionsGameProcess(_game,
                                new ShadowPlayersAssignTheirMinionsGameProcess(_game,
                                        new EndOfPhaseGameProcess(_game, Phase.ASSIGNMENT,
                                                new CanSpotGameProcess(_game, Filters.type(CardType.MINION),
                                                        new PlayoutSkirmishesGameProcess(_game, new AfterSkirmishesGameProcess(_game)), new RegroupGameProcess(_game)))))));
    }
}
