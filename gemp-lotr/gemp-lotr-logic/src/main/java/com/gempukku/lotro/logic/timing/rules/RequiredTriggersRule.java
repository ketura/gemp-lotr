package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.condition.OrCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class RequiredTriggersRule {
    private DefaultActionsEnvironment actionsEnvironment;

    public RequiredTriggersRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect) {
                        List<RequiredTriggerAction> result = new LinkedList<>();
                        for (PhysicalCard activableCard : Filters.filter(game.getGameState().getInPlay(), game, getActivatableCardsFilter())) {
                            if (!game.getModifiersQuerying().hasTextRemoved(game, activableCard)) {
                                final List<? extends RequiredTriggerAction> actions = activableCard.getBlueprint().getRequiredBeforeTriggers(game, effect, activableCard);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }

                        return result;
                    }

                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        List<RequiredTriggerAction> result = new LinkedList<>();
                        for (PhysicalCard activableCard : Filters.filter(game.getGameState().getInPlay(), game, getActivatableCardsFilter())) {
                            if (!game.getModifiersQuerying().hasTextRemoved(game, activableCard)) {
                                final List<? extends RequiredTriggerAction> actions = activableCard.getBlueprint().getRequiredAfterTriggers(game, effectResult, activableCard);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }

                        return result;
                    }
                }
        );
    }

    private Filter getActivatableCardsFilter() {
        return Filters.conditionFilter(
                Filters.or(Filters.currentSite, Filters.and(Filters.not(CardType.SITE))),
                new OrCondition(new PhaseCondition(Phase.PUT_RING_BEARER), new PhaseCondition(Phase.PLAY_STARTING_FELLOWSHIP)),
                Filters.and(Filters.not(CardType.SITE)));
    }
}
