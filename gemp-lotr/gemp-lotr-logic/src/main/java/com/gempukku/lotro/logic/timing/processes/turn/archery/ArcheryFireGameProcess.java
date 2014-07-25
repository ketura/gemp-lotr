package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ArcheryFireGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    private int _fellowshipArcheryTotal;
    private int _shadowArcheryTotal;
    private LotroGame _game;

    public ArcheryFireGameProcess(GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        _game = game;
        _fellowshipArcheryTotal = RuleUtils.calculateFellowshipArcheryTotal(game);

        _shadowArcheryTotal = RuleUtils.calculateShadowArcheryTotal(game);
        game.getGameState().sendMessage("Archery fire: fellowship - " + _fellowshipArcheryTotal + ", minion - " + _shadowArcheryTotal);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipPlayerAssignsArcheryDamageGameProcess(_shadowArcheryTotal,
                _game.getFormat().getAdventure().getAfterFellowshipArcheryGameProcess(_fellowshipArcheryTotal, _followingGameProcess));
    }
}
