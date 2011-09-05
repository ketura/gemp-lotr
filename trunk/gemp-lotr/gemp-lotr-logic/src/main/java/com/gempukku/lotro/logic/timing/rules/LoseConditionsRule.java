package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

public class LoseConditionsRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public LoseConditionsRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (!Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER))) {
                            DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, "Losing the game due to Ring-Bearer being killed");
                            action.addEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void playEffect(LotroGame game) {
                                            game.playerLost(game.getGameState().getCurrentPlayerId());
                                        }
                                    });
                            return Collections.singletonList(action);
                        }
                        PhysicalCard ringBearer = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER));
                        int ringBearerResistance = ringBearer.getBlueprint().getResistance();
                        if (game.getGameState().getBurdens(game.getGameState().getCurrentPlayerId()) >= ringBearerResistance) {
                            DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, "Losing the game due to Ring-Bearer corruption");
                            action.addEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void playEffect(LotroGame game) {
                                            game.playerLost(game.getGameState().getCurrentPlayerId());
                                        }
                                    });
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }
}
