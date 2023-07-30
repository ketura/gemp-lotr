package com.gempukku.lotro.rules;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;

import java.util.LinkedList;
import java.util.List;

public class OptionalTriggersFromHandRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public OptionalTriggersFromHandRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult) {
                        List<OptionalTriggerAction> result = new LinkedList<>();
                        final Side side = LotroGameUtils.getSide(game, playerId);
                        for (LotroPhysicalCard responseEvent : Filters.filter(game.getGameState().getHand(playerId), game, side)) {
                            final List<OptionalTriggerAction> actions = responseEvent.getBlueprint().getOptionalInHandAfterTriggers(playerId, game, effectResult, responseEvent);
                            if (actions != null)
                                result.addAll(actions);
                        }
                        return result;
                    }
                });
    }
}
