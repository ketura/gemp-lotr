package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.*;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EffectAppenderFactory {
    private Map<String, EffectAppenderProducer> effectAppenderProducers = new HashMap<>();

    public EffectAppenderFactory() {
        effectAppenderProducers.put("stacktopcardofdrawdeck", new StackTopCardOfDrawDeck());
        effectAppenderProducers.put("discard", new DiscardFromPlay());
        effectAppenderProducers.put("preventdiscard", new PreventCardEffectAppender());
        effectAppenderProducers.put("exert", new Exert());
        effectAppenderProducers.put("modifystrength", new ModifyStrength());
        effectAppenderProducers.put("addkeyword", new AddKeyword());
        effectAppenderProducers.put("modifyarcherytotal", new ModifyArcheryTotal());
        effectAppenderProducers.put("addburden", new AddBurden());
        effectAppenderProducers.put("discardtopcardfromdeck", new DiscardTopCardFromDeck());
        effectAppenderProducers.put("addtwilight", new AddTwilight());
        effectAppenderProducers.put("wound", new Wound());
        effectAppenderProducers.put("heal", new Heal());
        effectAppenderProducers.put("replaceinskirmish", new ReplaceInSkirmish());
        effectAppenderProducers.put("canttakemorewoundsthan", new CantTakeMoreWoundsThan());
        effectAppenderProducers.put("choice", new Choice());
        effectAppenderProducers.put("assigntoskirmishagainstminion", new AssignToSkirmishAgainstMinion());
        effectAppenderProducers.put("putcardfromhandonbottomofdeck", new PutCardFromHandOnBottomOfDeck());
        effectAppenderProducers.put("doesnotaddtoarcherytotal", new DoesNotAddToArcheryTotal());
        effectAppenderProducers.put("negatewound", new NegateWound());
        effectAppenderProducers.put("discardcardatrandomfromhand", new DiscardCardAtRandomFromHand());
        effectAppenderProducers.put("putonring", new PutOnRing());
        effectAppenderProducers.put("discardstackedcards", new DiscardStackedCards());
        effectAppenderProducers.put("memorize", new Memorize());
        effectAppenderProducers.put("preventwound", new PreventWound());
        effectAppenderProducers.put("putstackedcardsintohand", new PutStackedCardsIntoHand());
        effectAppenderProducers.put("condition", new ConditionEffect());
        effectAppenderProducers.put("drawcards", new DrawCards());
    }

    public EffectAppender getEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(effectObject.get("type"), "type");
        final EffectAppenderProducer effectAppenderProducer = effectAppenderProducers.get(type.toLowerCase());
        if (effectAppenderProducer == null)
            throw new InvalidCardDefinitionException("Unable to find effect of type: " + type);
        return effectAppenderProducer.createEffectAppender(effectObject, environment);
    }

    public Requirement getCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(effectObject.get("type"), "type");
        final EffectAppenderProducer effectAppenderProducer = effectAppenderProducers.get(type.toLowerCase());
        if (effectAppenderProducer == null)
            throw new InvalidCardDefinitionException("Unable to find effect of type: " + type);
        return effectAppenderProducer.createCostRequirement(effectObject, environment);
    }
}
