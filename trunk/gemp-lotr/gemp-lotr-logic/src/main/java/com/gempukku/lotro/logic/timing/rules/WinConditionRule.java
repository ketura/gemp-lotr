package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
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
                    public List<? extends Action> getRequiredBeforeTriggers(final LotroGame lotroGame, Effect effect, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                && lotroGame.getGameState().getCurrentPhase() == Phase.REGROUP
                                && lotroGame.getGameState().getCurrentSiteNumber() == 9) {
                            CostToEffectAction action = new CostToEffectAction(null, null, "Winning the game");
                            action.addEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void playEffect(LotroGame game) {
                                            game.playerWon(lotroGame.getGameState().getCurrentPlayerId());
                                        }
                                    });
                            return Collections.singletonList(action);
                        }

                        return null;
                    }
                }
        );
    }
}
