package com.gempukku.lotro.game.decisions;

public abstract class ForEachTwilightTokenYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachTwilightTokenYouSpotDecision(int id, int twilightTokens) {
        super(id, "Choose number of twilight tokens you wish to spot", 0, twilightTokens, twilightTokens);
    }

    @Override
    public void decisionMade(String result) throws DecisionResultInvalidException {
        twilightTokensSpotted(getValidatedResult(result));
    }

    protected abstract void twilightTokensSpotted(int twilightTokensSpotted);
}