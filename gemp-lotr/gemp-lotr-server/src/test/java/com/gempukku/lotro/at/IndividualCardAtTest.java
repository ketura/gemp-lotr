package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
}
