package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.adventure.ShadowAI;
import com.gempukku.lotro.processes.turn.EndOfPhaseGameProcess;
import com.gempukku.lotro.processes.turn.StartOfPhaseGameProcess;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.lotronly.ai.PlayAdventureShadowActionsGameProcess;

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
