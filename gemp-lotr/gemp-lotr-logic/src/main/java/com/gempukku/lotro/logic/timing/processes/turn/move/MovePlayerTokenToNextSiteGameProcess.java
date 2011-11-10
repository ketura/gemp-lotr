package com.gempukku.lotro.logic.timing.processes.turn.move;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class MovePlayerTokenToNextSiteGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    public MovePlayerTokenToNextSiteGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        gameState.movePlayerToNextSite(_game);

        int siteTwilightCost = _game.getModifiersQuerying().getTwilightCost(gameState, gameState.getCurrentSite(), false);
        int companionCount = Filters.countActive(gameState, _game.getModifiersQuerying(), Filters.type(CardType.COMPANION));

        gameState.addTwilight(siteTwilightCost + companionCount);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
