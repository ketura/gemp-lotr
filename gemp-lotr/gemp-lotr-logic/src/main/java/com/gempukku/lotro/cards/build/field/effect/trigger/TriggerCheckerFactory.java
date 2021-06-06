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
        triggerCheckers.put("playedfromstacked", new PlayedFromStacked());
        triggerCheckers.put("abouttodiscard", new AboutToDiscardFromPlay());
        triggerCheckers.put("abouttoheal", new AboutToHeal());
        triggerCheckers.put("losesskirmish", new LosesSkirmish());
        triggerCheckers.put("winsskirmish", new WinsSkirmish());
        triggerCheckers.put("cancelledskirmish", new CancelledSkirmish());
        triggerCheckers.put("startofturn", new StartOfTurn());
        triggerCheckers.put("endofturn", new EndOfTurn());
        triggerCheckers.put("startofphase", new StartOfPhase());
        triggerCheckers.put("endofphase", new EndOfPhase());
        triggerCheckers.put("abouttotakewound", new AboutToTakeWound());
        triggerCheckers.put("abouttoexert", new AboutToExert());
        triggerCheckers.put("abouttobekilled", new AboutToBeKilled());
        triggerCheckers.put("condition", new ConditionTrigger());
        triggerCheckers.put("assignedtoskirmish", new AssignedToSkirmish());
        triggerCheckers.put("revealcardfromtopofdrawdeck", new RevealCardFromTopOfDrawDeck());
        triggerCheckers.put("movesfrom", new MovesFrom());
        triggerCheckers.put("moves", new Moves());
        triggerCheckers.put("movesto", new MovesTo());
        triggerCheckers.put("losesinitiative", new LosesInitiative());
        triggerCheckers.put("takeswound", new TakesWound());
        triggerCheckers.put("exerts", new Exerts());
        triggerCheckers.put("discarded", new Discarded());
        triggerCheckers.put("discardfromhand", new DiscardFromHand());
        triggerCheckers.put("putsonring", new PutsOnRing());
        triggerCheckers.put("addsburden", new AddsBurden());
        triggerCheckers.put("removesburden", new RemovesBurden());
        triggerCheckers.put("addsthreat", new AddsThreat());
        triggerCheckers.put("transferred", new Transferred());
        triggerCheckers.put("killed", new Killed());
        triggerCheckers.put("reconciles", new Reconciles());
        triggerCheckers.put("usesspecialability", new UsesSpecialAbility());
    }

    public TriggerChecker getTriggerChecker(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String triggerType = FieldUtils.getString(object.get("type"), "type");
        if (triggerType == null)
            throw new InvalidCardDefinitionException("Trigger type not defined");
        final TriggerCheckerProducer triggerCheckerProducer = triggerCheckers.get(triggerType.toLowerCase());
        if (triggerCheckerProducer == null)
            throw new InvalidCardDefinitionException("Unable to find trigger of type: " + triggerType);
        return triggerCheckerProducer.getTriggerChecker(object, environment);
    }
}
