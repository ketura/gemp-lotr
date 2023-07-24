package com.gempukku.lotro.game.rules.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.timing.results.NormalSkirmishResult;
import com.gempukku.lotro.game.actions.lotronly.ResolveSkirmishDamageAction;
import com.gempukku.lotro.game.effects.KillEffect;
import com.gempukku.lotro.game.effects.EffectResult;
import com.gempukku.lotro.game.timing.results.OverwhelmSkirmishResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResolveSkirmishRule {
    private final DefaultGame _lotroGame;
    private final DefaultActionsEnvironment _actionsEnvironment;

    public ResolveSkirmishRule(DefaultGame lotroGame, DefaultActionsEnvironment actionsEnvironment) {
        _lotroGame = lotroGame;
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame lotroGame, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.SKIRMISH_FINISHED_NORMALLY) {
                            NormalSkirmishResult skirmishResult = (NormalSkirmishResult) effectResult;
                            ResolveSkirmishDamageAction action = new ResolveSkirmishDamageAction(skirmishResult);
                            return Collections.singletonList(action);
                        } else if (effectResult.getType() == EffectResult.Type.SKIRMISH_FINISHED_WITH_OVERWHELM) {
                            OverwhelmSkirmishResult skirmishResult = (OverwhelmSkirmishResult) effectResult;
                            Set<LotroPhysicalCard> losers = new HashSet<>(skirmishResult.getInSkirmishLosers());

                            RequiredTriggerAction action = new RequiredTriggerAction(null);
                            action.setText("Resolve skirmish overwhelm");
                            action.appendEffect(new KillEffect(losers, KillEffect.Cause.OVERWHELM));

                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                }
        );
    }
}
