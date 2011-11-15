package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.actions.ResolveSkirmishDamageAction;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ResolveSkirmishRule {
    private LotroGame _lotroGame;
    private DefaultActionsEnvironment _actionsEnvironment;

    public ResolveSkirmishRule(LotroGame lotroGame, DefaultActionsEnvironment actionsEnvironment) {
        _lotroGame = lotroGame;
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.RESOLVE_SKIRMISH) {
                            NormalSkirmishResult skirmishResult = (NormalSkirmishResult) effectResult;
                            ResolveSkirmishDamageAction action = new ResolveSkirmishDamageAction(skirmishResult);
                            return Collections.singletonList(action);
                        } else if (effectResult.getType() == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
                            OverwhelmSkirmishResult skirmishResult = (OverwhelmSkirmishResult) effectResult;
                            List<PhysicalCard> losers = new LinkedList<PhysicalCard>(skirmishResult.getInSkirmishLosers());

                            RequiredTriggerAction action = new RequiredTriggerAction(null);
                            action.setText("Resolving skirmish");
                            action.appendEffect(new KillEffect(losers, KillEffect.Cause.OVERWHELM));

                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                }
        );
    }
}
