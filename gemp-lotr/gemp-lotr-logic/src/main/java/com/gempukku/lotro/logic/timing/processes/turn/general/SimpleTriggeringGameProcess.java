package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class SimpleTriggeringGameProcess implements GameProcess {
    private LotroGame _game;
    private EffectResult _effectResult;
    private String _text;
    private GameProcess _followingGameProcess;

    public SimpleTriggeringGameProcess(LotroGame game, EffectResult effectResult, String text, GameProcess followingGameProcess) {
        _game = game;
        _effectResult = effectResult;
        _followingGameProcess = followingGameProcess;
        _text = text;
    }

    @Override
    public void process(LotroGame game) {
        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringResultEffect(_effectResult, _text), _text));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}