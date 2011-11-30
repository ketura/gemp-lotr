package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class MusterRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public MusterRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy("Muster rule") {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                && game.getGameState().getCurrentPhase() == Phase.REGROUP
                                && game.getGameState().getHand(playerId).size() > 0) {
                            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                            for (PhysicalCard musterCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Keyword.MUSTER)) {
                                OptionalTriggerAction action = new OptionalTriggerAction(musterCard);
                                action.appendCost(
                                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
                                action.appendEffect(
                                        new DrawCardsEffect(playerId, 1));
                                actions.add(action);
                            }

                            return actions;
                        }
                        return null;
                    }
                });
    }
}
