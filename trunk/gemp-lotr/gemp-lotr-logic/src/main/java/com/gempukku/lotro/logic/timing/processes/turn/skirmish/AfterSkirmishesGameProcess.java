package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.AssignmentGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.RegroupGameProcess;

public class AfterSkirmishesGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        GameState gameState = game.getGameState();
        if (!gameState.isFierceSkirmishes() && Filters.canSpot(gameState, game.getModifiersQuerying(), CardType.MINION, Keyword.FIERCE)) {
            gameState.setFierceSkirmishes(true);
            _followingGameProcess = new AssignmentGameProcess();
        } else {
            _followingGameProcess = new RegroupGameProcess();
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
