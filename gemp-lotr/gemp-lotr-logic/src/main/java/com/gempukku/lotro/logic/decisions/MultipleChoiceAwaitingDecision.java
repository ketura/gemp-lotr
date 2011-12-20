package com.gempukku.lotro.logic.decisions;

public abstract class MultipleChoiceAwaitingDecision extends AbstractAwaitingDecision {
    private String[] _possibleResults;

    public MultipleChoiceAwaitingDecision(int id, String text, String[] possibleResults) {
        super(id, text, AwaitingDecisionType.MULTIPLE_CHOICE);
        _possibleResults = possibleResults;
        setParam("results", _possibleResults);
    }

    protected abstract void validDecisionMade(int index, String result);

    @Override
    public final void decisionMade(String result) throws DecisionResultInvalidException {
        if (result == null)
            throw new DecisionResultInvalidException();

        try {
            int index = Integer.parseInt(result);
            validDecisionMade(index, _possibleResults[index]);
        } catch (NumberFormatException exp) {
            throw new DecisionResultInvalidException("Unkown response number");
        }
    }
}
