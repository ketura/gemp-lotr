package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public abstract class CardActionSelectionDecision extends AbstractAwaitingDecision {
    private LotroGame _game;
    private List<? extends Action> _actions;

    public CardActionSelectionDecision(LotroGame game, int decisionId, String text, List<? extends Action> actions) {
        super(decisionId, text, AwaitingDecisionType.CARD_ACTION_CHOICE);
        _game = game;
        _actions = actions;

        setParam("actionId", getActionIds(actions));
        setParam("cardId", getCardIds(actions));
        setParam("blueprintId", getBlueprintIdsForVirtualActions(actions));
        setParam("actionText", getActionTexts(actions));
    }

    private String[] getActionIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = String.valueOf(i);
        return result;
    }

    private String[] getBlueprintIdsForVirtualActions(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++) {
            Action action = actions.get(i);
            if (action.isVirtualCardAction())
                result[i] = String.valueOf(action.getActionSource().getBlueprintId());
            else
                result[i] = "inPlay";
        }
        return result;
    }

    private String[] getCardIds(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = String.valueOf(actions.get(i).getActionAttachedToCard().getCardId());
        return result;
    }

    private String[] getActionTexts(List<? extends Action> actions) {
        String[] result = new String[actions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = actions.get(i).getText(_game);
        return result;
    }

    protected Action getSelectedAction(String result) throws DecisionResultInvalidException {
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
