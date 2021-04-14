package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequirementFactory {
    private Map<String, RequirementProducer> requirementProducers = new HashMap<>();

    public RequirementFactory() {
        requirementProducers.put("not", new NotRequirementProducer());
        requirementProducers.put("or", new OrRequirementProducer());
        requirementProducers.put("canspot", new CanSpot());
        requirementProducers.put("cantspot", new CantSpot());
        requirementProducers.put("canspotindeadpile", new CanSpotInDeadPile());
        requirementProducers.put("canspotburdens", new CanSpotBurdens());
        requirementProducers.put("canspotthreats", new CanSpotThreats());
        requirementProducers.put("canspottwilight", new CanSpotTwilight());
        requirementProducers.put("controlssite", new ControlsSite());
        requirementProducers.put("twilightpoollessthan", new TwilightPoolLessThan());
        requirementProducers.put("location", new Location());
        requirementProducers.put("ringison", new RingIsOn());
        requirementProducers.put("phase", new PhaseRequirement());
        requirementProducers.put("ringisactive", new RingIsActive());
        requirementProducers.put("memorymatches", new MemoryMatches());
        requirementProducers.put("memoryis", new MemoryIs());
        requirementProducers.put("cantspotfpcultures", new CantSpotFPCultures());
        requirementProducers.put("haveinitiative", new HaveInitiative());
        requirementProducers.put("fierceskirmish", new FierceSkirmish());
        requirementProducers.put("movecountminimum", new MoveCountMinimum());
        requirementProducers.put("wasassignedtoskirmish", new WasAssignedToSkirmish());
        requirementProducers.put("didwinskirmish", new DidWinSkirmish());
        requirementProducers.put("ismore", new IsMore());
        requirementProducers.put("hasinzonedata", new HasInZoneData());
        requirementProducers.put("isside", new IsSideRequirementProducer());
        requirementProducers.put("isowner", new IsOwnerRequirementProducer());
        requirementProducers.put("perturnlimit", new PerTurnLimit());
        requirementProducers.put("perphaselimit", new PerPhaseLimit());
        requirementProducers.put("cardsinhandmorethan", new CardsInHandMoreThan());
        requirementProducers.put("cardsindeckcount", new CardsInDeckCount());
        requirementProducers.put("playedcardthisphase", new PlayedCardThisPhase());
        requirementProducers.put("sarumanfirstsentenceactive", new SarumanFirstSentenceActive());
    }

    public Requirement getRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(object.get("type"), "type");
        final RequirementProducer requirementProducer = requirementProducers.get(type.toLowerCase());
        if (requirementProducer == null)
            throw new InvalidCardDefinitionException("Unable to resolve requirement of type: " + type);
        return requirementProducer.getPlayRequirement(object, environment);
    }

    public Requirement[] getRequirements(JSONObject[] object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        Requirement[] result = new Requirement[object.length];
        for (int i = 0; i < object.length; i++)
            result[i] = getRequirement(object[i], environment);
        return result;
    }
}
