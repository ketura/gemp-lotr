package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ModifierSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModifierSourceFactory {
    private Map<String, ModifierSourceProducer> modifierProducers = new HashMap<>();

    public ModifierSourceFactory() {
        modifierProducers.put("addkeyword", new AddKeyword());
        modifierProducers.put("removekeyword", new RemoveKeyword());
        modifierProducers.put("archerytotal", new ArcheryTotal());
        modifierProducers.put("cantbeexerted", new CantBeExerted());
        modifierProducers.put("cantbeoverwhelmedmultiplier", new CantBeOverwhelmedMultiplier());
        modifierProducers.put("canttakemorewoundsthan", new CantTakeMoreWoundsThan());
        modifierProducers.put("canttakewounds", new CantTakeWounds());
        modifierProducers.put("modifycost", new ModifyCost());
        modifierProducers.put("modifyroamingpenalty", new ModifyRoamingPenalty());
        modifierProducers.put("modifystrength", new ModifyStrength());
        modifierProducers.put("opponentmaynotdiscard", new OpponentMayNotDiscard());
        modifierProducers.put("addnotwilightforcompanionmove", new AddNoTwilightForCompanionMove());
        modifierProducers.put("modifyarcherytotal", new ModifyArcheryTotal());
        modifierProducers.put("modifysitenumber", new ModifySiteNumber());
        modifierProducers.put("cantusespecialabilities", new CantUseSpecialAbilities());
        modifierProducers.put("skipphase", new SkipPhase());
    }

    public ModifierSource getModifier(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(object.get("type"), "type");
        final ModifierSourceProducer modifierSourceProducer = modifierProducers.get(type.toLowerCase());
        if (modifierSourceProducer == null)
            throw new InvalidCardDefinitionException("Unable to resolve modifier of type: " + type);
        return modifierSourceProducer.getModifierSource(object, environment);
    }
}
