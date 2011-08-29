package com.gempukku.lotro.logic.decisions;

import java.util.Map;

public interface AwaitingDecision {
    public int getAwaitingDecisionId();

    public String getText();

    public AwaitingDecisionType getDecisionType();

    public Map<String, Object> getDecisionParameters();

    public void decisionMade(String result) throws DecisionResultInvalidException;
}
