package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.EndOfTurnResult;

import java.util.Collection;

public class EndOfTurnGameProcess implements GameProcess {
    private LotroGame _game;

    public EndOfTurnGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process(LotroGame game) {
        SystemQueueAction action = new SystemQueueAction() {
            @Override
            public String getText(LotroGame game) {
                return "End of turn";
            }
        };
        action.appendEffect(
                new TriggeringResultEffect(null, new EndOfTurnResult(), "End of turn"));
        action.appendEffect(
                new AbstractSuccessfulEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return null;
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }

                    @Override
                    public Collection<? extends EffectResult> playEffect(LotroGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).removeEndOfTurn();
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).removeEndOfTurnActionProxies();
                        return null;
                    }
                });
        _game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new CleanupProcess(_game);
    }
}
