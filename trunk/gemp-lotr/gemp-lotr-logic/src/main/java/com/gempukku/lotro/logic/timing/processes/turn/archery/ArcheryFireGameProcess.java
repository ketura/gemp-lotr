package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ArcheryFireGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    private int _fellowshipArcheryTotal;
    private int _shadowArcheryTotal;

    public ArcheryFireGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        _fellowshipArcheryTotal = RuleUtils.calculateFellowshipArcheryTotal(_game);

        _shadowArcheryTotal = RuleUtils.calculateShadowArcheryTotal(_game);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipPlayerAssignsArcheryDamageGameProcess(_game, _shadowArcheryTotal,
                new FellowshipPlayerChoosesShadowPlayerToAssignDamageToGameProcess(_game, _fellowshipArcheryTotal, _followingGameProcess));
    }
}
