package com.gempukku.lotro.game.rules;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.rules.lotronly.LotroGameUtils;

import java.util.LinkedList;
import java.util.List;

public class ActivatePhaseActionsFromHandRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public ActivatePhaseActionsFromHandRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
                        List<Action> result = new LinkedList<>();
                        final Side side = LotroGameUtils.getSide(game, playerId);
                        for (PhysicalCard activableCard : Filters.filter(game.getGameState().getHand(playerId), game, side)) {
                            List<? extends Action> list = activableCard.getBlueprint().getPhaseActionsInHand(playerId, game, activableCard);
                            if (list != null) {
                                for (Action action : list) {
                                    action.setVirtualCardAction(true);
                                    result.add(action);
                                }
                            }
                        }
                        return result;
                    }
                });
    }
}
