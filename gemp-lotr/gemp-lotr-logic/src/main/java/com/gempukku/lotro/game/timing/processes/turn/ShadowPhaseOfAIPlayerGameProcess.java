package com.gempukku.lotro.game.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.adventure.ShadowAI;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.ai.PlayAdventureShadowActionsGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ShadowPhaseOfAIPlayerGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;
    private final ShadowAI _shadowAI;

    public ShadowPhaseOfAIPlayerGameProcess(ShadowAI shadowAI) {
        _shadowAI = shadowAI;
    }

    @Override
    public void process(DefaultGame game) {
        if (game.getModifiersQuerying().shouldSkipPhase(game, Phase.SHADOW, "AI"))
            _followingGameProcess = new ManeuverGameProcess();
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.SHADOW, "AI",
                    new PlayAdventureShadowActionsGameProcess(_shadowAI,
                            new EndOfPhaseGameProcess(Phase.SHADOW,
                                    new ManeuverGameProcess())));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
