package com.gempukku.lotro.rules.tribbles;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.game.TribblesGame;

import java.util.LinkedList;
import java.util.List;

public class TribblesOptionalTriggersRule {

    protected final DefaultActionsEnvironment actionsEnvironment;

    public TribblesOptionalTriggersRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect) {
                        List<OptionalTriggerAction> result = new LinkedList<>();
                        for (LotroPhysicalCard activableCard : Filters.filter(game.getGameState().getInPlay(), game, getActivatableCardsFilter(playerId))) {
                            if (!game.getModifiersQuerying().hasTextRemoved(game, activableCard)) {
                                final List<? extends OptionalTriggerAction> actions = activableCard.getBlueprint().getOptionalBeforeTriggers(playerId, game, effect, activableCard);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }

                        return result;
                    }

                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggerActions(String playerId,
                                                                                                DefaultGame game,
                                                                                                EffectResult effectResult) {
                        List<OptionalTriggerAction> result = new LinkedList<>();
                        for (LotroPhysicalCard activatableCard : Filters.filter(game.getGameState().getInPlay(),
                                game, getActivatableCardsFilter(playerId))) {
                            if (!game.getModifiersQuerying().hasTextRemoved(game, activatableCard)) {
                                final List<? extends OptionalTriggerAction> actions =
                                        activatableCard.getOptionalAfterTriggerActions(playerId, game, effectResult,
                                                activatableCard);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }

                        return result;
                    }
                }
        );
    }

    private Filter getActivatableCardsFilter(String playerId) {
        return Filters.and(Filters.owner(playerId), Filters.active);
    }
}