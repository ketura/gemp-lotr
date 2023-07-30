package com.gempukku.lotro.rules;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.actions.ActivateCardAction;
import com.gempukku.lotro.actions.Action;

import java.util.LinkedList;
import java.util.List;

public class ActivatePhaseActionsRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public ActivatePhaseActionsRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
                        List<Action> result = new LinkedList<>();
                        for (LotroPhysicalCard activableCard : Filters.filter(game.getGameState().getInPlay(), game, getActivatableCardsFilter(playerId))) {
                            if (!game.getModifiersQuerying().hasTextRemoved(game, activableCard)) {
                                final List<? extends ActivateCardAction> actions = activableCard.getBlueprint().getPhaseActionsInPlay(playerId, game, activableCard);
                                if (actions != null)
                                    result.addAll(actions);

                                final List<? extends Action> extraActions = game.getModifiersQuerying().getExtraPhaseActions(game, activableCard);
                                if (extraActions != null) {
                                    for (Action action : extraActions) {
                                        if (action != null)
                                            result.add(action);
                                    }
                                }
                            }
                        }

                        return result;
                    }
                });
    }

    private Filter getActivatableCardsFilter(String playerId) {
        return Filters.or(
                Filters.and(CardType.SITE,
                        new Filter() {
                            @Override
                            public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                                if (game.getGameState().getCurrentPhase().isRealPhase())
                                    return Filters.currentSite.accepts(game, physicalCard);
                                return false;
                            }
                        }),
                Filters.and(Filters.not(CardType.SITE), Filters.owner(playerId), Filters.active));
    }
}
