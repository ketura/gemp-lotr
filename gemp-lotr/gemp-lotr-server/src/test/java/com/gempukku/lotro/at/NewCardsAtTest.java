package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class NewCardsAtTest extends AbstractAtTest {
    @Test
    public void exertAsCost() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl gimli = new PhysicalCardImpl(100, "1_13", P1, _library.getLotroCardBlueprint("1_13"));
        PhysicalCardImpl inquisitor = new PhysicalCardImpl(100, "1_268", P2, _library.getLotroCardBlueprint("1_268"));

        _game.getGameState().addCardToZone(_game, gimli, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, inquisitor, Zone.SHADOW_CHARACTERS);

        _game.getGameState().addTokens(gimli, Token.WOUND, 2);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Play inquisitor
        playerDecided(P2, "0");
        playerDecided(P2, "");

        // Pass in maneuver
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in archery
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in assignment
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assign minion to Frodo
        playerDecided(P1, gimli.getCardId() + " " + inquisitor.getCardId());

        // Start skirmish
        playerDecided(P1, String.valueOf(gimli.getCardId()));

        assertEquals(0, ((String[]) _userFeedback.getAwaitingDecision(P1).getDecisionParameters().get("actionId")).length);
    }

    @Test
    public void reduceArcheryTotal() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl legolas = new PhysicalCardImpl(100, "40_52", P1, _library.getLotroCardBlueprint("40_52"));
        PhysicalCardImpl arrowsOfLight = new PhysicalCardImpl(100, "40_33", P1, _library.getLotroCardBlueprint("40_33"));
        PhysicalCardImpl inquisitor = new PhysicalCardImpl(100, "1_268", P2, _library.getLotroCardBlueprint("1_268"));

        _game.getGameState().addCardToZone(_game, inquisitor, Zone.SHADOW_CHARACTERS);

        _game.getGameState().addCardToZone(_game, legolas, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, arrowsOfLight, Zone.HAND);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

        // Pass in maneuver
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Play ArrowsOfLight
        playerDecided(P1, "0");
        playerDecided(P1, "0");
    }

    @Test
    public void playedTrigger() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl bruinenUnleashed = new PhysicalCardImpl(100, "40_37", P1, _library.getLotroCardBlueprint("40_37"));
        PhysicalCardImpl legolas = new PhysicalCardImpl(100, "40_52", P1, _library.getLotroCardBlueprint("40_52"));
        PhysicalCardImpl nazgul = new PhysicalCardImpl(100, "40_211", P2, _library.getLotroCardBlueprint("40_211"));

        _game.getGameState().addCardToZone(_game, legolas, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, bruinenUnleashed, Zone.SUPPORT);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.HAND);

        skipMulligans();

        _game.getGameState().setTwilight(10);

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "0");

        assertEquals(1, _game.getGameState().getWounds(nazgul));
    }

    @Test
    public void choiceEffect() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl getOutOfTheShire = createCard(P1, "40_320");
        PhysicalCardImpl merry = createCard(P1, "40_256");
        PhysicalCardImpl nazgul = createCard(P2, "40_211");

        _game.getGameState().addCardToZone(_game, getOutOfTheShire, Zone.HAND);
        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.SHADOW_CHARACTERS);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

        // Pass in maneuver
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in archery
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in assignment
        playerDecided(P1, "");
        playerDecided(P2, "");

        playerDecided(P1, merry.getCardId() + " " + nazgul.getCardId());

        playerDecided(P1, "" + merry.getCardId());

        // Play Get Out of the Shire
        playerDecided(P1, "0");

        // Choose to cancel skirmish
        playerDecided(P1, "1");

        // We're in Fierce skirmishes
        assertEquals(Phase.ASSIGNMENT, _game.getGameState().getCurrentPhase());
        assertEquals(0, _game.getGameState().getWounds(merry));
        assertEquals(Zone.FREE_CHARACTERS, merry.getZone());
    }

    @Test
    public void conditionalEffect() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl getOutOfTheShire = createCard(P1, "40_320");
        PhysicalCardImpl nazgul = createCard(P2, "40_211");

        _game.getGameState().addCardToZone(_game, getOutOfTheShire, Zone.HAND);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.SHADOW_CHARACTERS);

        final PhysicalCard frodo = _game.getGameState().getRingBearer(P1);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

        // Pass in maneuver
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in archery
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Pass in assignment
        playerDecided(P1, "");
        playerDecided(P2, "");

        playerDecided(P1, frodo.getCardId() + " " + nazgul.getCardId());

        playerDecided(P1, "" + frodo.getCardId());

        // Play Get Out of the Shire
        playerDecided(P1, "0");

        // No choice given
        assertNull(_userFeedback.getAwaitingDecision(P1));
    }

    @Test
    public void costToEffect() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl celeborn = createCard(P1, "40_38");
        PhysicalCardImpl celebornInDeck = createCard(P1, "40_38");
        PhysicalCardImpl nazgul = createCard(P2, "40_211");

        _game.getGameState().addCardToZone(_game, celeborn, Zone.SUPPORT);
        _game.getGameState().putCardOnTopOfDeck(celebornInDeck);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.SHADOW_CHARACTERS);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

        // Use Celeborn
        playerDecided(P1, "0");
        // Pass on reveal
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Choose to discard
        playerDecided(P1, "0");

        assertEquals(Zone.DISCARD, celebornInDeck.getZone());
        assertEquals(1, _game.getGameState().getWounds(nazgul));
    }

    @Test
    public void costToEffectPass() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        PhysicalCardImpl celeborn = createCard(P1, "40_38");
        PhysicalCardImpl celebornInDeck = createCard(P1, "40_38");
        PhysicalCardImpl nazgul = createCard(P2, "40_211");

        _game.getGameState().addCardToZone(_game, celeborn, Zone.SUPPORT);
        _game.getGameState().putCardOnTopOfDeck(celebornInDeck);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.SHADOW_CHARACTERS);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

        // Use Celeborn
        playerDecided(P1, "0");
        // Pass on reveal
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Choose not to discard
        playerDecided(P1, "1");

        assertEquals(Zone.DECK, celebornInDeck.getZone());
        assertEquals(0, _game.getGameState().getWounds(nazgul));
    }

    @Test
    public void checkingEventCostsAsRequirements() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl battleFever = createCard(P1, "40_5");
        final PhysicalCardImpl gimli = createCard(P1, "40_18");
        PhysicalCardImpl nazgul = createCard(P2, "40_211");


        _game.getGameState().addCardToZone(_game, gimli, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, battleFever, Zone.HAND);
        _game.getGameState().addCardToZone(_game, nazgul, Zone.SHADOW_CHARACTERS);

        skipMulligans();

        // Pass in fellowship
        playerDecided(P1, "");

        // Pass in shadow
        playerDecided(P2, "");

//Pass in maneuver
        playerDecided(P1, "");
        playerDecided(P2, "");

//Pass in archery
        playerDecided(P1, "");
        playerDecided(P2, "");

//Pass in assignment
        playerDecided(P1, "");
        playerDecided(P2, "");

        playerDecided(P1, gimli.getCardId() + " " + nazgul.getCardId());
        playerDecided(P1, "" + gimli.getCardId());

        assertNull(getCardActionId(P1, "Play Battle Fever"));
    }

    @Test
    public void discardCardEffect() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl blackBreath = createCard(P2, "40_183");
        final PhysicalCardImpl athelas = createCard(P1, "40_313");
        final PhysicalCardImpl athelasInHand = createCard(P1, "40_313");
        PhysicalCardImpl aragorn = createCard(P1, "40_94");

        _game.getGameState().addCardToZone(_game, aragorn, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, blackBreath, aragorn);
        _game.getGameState().addCardToZone(_game, athelas, Zone.HAND);
        _game.getGameState().addCardToZone(_game, athelasInHand, Zone.HAND);

        skipMulligans();

        // Play athelas
        playerDecided(P1, "0");
        // Attach to Aragorn
        playerDecided(P1, "" + aragorn.getCardId());

        playerDecided(P1, "0");

        playerDecided(P1, "0");

        assertEquals(Zone.DISCARD, blackBreath.getZone());
    }
}
