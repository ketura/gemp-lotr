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
        int index = resultIndex(result);
        if (index < 0)
            throw new DecisionResultInvalidException();

        validDecisionMade(index, result);
    }

    private int resultIndex(String result) {
        for (int i = 0; i < _possibleResults.length; i++) {
            if (_possibleResults[i].equals(result))
                return i;
        }
        return -1;
    }
}
