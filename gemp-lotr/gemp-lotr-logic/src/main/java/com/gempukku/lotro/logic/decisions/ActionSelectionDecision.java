package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public abstract class ActionSelectionDecision extends AbstractAwaitingDecision {
    private List<? extends Action> _actions;

    public ActionSelectionDecision(int decisionId, String text, List<? extends Action> actions) {
        super(decisionId, text, AwaitingDecisionType.ACTION_CHOICE);
        _actions = actions;

        setParam("actionId", getActionIds(actions));
        setParam("blueprintId", getCardIds(actions));
        setParam("actionText", getActionTexts(actions));
    }

    private String[] getActionIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = String.valueOf(i);
        return result;
    }

    private String[] getCardIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++) {
            PhysicalCard physicalCard = actions.get(i).getActionSource();
            if (physicalCard != null)
                result[i] = String.valueOf(physicalCard.getBlueprintId());
            else
                result[i] = "rules";
        }
        return result;
    }

    private String[] getActionTexts(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = actions.get(i).getText();
        return result;
    }

    protected Action getSelectedAction(String result) throws DecisionResultInvalidException {
        if (result.equals(""))
            throw new DecisionResultInvalidException();

        try {
            int actionIndex = Integer.parseInt(result);
            if (actionIndex < 0 || actionIndex >= _actions.size())
                throw new DecisionResultInvalidException();

            return _actions.get(actionIndex);
        } catch (NumberFormatException exp) {
            throw new DecisionResultInvalidException();
        }
    }
}
