package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class ChoiceCost implements Cost {
    private CostToEffectAction _action;
    private String _choicePlayerId;
    private List<ChooseableCost> _possibleCosts;

    public ChoiceCost(CostToEffectAction action, String choicePlayerId, List<ChooseableCost> possibleCosts) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _possibleCosts = possibleCosts;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        final List<Cost> possibleCosts = new LinkedList<Cost>();
        for (ChooseableCost cost : _possibleCosts) {
            if (cost.canPlayCost(game))
                possibleCosts.add(cost);
        }

        if (possibleCosts.size() == 0)
            return new CostResolution(null, false);

        if (possibleCosts.size() == 1) {
            _action.appendCost(possibleCosts.get(0));
        } else {
            game.getUserFeedback().sendAwaitingDecision(_choicePlayerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose cost to pay", getEffectsText(possibleCosts, game)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            _action.insertCost(possibleCosts.get(index));
                        }
                    });
        }
        return new CostResolution(null, true);
    }

    private String[] getEffectsText(List<Cost> possibleCosts, LotroGame game) {
        String[] result = new String[possibleCosts.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = possibleCosts.get(i).getText(game);
        return result;
    }
}
