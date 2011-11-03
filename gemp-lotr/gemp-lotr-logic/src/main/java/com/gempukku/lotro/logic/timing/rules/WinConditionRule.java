package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class WinConditionRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public WinConditionRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResults) {
                        if (effectResults.getType() == EffectResult.Type.START_OF_PHASE
                                && game.getGameState().getCurrentPhase() == Phase.REGROUP
                                && game.getGameState().getCurrentSiteNumber() == 9
                                && !game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.WIN_CHECK_AFTER_SHADOW_RECONCILE))
                            game.playerWon(game.getGameState().getCurrentPlayerId(), "Surviving to Regroup phase on site 9");
                        return null;
                    }
                });
    }
}
