package com.gempukku.lotro.logic.decisions;

public abstract class ForEachBurdenYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachBurdenYouSpotDecision(int id, int burdens) {
        super(id, "Choose number of burdens you wish to spot", 0, burdens, burdens);
    }

    @Override
    public void decisionMade(String result) throws DecisionResultInvalidException {
        burdensSpotted(getValidatedResult(result));
    }

    protected abstract void burdensSpotted(int burdensSpotted);
}