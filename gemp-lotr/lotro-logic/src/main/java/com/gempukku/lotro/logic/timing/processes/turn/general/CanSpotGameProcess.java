package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class CanSpotGameProcess implements GameProcess {
    private LotroGame _game;
    private Filter _filter;
    private GameProcess _canSpotGameProcess;
    private GameProcess _cantSpotGameProcess;

    public CanSpotGameProcess(LotroGame game, Filter filter, GameProcess canSpotGameProcess, GameProcess cantSpotGameProcess) {
        _game = game;
        _filter = filter;
        _canSpotGameProcess = canSpotGameProcess;
        _cantSpotGameProcess = cantSpotGameProcess;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        if (Filters.canSpot(_game.getGameState(), _game.getModifiersQuerying(), _filter))
            return _canSpotGameProcess;
        else
            return _cantSpotGameProcess;
    }
}
