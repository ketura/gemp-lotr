package com.gempukku.lotro.cards.decisions;

import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;

import java.util.Map;

public abstract class ForEachBurdenYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachBurdenYouSpotDecision(int id, int burdens) {
        super(id, "Choose number of burdens you wish to spot", 0, burdens);
    }

    @Override
    public Map<String, Object> getDecisionParameters() {
        Map<String, Object> result = super.getDecisionParameters();
        result.put("defaultValue", result.get("max"));
        return result;
    }

    @Override
    public void decisionMade(String result) throws DecisionResultInvalidException {
        burdensSpotted(getValidatedResult(result));
    }

    protected abstract void burdensSpotted(int burdensSpotted);
}