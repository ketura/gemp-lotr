package com.gempukku.lotro.game.rules.tribbles;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.LinkedList;
import java.util.List;

public class TribblesPlayCardRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public TribblesPlayCardRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
                        if (GameUtils.isCurrentPlayer(game, playerId)) {
                            List<Action> result = new LinkedList<>();
                            for (PhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game)) {
                                if (game.checkPlayRequirements(card))
                                    result.add(TribblesPlayUtils.getPlayCardAction(game, card, 0, false));
                            }
                            return result;
                        }

                        return null;
                    }
                }
        );
    }
}
