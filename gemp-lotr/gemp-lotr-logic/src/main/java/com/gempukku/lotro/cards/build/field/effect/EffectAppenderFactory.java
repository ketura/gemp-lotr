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
        effectAppenderProducers.put("removefromthegame", new RemoveFromTheGame());
        effectAppenderProducers.put("preventdiscard", new PreventCardEffectAppender());
        effectAppenderProducers.put("preventheal", new PreventCardEffectAppender());
        effectAppenderProducers.put("exert", new Exert());
        effectAppenderProducers.put("exhaust", new Exhaust());
        effectAppenderProducers.put("modifystrength", new ModifyStrength());
        effectAppenderProducers.put("addkeyword", new AddKeyword());
        effectAppenderProducers.put("modifyarcherytotal", new ModifyArcheryTotal());
        effectAppenderProducers.put("modifysitenumber", new ModifySiteNumber());
        effectAppenderProducers.put("addburdens", new AddBurdens());
        effectAppenderProducers.put("discardtopcardsfromdeck", new DiscardTopCardFromDeck());
        effectAppenderProducers.put("addtwilight", new AddTwilight());
        effectAppenderProducers.put("wound", new Wound());
        effectAppenderProducers.put("heal", new Heal());
        effectAppenderProducers.put("replaceinskirmish", new ReplaceInSkirmish());
        effectAppenderProducers.put("replaceinassignment", new ReplaceInAssignment());
        effectAppenderProducers.put("canttakemorewoundsthan", new CantTakeMoreWoundsThan());
        effectAppenderProducers.put("canttakewounds", new CantTakeWounds());
        effectAppenderProducers.put("choice", new Choice());
        effectAppenderProducers.put("assignfpcharactertoskirmish", new AssignFpCharacterToSkirmish());
        effectAppenderProducers.put("cancelallassignments", new CancelAllAssignments());
        effectAppenderProducers.put("putcardsfromhandonbottomofdeck", new PutCardsFromHandOnBottomOfDeck());
        effectAppenderProducers.put("putcardsfromhandontopofdeck", new PutCardsFromHandOnTopOfDeck());
        effectAppenderProducers.put("doesnotaddtoarcherytotal", new DoesNotAddToArcheryTotal());
        effectAppenderProducers.put("negatewound", new NegateWound());
        effectAppenderProducers.put("discardcardatrandomfromhand", new DiscardCardAtRandomFromHand());
        effectAppenderProducers.put("putonring", new PutOnRing());
        effectAppenderProducers.put("takeoffring", new TakeOffRing());
        effectAppenderProducers.put("discardstackedcards", new DiscardStackedCards());
        effectAppenderProducers.put("memorize", new MemorizeActive());
        effectAppenderProducers.put("memorizenumber", new MemorizeNumber());
        effectAppenderProducers.put("preventwound", new PreventWound());
        effectAppenderProducers.put("preventexert", new PreventExert());
        effectAppenderProducers.put("putstackedcardsintohand", new PutStackedCardsIntoHand());
        effectAppenderProducers.put("conditional", new ConditionEffect());
        effectAppenderProducers.put("drawcards", new DrawCards());
        effectAppenderProducers.put("removeburdens", new RemoveBurdens());
        effectAppenderProducers.put("stackcardsfromhand", new StackCardsFromHand());
        effectAppenderProducers.put("stackcardsfromdiscard", new StackCardsFromDiscard());
        effectAppenderProducers.put("putcardsfromdiscardintohand", new PutCardsFromDiscardIntoHand());
        effectAppenderProducers.put("addtrigger", new AddTrigger());
        effectAppenderProducers.put("stackplayedevent", new StackPlayedEvent());
        effectAppenderProducers.put("cancelevent", new CancelEvent());
        effectAppenderProducers.put("play", new PlayCardFromHand());
        effectAppenderProducers.put("playcardfromdiscard", new PlayCardFromDiscard());
        effectAppenderProducers.put("playcardfromdrawdeck", new PlayCardFromDrawDeck());
        effectAppenderProducers.put("playcardfromstacked", new PlayCardFromStacked());
        effectAppenderProducers.put("reducearcherytotal", new ReduceArcheryTotal());
        effectAppenderProducers.put("revealtopcardsofdrawdeck", new RevealTopCardsOfDrawDeck());
        effectAppenderProducers.put("revealbottomcardsofdrawdeck", new RevealBottomCardsOfDrawDeck());
        effectAppenderProducers.put("optional", new Optional());
        effectAppenderProducers.put("costtoeffect", new CostToEffect());
        effectAppenderProducers.put("spot", new Spot());
        effectAppenderProducers.put("choosehowmanytospot", new ChooseHowManyToSpot());
        effectAppenderProducers.put("choosehowmanyburdenstospot", new ChooseHowManyBurdensToSpot());
        effectAppenderProducers.put("reordertopcardsofdrawdeck", new ReorderTopCardsOfDrawDeck());
        effectAppenderProducers.put("cantbeassignedtoskirmish", new CantBeAssignedToSkirmish());
        effectAppenderProducers.put("preventable", new PreventableAppenderProducer());
        effectAppenderProducers.put("cantbeoverwhelmedmultiplier", new CantBeOverwhelmedMultiplier());
        effectAppenderProducers.put("cancelskirmish", new CancelSkirmish());
        effectAppenderProducers.put("discardfromhand", new DiscardFromHand());
        effectAppenderProducers.put("chooseandaddtwilight", new ChooseAndAddTwilight());
        effectAppenderProducers.put("chooseandremovetwilight", new ChooseAndRemoveTwilight());
        effectAppenderProducers.put("removetwilight", new RemoveTwilight());
        effectAppenderProducers.put("removethreats", new RemoveThreats());
        effectAppenderProducers.put("stackcards", new StackCardsFromPlay());
        effectAppenderProducers.put("removekeyword", new RemoveKeyword());
        effectAppenderProducers.put("putcardsfromdiscardontopofdrawdeck", new PutCardsFromDiscardOnTopOfDrawDeck());
        effectAppenderProducers.put("putcardsfromdiscardonbottomofdrawdeck", new PutCardsFromDiscardOnBottomOfDrawDeck());
        effectAppenderProducers.put("lookattopcardsofdrawdeck", new LookAtTopCardsOfDrawDeck());
        effectAppenderProducers.put("addthreats", new AddThreats());
        effectAppenderProducers.put("sideplayercantplayphaseeventsorusephasespecialabilities", new SidePlayerCantPlayPhaseEventsOrUsePhaseSpecialAbilities());
        effectAppenderProducers.put("shadowcanthaveinitiative", new ShadowCantHaveInitiative());
        effectAppenderProducers.put("allycanparticipateinarcheryfireandskirmishes", new AllyCanParticipateInArcheryFireAndSkirmishes());
        effectAppenderProducers.put("allycanparticipateinskirmishes", new AllyCanParticipateInSkirmishes());
        effectAppenderProducers.put("revealrandomcardsfromhand", new RevealRandomCardsFromHand());
        effectAppenderProducers.put("putcardsfromdeckintohand", new PutCardsFromDeckIntoHand());
        effectAppenderProducers.put("addmodifier", new AddModifier());
        effectAppenderProducers.put("playnextsite", new PlayNextSite());
        effectAppenderProducers.put("playsite", new PlaySite());
        effectAppenderProducers.put("duplicate", new Duplicate());
        effectAppenderProducers.put("revealcardsfromhand", new RevealCardsFromHand());
        effectAppenderProducers.put("revealcards", new RevealCards());
        effectAppenderProducers.put("addtokens", new AddTokens());
        effectAppenderProducers.put("removetokens", new RemoveTokens());
        effectAppenderProducers.put("chooseandremovetokens", new ChooseAndRemoveTokens());
        effectAppenderProducers.put("transfer", new Transfer());
        effectAppenderProducers.put("transfertosupport", new TransferToSupport());
        effectAppenderProducers.put("transferfromdiscard", new TransferFromDiscard());
        effectAppenderProducers.put("foreachshadowplayer", new ForEachShadowPlayer());
        effectAppenderProducers.put("returntohand", new ReturnToHand());
        effectAppenderProducers.put("reconcilehand", new ReconcileHand());
        effectAppenderProducers.put("shufflecardsfromdiscardintodrawdeck", new ShuffleCardsFromDiscardIntoDrawDeck());
        effectAppenderProducers.put("chooseactivecards", new ChooseActiveCards());
        effectAppenderProducers.put("choosecardsfromdiscard", new ChooseCardsFromDiscard());
        effectAppenderProducers.put("putplayedeventonbottomofdrawdeck", new PutPlayedEventOnBottomOfDrawDeck());
        effectAppenderProducers.put("putplayedeventintohand", new PutPlayedEventIntoHand());
        effectAppenderProducers.put("removecardsindiscardfromgame", new RemoveCardsInDiscardFromGame());
        effectAppenderProducers.put("makeselfringbearer", new MakeSelfRingBearer());
        effectAppenderProducers.put("choosearace", new ChooseARace());
        effectAppenderProducers.put("chooseyesorno", new ChooseYesOrNo());
        effectAppenderProducers.put("chooseakeyword", new ChooseAKeyword());
        effectAppenderProducers.put("storewhileinzone", new StoreWhileInZone());
        effectAppenderProducers.put("resetwhileinzonedata", new ResetWhileInZoneData());
        effectAppenderProducers.put("appendcardidstowhileinzone", new AppendCardIdsToWhileInZone());
        effectAppenderProducers.put("corruptringbearer", new CorruptRingBearer());
        effectAppenderProducers.put("kill", new Kill());
        effectAppenderProducers.put("endphase", new EndPhase());
        effectAppenderProducers.put("takecontrolofsite", new TakeControlOfSite());
        effectAppenderProducers.put("liberatesite", new LiberateSite());
        effectAppenderProducers.put("filtercardsinmemory", new FilterCardsInMemory());
        effectAppenderProducers.put("discardcardsfromdrawdeck", new DiscardCardsFromDrawDeck());
        effectAppenderProducers.put("incrementperturnlimit", new IncrementPerTurnLimit());
        effectAppenderProducers.put("incrementperphaselimit", new IncrementPerPhaseLimit());
        effectAppenderProducers.put("revealhand", new RevealHand());
        effectAppenderProducers.put("lookathand", new LookAtHand());
        effectAppenderProducers.put("placenowoundforexert", new PlaceNoWoundForExert());
        effectAppenderProducers.put("shufflehandintodrawdeck", new ShuffleHandIntoDrawDeck());
        effectAppenderProducers.put("getcardsfromtopofdeck", new GetCardsFromTopOfDeck());
        effectAppenderProducers.put("removetext", new RemoveText());
        effectAppenderProducers.put("stackcardsfromdeck", new StackCardsFromDeck());
        effectAppenderProducers.put("foreachplayer", new ForEachPlayer());
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
