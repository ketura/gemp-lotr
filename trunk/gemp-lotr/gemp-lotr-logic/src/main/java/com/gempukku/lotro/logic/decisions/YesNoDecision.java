package com.gempukku.lotro.logic.decisions;

public class YesNoDecision extends MultipleChoiceAwaitingDecision {
    public YesNoDecision(String text) {
        super(1, text, new String[]{"Yes", "No"});
    }

    @Override
    protected final void validDecisionMade(int index, String result) {
        if (index == 0)
            yes();
        else
            no();
    }

    protected void yes() {

    }

    protected void no() {

    }
}
