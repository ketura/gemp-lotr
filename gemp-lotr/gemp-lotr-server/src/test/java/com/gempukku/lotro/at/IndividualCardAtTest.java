package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class IndividualCardAtTest extends AbstractAtTest {
    @Test
    public void dwarvenAxeDoesNotFreeze() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl gimli = new PhysicalCardImpl(100, "1_13", P1, _library.getLotroCardBlueprint("1_13"));
        PhysicalCardImpl dwarvenAxe = new PhysicalCardImpl(101, "1_9", P1, _library.getLotroCardBlueprint("1_9"));
        PhysicalCardImpl goblinRunner = new PhysicalCardImpl(102, "1_178", P2, _library.getLotroCardBlueprint("1_178"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, gimli, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, dwarvenAxe, gimli);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, goblinRunner, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // End maneuver phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End archery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assign
        playerDecided(P1, gimli.getCardId() + " " + goblinRunner.getCardId());

        // Start skirmish
        playerDecided(P1, String.valueOf(gimli.getCardId()));

        // End skirmish phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        final AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ACTION_CHOICE, awaitingDecision.getDecisionType());
        validateContents(new String[]{"1_9", "rules"}, (String[]) awaitingDecision.getDecisionParameters().get("blueprintId"));

        playerDecided(P1, "0");

        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        assertEquals(Zone.DISCARD, goblinRunner.getZone());
    }

    @Test
    public void playDiscountAsfalothOnArwen() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        extraCards.put(P1, Arrays.asList("1_30", "1_31"));
        initializeSimplestGame(extraCards);

        // Play first character
        AwaitingDecision firstCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, firstCharacterDecision.getDecisionType());
        validateContents(new String[]{"1_30"}, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(firstCharacterDecision, "1_30"));

        skipMulligans();

        PhysicalCard asfaloth = _game.getGameState().getHand(P1).get(0);

        final AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, awaitingDecision.getDecisionType());
        validateContents(new String[]{"" + asfaloth.getCardId()}, (String[]) awaitingDecision.getDecisionParameters().get("cardId"));

        assertEquals(0, _game.getGameState().getTwilightPool());

        playerDecided(P1, "0");

        assertEquals(0, _game.getGameState().getTwilightPool());
        assertEquals(Zone.ATTACHED, asfaloth.getZone());
    }

    @Test
    public void playDiscountAsfalothOnOtherElf() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        extraCards.put(P1, Arrays.asList("1_51", "1_31"));
        initializeSimplestGame(extraCards);

        // Play first character
        AwaitingDecision firstCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, firstCharacterDecision.getDecisionType());
        validateContents(new String[]{"1_51"}, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(firstCharacterDecision, "1_51"));

        skipMulligans();

        PhysicalCard asfaloth = _game.getGameState().getHand(P1).get(0);

        final AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, awaitingDecision.getDecisionType());
        validateContents(new String[]{"" + asfaloth.getCardId()}, (String[]) awaitingDecision.getDecisionParameters().get("cardId"));

        assertEquals(0, _game.getGameState().getTwilightPool());

        playerDecided(P1, "0");

        assertEquals(2, _game.getGameState().getTwilightPool());
        assertEquals(Zone.ATTACHED, asfaloth.getZone());
    }

    @Test
    public void bilboRingBearerWithConsortingAndMorgulBrute() throws DecisionResultInvalidException {
        Map<String, LotroDeck> decks = new HashMap<String, LotroDeck>();
        final LotroDeck p1Deck = createSimplestDeck();
        p1Deck.setRingBearer("9_49");
        decks.put(P1, p1Deck);
        final LotroDeck p2Deck = createSimplestDeck();
        p2Deck.addCard("7_188");
        decks.put(P2, p2Deck);

        initializeGameWithDecks(decks);

        skipMulligans();

        PhysicalCard morgulBrute = _game.getGameState().getHand(P2).iterator().next();

        PhysicalCardImpl consortingWithWizards = new PhysicalCardImpl(100, "2_97", P1, _library.getLotroCardBlueprint("2_97"));
        PhysicalCardImpl ulaireEnquea = new PhysicalCardImpl(101, "1_231", P2, _library.getLotroCardBlueprint("1_231"));

        _game.getGameState().attachCard(_game, consortingWithWizards, _game.getGameState().getRingBearer(P1));

        _game.getGameState().addTwilight(3);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, ulaireEnquea, Zone.SHADOW_CHARACTERS);

        final AwaitingDecision shadowDecision = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, shadowDecision.getDecisionType());
        validateContents(new String[]{"" + morgulBrute.getCardId()}, ((String[]) shadowDecision.getDecisionParameters().get("cardId")));

        playerDecided(P2, "0");

        final AwaitingDecision optionalPlayDecision = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalPlayDecision.getDecisionType());
        validateContents(new String[]{"" + morgulBrute.getCardId()}, ((String[]) optionalPlayDecision.getDecisionParameters().get("cardId")));

        assertEquals(1, _game.getGameState().getBurdens());

        // User optional trigger of Morgul Brute
        playerDecided(P2, "0");

        // This should add burden without asking FP player for choice, since Bilbo can't be wounded, because of Consorting With Wizards
        assertEquals(2, _game.getGameState().getBurdens());
    }

    @Test
    public void mumakChieftainPlayingMumakForFree() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl mumakChieftain = new PhysicalCardImpl(100, "10_45", P2, _library.getLotroCardBlueprint("10_45"));
        PhysicalCardImpl mumak = new PhysicalCardImpl(100, "5_73", P2, _library.getLotroCardBlueprint("5_73"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, mumak, Zone.DISCARD);
        _game.getGameState().addCardToZone(_game, mumakChieftain, Zone.HAND);
        _game.getGameState().setTwilight(5);

        // End fellowship phase
        playerDecided(P1, "");

        assertEquals(7, _game.getGameState().getTwilightPool());

        // Play mumak chieftain
        final AwaitingDecision shadowDecision = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, shadowDecision.getDecisionType());
        validateContents(new String[]{"" + mumakChieftain.getCardId()}, ((String[]) shadowDecision.getDecisionParameters().get("cardId")));
        playerDecided(P2, "0");

        assertEquals(0, _game.getGameState().getTwilightPool());

        final AwaitingDecision optionalPlayDecision = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalPlayDecision.getDecisionType());
        validateContents(new String[]{"" + mumakChieftain.getCardId()}, ((String[]) optionalPlayDecision.getDecisionParameters().get("cardId")));
        playerDecided(P2, "0");

        assertEquals(Zone.ATTACHED, mumak.getZone());
    }
}
