package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class KilledCardRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public KilledCardRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.KILL) {
                            KillResult killResult = (KillResult) effectResult;
                            Set<PhysicalCard> killedCards = killResult.getKilledCards();
                            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                            for (PhysicalCard killedCard : killedCards) {
                                OptionalTriggerAction trigger = killedCard.getBlueprint().getKilledOptionalTrigger(playerId, game, killedCard);
                                if (trigger != null) {
                                    trigger.setVirtualCardAction(true);
                                    actions.add(trigger);
                                }
                            }
                            return actions;
                        }
                        return null;
                    }
                });
    }
}
