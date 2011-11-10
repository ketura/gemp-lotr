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
    private LotroGame _game;

    public AfterSkirmishesGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process(LotroGame game) {

    }

    @Override
    public GameProcess getNextProcess() {
        GameState gameState = _game.getGameState();
        if (!gameState.isFierceSkirmishes() && Filters.canSpot(gameState, _game.getModifiersQuerying(), Filters.type(CardType.MINION), Filters.keyword(Keyword.FIERCE))) {
            gameState.setFierceSkirmishes(true);
            return new AssignmentGameProcess(_game);
        } else {
            return new RegroupGameProcess(_game);
        }
    }
}
