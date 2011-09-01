package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public abstract class ActionsSelectionDecision extends AbstractAwaitingDecision {
    private List<? extends Action> _actions;
    private boolean _optional;

    public ActionsSelectionDecision(int decisionId, String text, List<? extends Action> actions, boolean optional) {
        super(decisionId, text, AwaitingDecisionType.ACTION_CHOICE);
        _actions = actions;
        _optional = optional;

        setParam("actionId", getActionIds(actions));
        setParam("cardId", getCardIds(actions));
        setParam("actionText", getActionTexts(actions));
        setParam("optional", String.valueOf(optional));
    }

    private String[] getActionIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = String.valueOf(i);
        return result;
    }

    private String[] getCardIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = String.valueOf(actions.get(i).getActionSource().getCardId());
        return result;
    }

    private String[] getActionTexts(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = actions.get(i).getText();
        return result;
    }

    protected Action getSelectedAction(String result) throws DecisionResultInvalidException {
        if (result.equals("") && !_optional)
            throw new DecisionResultInvalidException();

        if (result.equals(""))
            return null;
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
