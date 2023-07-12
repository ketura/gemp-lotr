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

public class PlayCardRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public PlayCardRule(DefaultActionsEnvironment actionsEnvironment) {
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
                                if (TribblesPlayUtils.checkPlayRequirements(game, card, Filters.any, 0,
                                        0, false, false, true))
                                    result.add(TribblesPlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                            }
                            return result;
                        }

                        return null;
                    }
                }
        );
    }
}
