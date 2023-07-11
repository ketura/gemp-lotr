package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.TriggerConditions;
import com.gempukku.lotro.game.effects.NegateWoundEffect;
import com.gempukku.lotro.game.effects.PreventCardEffect;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.PhysicalCardImpl;
import com.gempukku.lotro.game.actions.RequiredTriggerAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.WoundCharactersEffect;
import com.gempukku.lotro.game.modifiers.ModifierFlag;
import com.gempukku.lotro.game.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.EffectResult;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class WoundEffectAtTest extends AbstractAtTest {
    @Test
    public void woundSuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        final AtomicInteger triggerCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachWounded(game, effectResult, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);

        WoundCharactersEffect woundEffect = new WoundCharactersEffect(merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, woundEffect);

        assertEquals(1, _game.getGameState().getWounds(merry));
        assertTrue(woundEffect.wasCarriedOut());

        assertEquals(1, triggerCount.get());
    }

    @Test
    public void woundUnsuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        final AtomicInteger triggerCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachWounded(game, effectResult, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        _game.getGameState().addCardToZone(_game, merry, Zone.DISCARD);

        WoundCharactersEffect woundEffect = new WoundCharactersEffect(merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, woundEffect);

        assertEquals(0, _game.getGameState().getWounds(merry));
        assertFalse(woundEffect.wasCarriedOut());

        assertEquals(0, triggerCount.get());
    }

    @Test
    public void woundPrevention() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final PhysicalCardImpl pippin = new PhysicalCardImpl(102, "1_306", P1, _cardLibrary.getLotroCardBlueprint("1_306"));

        final AtomicInteger triggerCount = new AtomicInteger(0);
        final AtomicInteger preventCount = new AtomicInteger(0);

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
                        if (TriggerConditions.isGettingWounded(effect, game, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new PreventCardEffect((WoundCharactersEffect) effect, merry));
                            action.appendEffect(
                                    new IncrementEffect(preventCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }

                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachWounded(game, effectResult, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);

        WoundCharactersEffect woundEffect = new WoundCharactersEffect(merry, merry, pippin);

        carryOutEffectInPhaseActionByPlayer(P1, woundEffect);

        assertEquals(0, _game.getGameState().getWounds(merry));
        assertEquals(1, _game.getGameState().getWounds(pippin));
        assertFalse(woundEffect.wasCarriedOut());

        assertEquals(0, triggerCount.get());
        assertEquals(1, preventCount.get());
    }

    @Test
    public void insteadOfWoundWorksIfCantPrevent() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        final AtomicInteger triggerCount = new AtomicInteger(0);
        final AtomicInteger negateCount = new AtomicInteger(0);

        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                new SpecialFlagModifier(null, ModifierFlag.CANT_PREVENT_WOUNDS));

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
                        if (TriggerConditions.isGettingWounded(effect, game, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new NegateWoundEffect((WoundCharactersEffect) effect, merry));
                            action.appendEffect(
                                    new IncrementEffect(negateCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }

                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachWounded(game, effectResult, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);

        WoundCharactersEffect woundEffect = new WoundCharactersEffect(merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, woundEffect);

        assertEquals(0, _game.getGameState().getWounds(merry));
        assertFalse(woundEffect.wasCarriedOut());

        assertEquals(0, triggerCount.get());
        assertEquals(1, negateCount.get());
    }

    @Test
    public void cantPrevent() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        final AtomicInteger triggerCount = new AtomicInteger(0);
        final AtomicInteger preventCount = new AtomicInteger(0);

        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                new SpecialFlagModifier(null, ModifierFlag.CANT_PREVENT_WOUNDS));

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
                        if (TriggerConditions.isGettingWounded(effect, game, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new PreventCardEffect((WoundCharactersEffect) effect, merry));
                            action.appendEffect(
                                    new IncrementEffect(preventCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }

                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachWounded(game, effectResult, merry)) {
                            RequiredTriggerAction action = new RequiredTriggerAction(merry);
                            action.appendEffect(
                                    new IncrementEffect(triggerCount));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);

        WoundCharactersEffect woundEffect = new WoundCharactersEffect(merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, woundEffect);

        assertEquals(1, _game.getGameState().getWounds(merry));
        assertTrue(woundEffect.wasCarriedOut());

        assertEquals(1, triggerCount.get());
        assertEquals(1, preventCount.get());
    }
}
