package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TriggerCheckerFactory {
    private Map<String, TriggerCheckerProducer> triggerCheckers = new HashMap<>();

    public TriggerCheckerFactory() {
        triggerCheckers.put("played", new PlayedTriggerCheckerProducer());
        triggerCheckers.put("abouttodiscard", new AboutToDiscardFromPlay());
        triggerCheckers.put("losesskirmish", new LosesSkirmish());
        triggerCheckers.put("winsskirmish", new WinsSkirmish());
        triggerCheckers.put("startofturn", new StartOfTurn());
        triggerCheckers.put("startofphase", new StartOfPhase());
        triggerCheckers.put("abouttotakewound", new AboutToTakeWound());
        triggerCheckers.put("condition", new ConditionTrigger());
        triggerCheckers.put("assignedtoskirmish", new AssignedToSkirmish());
        triggerCheckers.put("revealcardfromtopofdrawdeck", new RevealCardFromTopOfDrawDeck());
        triggerCheckers.put("movesto", new MovesTo());
        triggerCheckers.put("moves", new Moves());
        triggerCheckers.put("losesinitiative", new LosesInitiative());
    }

    public TriggerChecker getTriggerChecker(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String triggerType = FieldUtils.getString(object.get("type"), "type");
        final TriggerCheckerProducer triggerCheckerProducer = triggerCheckers.get(triggerType.toLowerCase());
        if (triggerCheckerProducer == null)
            throw new InvalidCardDefinitionException("Unable to find trigger of type: " + triggerType);
        return triggerCheckerProducer.getTriggerChecker(object, environment);
    }
}
