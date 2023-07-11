package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.TriggerConditions;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.PhysicalCardImpl;
import com.gempukku.lotro.game.actions.RequiredTriggerAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.DrawCardsEffect;
import com.gempukku.lotro.game.effects.PreventEffect;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.EffectResult;
import com.gempukku.lotro.game.timing.Preventable;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class DrawEffectAtTest extends AbstractAtTest {
    @Test
    public void drawingSuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().putCardOnTopOfDeck(merry);

        final AtomicInteger triggerCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachCardDrawn(game, effectResult, P1)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        DrawCardsEffect drawEffect = new DrawCardsEffect(null, P1, 1);

        carryOutEffectInPhaseActionByPlayer(P1, drawEffect);

        assertEquals(1, _game.getGameState().getHand(P1).size());
        assertEquals(0, _game.getGameState().getDeck(P1).size());
        assertTrue(_game.getGameState().getHand(P1).contains(merry));
        assertTrue(drawEffect.wasCarriedOut());

        assertEquals(1, triggerCount.get());
    }

    @Test
    public void drawingMultipleNotSuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().putCardOnTopOfDeck(merry);

        final AtomicInteger triggerCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachCardDrawn(game, effectResult, P1)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        DrawCardsEffect drawEffect = new DrawCardsEffect(null, P1, 2);

        carryOutEffectInPhaseActionByPlayer(P1, drawEffect);

        assertEquals(1, _game.getGameState().getHand(P1).size());
        assertEquals(0, _game.getGameState().getDeck(P1).size());
        assertTrue(_game.getGameState().getHand(P1).contains(merry));
        assertFalse(drawEffect.wasCarriedOut());

        assertEquals(1, triggerCount.get());
    }

    @Test
    public void drawingMultipleSuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final PhysicalCardImpl merry2 = new PhysicalCardImpl(102, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().putCardOnTopOfDeck(merry);
        _game.getGameState().putCardOnTopOfDeck(merry2);

        final AtomicInteger triggerCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachCardDrawn(game, effectResult, P1)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        DrawCardsEffect drawEffect = new DrawCardsEffect(null, P1, 2);

        carryOutEffectInPhaseActionByPlayer(P1, drawEffect);

        assertEquals(2, _game.getGameState().getHand(P1).size());
        assertEquals(0, _game.getGameState().getDeck(P1).size());
        assertTrue(_game.getGameState().getHand(P1).contains(merry));
        assertTrue(_game.getGameState().getHand(P1).contains(merry2));
        assertTrue(drawEffect.wasCarriedOut());

        assertEquals(2, triggerCount.get());
    }

    @Test
    public void insteadOfDraw() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().putCardOnTopOfDeck(merry);

        final AtomicInteger triggerCount = new AtomicInteger(0);
        final AtomicInteger preventCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachCardDrawn(game, effectResult, P1)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }

                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
                        if (TriggerConditions.isDrawingACard(effect, game, P1)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new PreventEffect((Preventable) effect));
                            action.appendEffect(
                                    new IncrementEffect(preventCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        DrawCardsEffect drawEffect = new DrawCardsEffect(null, P1, 1);

        carryOutEffectInPhaseActionByPlayer(P1, drawEffect);

        assertEquals(0, _game.getGameState().getHand(P1).size());
        assertEquals(1, _game.getGameState().getDeck(P1).size());
        assertFalse(drawEffect.wasCarriedOut());

        assertEquals(0, triggerCount.get());
        assertEquals(1, preventCount.get());
    }
}
