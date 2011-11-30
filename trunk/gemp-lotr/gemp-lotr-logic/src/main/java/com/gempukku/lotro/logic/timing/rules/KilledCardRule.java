package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KilledResult;

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
                new AbstractActionProxy("Killed card rule") {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.ANY_NUMBER_KILLED) {
                            KilledResult killResult = (KilledResult) effectResult;
                            Set<PhysicalCard> killedCards = killResult.getKilledCards();
                            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                            for (PhysicalCard killedCard : killedCards) {
                                if (!effectResult.wasOptionalTriggerUsed(killedCard)) {
                                    OptionalTriggerAction trigger = killedCard.getBlueprint().getKilledOptionalTrigger(playerId, game, killedCard);
                                    if (trigger != null) {
                                        trigger.setVirtualCardAction(true);
                                        actions.add(trigger);
                                    }
                                }
                            }
                            return actions;
                        }
                        return null;
                    }
                });
    }
}
