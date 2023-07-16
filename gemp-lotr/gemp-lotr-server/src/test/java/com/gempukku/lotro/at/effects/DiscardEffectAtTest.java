package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.TriggerConditions;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.game.effects.EffectResult;
import com.gempukku.lotro.game.timing.results.DiscardCardsFromPlayResult;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DiscardEffectAtTest extends AbstractAtTest {
    @Test
    public void attachedCardGetsDiscarded() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final LotroPhysicalCardImpl merry = new LotroPhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final LotroPhysicalCardImpl hobbitSword = new LotroPhysicalCardImpl(101, "1_299", P1, _cardLibrary.getLotroCardBlueprint("1_299"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, hobbitSword, merry);

        final Set<PhysicalCard> discardedFromPlay = new HashSet<>();

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Filters.any)) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            discardedFromPlay.add(discardResult.getDiscardedCard());
                            return null;
                        }
                        return null;
                    }
                });

        DiscardCardsFromPlayEffect discardEffect = new DiscardCardsFromPlayEffect(merry.getOwner(), merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, discardEffect);

        assertTrue(discardEffect.wasCarriedOut());

        assertEquals(2, discardedFromPlay.size());
        assertTrue(discardedFromPlay.contains(merry));
        assertTrue(discardedFromPlay.contains(hobbitSword));

        assertEquals(Zone.DISCARD, merry.getZone());
        assertEquals(Zone.DISCARD, hobbitSword.getZone());
    }

    @Test
    public void attachedCardToAttachedCardGetsDiscarded() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final LotroPhysicalCardImpl merry = new LotroPhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final LotroPhysicalCardImpl alatar = new LotroPhysicalCardImpl(101, "13_28", P1, _cardLibrary.getLotroCardBlueprint("13_28"));
        final LotroPhysicalCardImpl whisperInTheDark = new LotroPhysicalCardImpl(101, "18_77", P1, _cardLibrary.getLotroCardBlueprint("18_77"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, alatar, merry);
        _game.getGameState().attachCard(_game, whisperInTheDark, alatar);

        final Set<PhysicalCard> discardedFromPlay = new HashSet<>();

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Filters.any)) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            discardedFromPlay.add(discardResult.getDiscardedCard());
                            return null;
                        }
                        return null;
                    }
                });

        DiscardCardsFromPlayEffect discardEffect = new DiscardCardsFromPlayEffect(merry.getOwner(), merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, discardEffect);

        assertTrue(discardEffect.wasCarriedOut());

        assertEquals(3, discardedFromPlay.size());
        assertTrue(discardedFromPlay.contains(merry));
        assertTrue(discardedFromPlay.contains(alatar));
        assertTrue(discardedFromPlay.contains(whisperInTheDark));

        assertEquals(Zone.DISCARD, merry.getZone());
        assertEquals(Zone.DISCARD, alatar.getZone());
        assertEquals(Zone.DISCARD, whisperInTheDark.getZone());
    }

    @Test
    public void stackedCardGetsDiscarded() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        skipMulligans();

        final LotroPhysicalCardImpl merry = new LotroPhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final LotroPhysicalCardImpl hobbitSword = new LotroPhysicalCardImpl(101, "1_299", P1, _cardLibrary.getLotroCardBlueprint("1_299"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().stackCard(_game, hobbitSword, merry);

        final Set<PhysicalCard> discardedFromPlay = new HashSet<>();

        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Filters.any)) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            discardedFromPlay.add(discardResult.getDiscardedCard());
                            return null;
                        }
                        return null;
                    }
                });

        DiscardCardsFromPlayEffect discardEffect = new DiscardCardsFromPlayEffect(merry.getOwner(), merry, merry);

        carryOutEffectInPhaseActionByPlayer(P1, discardEffect);

        assertTrue(discardEffect.wasCarriedOut());

        assertEquals(1, discardedFromPlay.size());
        assertTrue(discardedFromPlay.contains(merry));

        assertEquals(Zone.DISCARD, merry.getZone());
        assertEquals(Zone.DISCARD, hobbitSword.getZone());
    }

}
