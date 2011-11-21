package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.effects.ThreatWoundsEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

public class ThreatRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public ThreatRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.KILL) {
                            KillResult killResult = (KillResult) effectResult;
                            if (Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), Filters.or(CardType.COMPANION, CardType.ALLY)).size() > 0) {
                                RequiredTriggerAction action = new RequiredTriggerAction(null);
                                action.appendEffect(
                                        new ThreatWoundsEffect(killResult));
                                return Collections.singletonList(action);
                            }
                        }
                        if (effectResult.getType() == EffectResult.Type.THREAT_WOUND_TRIGGER) {
                            int threats = game.getGameState().getThreats();
                            if (threats > 0) {
                                RequiredTriggerAction action = new RequiredTriggerAction(null);
                                action.setText("Threat damage assignment");
                                action.appendEffect(
                                        new SendMessageEffect(game.getGameState().getCurrentPlayerId() + " assigns " + threats + " threat damage"));
                                action.appendEffect(
                                        new RemoveThreatsEffect(null, threats));
                                for (int i = 0; i < threats; i++) {
                                    Filterable filter = CardType.COMPANION;

                                    if (game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.RING_BEARER_CANT_TAKE_THREAT_WOUNDS))
                                        filter = Filters.and(filter, Filters.not(Keyword.RING_BEARER));

                                    action.appendEffect(
                                            new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, filter));
                                }
                                return Collections.singletonList(action);
                            }
                        }
                        return null;
                    }
                });
    }
}
