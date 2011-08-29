package com.gempukku.lotro.logic.decisions;

public abstract class IntegerAwaitingDecision extends AbstractAwaitingDecision {
    private Integer _min;
    private Integer _max;

    public IntegerAwaitingDecision(int id, String text) {
        this(id, text, null);
    }

    public IntegerAwaitingDecision(int id, String text, Integer min) {
        this(id, text, min, null);
    }

    public IntegerAwaitingDecision(int id, String text, Integer min, Integer max) {
        super(id, text, AwaitingDecisionType.INTEGER);
        _min = min;
        _max = max;
        if (min != null)
            setParam("min", min.toString());
        if (max != null)
            setParam("max", max.toString());
    }

    protected int getValidatedResult(String result) throws DecisionResultInvalidException {
        try {
            int value = Integer.parseInt(result);
            if (_min != null && _min > value)
                throw new DecisionResultInvalidException();
            if (_max != null && _max < value)
                throw new DecisionResultInvalidException();

            return value;
        } catch (NumberFormatException exp) {
            throw new DecisionResultInvalidException();
        }
    }
}
