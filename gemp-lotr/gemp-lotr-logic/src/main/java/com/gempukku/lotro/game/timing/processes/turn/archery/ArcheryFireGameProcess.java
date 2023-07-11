package com.gempukku.lotro.game.timing.processes.turn.archery;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.rules.RuleUtils;
import com.gempukku.lotro.game.timing.processes.GameProcess;

public class ArcheryFireGameProcess implements GameProcess {
    private final GameProcess _followingGameProcess;

    private int _fellowshipArcheryTotal;
    private int _shadowArcheryTotal;
    private DefaultGame _game;

    public ArcheryFireGameProcess(GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
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
