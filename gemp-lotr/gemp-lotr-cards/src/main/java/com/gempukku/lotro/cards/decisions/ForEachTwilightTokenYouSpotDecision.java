package com.gempukku.lotro.cards.decisions;

import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;

import java.util.Map;

public abstract class ForEachTwilightTokenYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachTwilightTokenYouSpotDecision(int id, int twilightTokens) {
        super(id, "Choose number of twilight tokens you wish to spot", 0, twilightTokens);
    }

    @Override
    public Map<String, Object> getDecisionParameters() {
        Map<String, Object> result = super.getDecisionParameters();
        result.put("defaultValue", result.get("max"));
        return result;
    }

    @Override
    public void decisionMade(String result) throws DecisionResultInvalidException {
        twilightTokensSpotted(getValidatedResult(result));
    }

    protected abstract void twilightTokensSpotted(int twilightTokensSpotted);
}