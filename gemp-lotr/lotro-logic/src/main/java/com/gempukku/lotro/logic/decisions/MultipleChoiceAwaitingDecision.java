package com.gempukku.lotro.logic.decisions;

public abstract class MultipleChoiceAwaitingDecision extends AbstractAwaitingDecision {
    private String[] _possibleResults;

    public MultipleChoiceAwaitingDecision(int id, String text, String[] possibleResults) {
        super(id, text, AwaitingDecisionType.MULTIPLE_CHOICE);
        _possibleResults = possibleResults;
        setParam("results", _possibleResults);
    }

    protected abstract void validDecisionMade(String result);

    @Override
    public final void decisionMade(String result) throws DecisionResultInvalidException {
        if (result == null || !containsResult(result))
            throw new DecisionResultInvalidException();

        validDecisionMade(result);
    }

    private boolean containsResult(String result) {
        for (String possibleResult : _possibleResults)
            if (possibleResult.equals(result))
                return true;
        return false;
    }
}
