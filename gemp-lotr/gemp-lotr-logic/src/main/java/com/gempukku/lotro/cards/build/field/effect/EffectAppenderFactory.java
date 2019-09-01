package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.*;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EffectAppenderFactory {
    private Map<String, EffectAppenderProducer> effectAppenderProducers = new HashMap<>();

    public EffectAppenderFactory() {
        effectAppenderProducers.put("stacktopcardsofdrawdeck", new StackTopCardsOfDrawDeck());
        effectAppenderProducers.put("discard", new DiscardFromPlay());
        effectAppenderProducers.put("preventdiscard", new PreventCardEffectAppender());
        effectAppenderProducers.put("exert", new Exert());
        effectAppenderProducers.put("modifystrength", new ModifyStrength());
        effectAppenderProducers.put("addkeyword", new AddKeyword());
        effectAppenderProducers.put("modifyarcherytotal", new ModifyArcheryTotal());
        effectAppenderProducers.put("addburden", new AddBurden());
        effectAppenderProducers.put("discardtopcardsfromdeck", new DiscardTopCardFromDeck());
        effectAppenderProducers.put("addtwilight", new AddTwilight());
        effectAppenderProducers.put("wound", new Wound());
        effectAppenderProducers.put("heal", new Heal());
        effectAppenderProducers.put("replaceinskirmish", new ReplaceInSkirmish());
        effectAppenderProducers.put("canttakemorewoundsthan", new CantTakeMoreWoundsThan());
        effectAppenderProducers.put("choice", new Choice());
        effectAppenderProducers.put("assigntoskirmishagainstminion", new AssignToSkirmishAgainstMinion());
        effectAppenderProducers.put("putcardsfromhandonbottomofdeck", new PutCardsFromHandOnBottomOfDeck());
        effectAppenderProducers.put("doesnotaddtoarcherytotal", new DoesNotAddToArcheryTotal());
        effectAppenderProducers.put("negatewound", new NegateWound());
        effectAppenderProducers.put("discardcardatrandomfromhand", new DiscardCardAtRandomFromHand());
        effectAppenderProducers.put("putonring", new PutOnRing());
        effectAppenderProducers.put("discardstackedcards", new DiscardStackedCards());
        effectAppenderProducers.put("memorize", new Memorize());
        effectAppenderProducers.put("preventwound", new PreventWound());
        effectAppenderProducers.put("putstackedcardsintohand", new PutStackedCardsIntoHand());
        effectAppenderProducers.put("conditional", new ConditionEffect());
        effectAppenderProducers.put("drawcards", new DrawCards());
        effectAppenderProducers.put("removeburdens", new RemoveBurdens());
        effectAppenderProducers.put("stackcardsfromhand", new StackCardsFromHand());
        effectAppenderProducers.put("stackcardsfromdiscard", new StackCardsFromDiscard());
        effectAppenderProducers.put("putcardsfromdiscardintohand", new PutCardsFromDiscardIntoHand());
        effectAppenderProducers.put("addtrigger", new AddTrigger());
        effectAppenderProducers.put("stackplayedevent", new StackPlayedEvent());
        effectAppenderProducers.put("playcardfromdiscard", new PlayCardFromDiscard());
        effectAppenderProducers.put("reducearcherytotal", new ReduceArcheryTotal());
        effectAppenderProducers.put("revealtopcardsofdrawdeck", new RevealTopCardsOfDrawDeck());
        effectAppenderProducers.put("optional", new Optional());
        effectAppenderProducers.put("costtoeffect", new CostToEffect());
        effectAppenderProducers.put("spot", new Spot());
        effectAppenderProducers.put("choosehowmanytospot", new ChooseHowManyToSpot());
        effectAppenderProducers.put("reordertopcardsofdrawdeck", new ReorderTopCardsOfDrawDeck());
        effectAppenderProducers.put("cantbeassignedtoskirmish", new CantBeAssignedToSkirmish());
        effectAppenderProducers.put("preventable", new PreventableAppenderProducer());
        effectAppenderProducers.put("cantbeoverwhelmedmultiplier", new CantBeOverwhelmedMultiplier());
        effectAppenderProducers.put("play", new PlayCardFromHand());
        effectAppenderProducers.put("cancelskirmish", new CancelSkirmish());
        effectAppenderProducers.put("discardfromhand", new CancelSkirmish());
        effectAppenderProducers.put("chooseandremovetwilight", new ChooseAndRemoveTwilight());
        effectAppenderProducers.put("removetwilight", new RemoveTwilight());
        effectAppenderProducers.put("stackcards", new StackCardsFromPlay());
        effectAppenderProducers.put("removekeyword", new RemoveKeyword());
        effectAppenderProducers.put("putcardsfromdiscardontopofdrawdeck", new PutCardsFromDiscardOnTopOfDrawDeck());
        effectAppenderProducers.put("lookattopcardsofdrawdeck", new LookAtTopCardsOfDrawDeck());
    }

    public EffectAppender getEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(effectObject.get("type"), "type");
        final EffectAppenderProducer effectAppenderProducer = effectAppenderProducers.get(type.toLowerCase());
        if (effectAppenderProducer == null)
            throw new InvalidCardDefinitionException("Unable to find effect of type: " + type);
        return effectAppenderProducer.createEffectAppender(effectObject, environment);
    }

    public EffectAppender[] getEffectAppenders(JSONObject[] effectArray, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        EffectAppender[] result = new EffectAppender[effectArray.length];
        for (int i = 0; i < result.length; i++) {
            final String type = FieldUtils.getString(effectArray[i].get("type"), "type");
            final EffectAppenderProducer effectAppenderProducer = effectAppenderProducers.get(type.toLowerCase());
            if (effectAppenderProducer == null)
                throw new InvalidCardDefinitionException("Unable to find effect of type: " + type);
            result[i] = effectAppenderProducer.createEffectAppender(effectArray[i], environment);
        }
        return result;
    }
}
