package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class AssignmentAtTest extends AbstractAtTest {
    @Test
    public void orcAssassinAssignNotToAlly() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl hobbitPartyGuest = new PhysicalCardImpl(101, "1_297", P1, _library.getLotroCardBlueprint("1_297"));
        PhysicalCardImpl orcAssassin = new PhysicalCardImpl(102, "1_262", P2, _library.getLotroCardBlueprint("1_262"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, hobbitPartyGuest, Zone.SUPPORT);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, orcAssassin, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assignment phase
        playerDecided(P1, "");

        AwaitingDecision assignmentActions = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, assignmentActions.getDecisionType());
        playerDecided(P2, "0");

        AwaitingDecision assignCharacter = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, assignCharacter.getDecisionType());
        validateContents(new String[]{String.valueOf(merry.getCardId()), String.valueOf(_game.getGameState().getRingBearer(P1).getCardId())}, (String[]) assignCharacter.getDecisionParameters().get("cardId"));
    }

    @Test
    public void sarumanAssignsToCompainon() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl pippin = new PhysicalCardImpl(101, "1_306", P1, _library.getLotroCardBlueprint("1_306"));
        PhysicalCardImpl saruman = new PhysicalCardImpl(102, "3_69", P2, _library.getLotroCardBlueprint("3_69"));
        PhysicalCardImpl urukHaiRaidingParty = new PhysicalCardImpl(103, "1_158", P2, _library.getLotroCardBlueprint("1_158"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, saruman, Zone.SHADOW_CHARACTERS);
        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assignment phase
        playerDecided(P1, "");

        AwaitingDecision assignmentActions = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, assignmentActions.getDecisionType());
        playerDecided(P2, "0");

        AwaitingDecision chooseMinion = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, chooseMinion.getDecisionType());
        playerDecided(P2, String.valueOf(urukHaiRaidingParty.getCardId()));

        AwaitingDecision chooseCompanion = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, chooseCompanion.getDecisionType());
        validateContents(new String[]{String.valueOf(merry.getCardId()), String.valueOf(pippin.getCardId())}, (String[]) chooseCompanion.getDecisionParameters().get("cardId"));
    }

    @Test
    public void normalAssignment() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl pippin = new PhysicalCardImpl(101, "1_306", P1, _library.getLotroCardBlueprint("1_306"));
        PhysicalCardImpl urukHaiRaidingParty1 = new PhysicalCardImpl(102, "1_158", P2, _library.getLotroCardBlueprint("1_158"));
        PhysicalCardImpl urukHaiRaidingParty2 = new PhysicalCardImpl(103, "1_158", P2, _library.getLotroCardBlueprint("1_158"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty1, Zone.SHADOW_CHARACTERS);
        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty2, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        final List<Assignment> assignmentsBeforeFreePlayer = _game.getGameState().getAssignments();
        assertEquals(0, assignmentsBeforeFreePlayer.size());

        AwaitingDecision assignmentDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ASSIGN_MINIONS, assignmentDecision.getDecisionType());
        validateContents(toCardIdArray(urukHaiRaidingParty1, urukHaiRaidingParty2), (String[]) assignmentDecision.getDecisionParameters().get("minions"));
        validateContents(toCardIdArray(merry, pippin, _game.getGameState().getRingBearer(P1)), (String[]) assignmentDecision.getDecisionParameters().get("freeCharacters"));
        playerDecided(P1, merry.getCardId() + " " + urukHaiRaidingParty1.getCardId());

        final List<Assignment> assignmentsAfterFreePlayer = _game.getGameState().getAssignments();
        assertEquals(1, assignmentsAfterFreePlayer.size());
        assertEquals(merry, assignmentsAfterFreePlayer.get(0).getFellowshipCharacter());
        assertEquals(1, assignmentsAfterFreePlayer.get(0).getShadowCharacters().size());
        assertTrue(assignmentsAfterFreePlayer.get(0).getShadowCharacters().contains(urukHaiRaidingParty1));

        AwaitingDecision shadowAssignmentDecision = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.ASSIGN_MINIONS, shadowAssignmentDecision.getDecisionType());
        validateContents(toCardIdArray(urukHaiRaidingParty2), (String[]) shadowAssignmentDecision.getDecisionParameters().get("minions"));
        validateContents(toCardIdArray(merry, pippin, _game.getGameState().getRingBearer(P1)), (String[]) shadowAssignmentDecision.getDecisionParameters().get("freeCharacters"));
        playerDecided(P2, pippin.getCardId() + " " + urukHaiRaidingParty2.getCardId());

        final List<Assignment> assignmentsAfterShadowPlayer = _game.getGameState().getAssignments();
        assertEquals(2, assignmentsAfterFreePlayer.size());
        assertEquals(merry, assignmentsAfterFreePlayer.get(0).getFellowshipCharacter());
        assertEquals(1, assignmentsAfterFreePlayer.get(0).getShadowCharacters().size());
        assertTrue(assignmentsAfterFreePlayer.get(0).getShadowCharacters().contains(urukHaiRaidingParty1));
        assertEquals(pippin, assignmentsAfterFreePlayer.get(1).getFellowshipCharacter());
        assertEquals(1, assignmentsAfterFreePlayer.get(1).getShadowCharacters().size());
        assertTrue(assignmentsAfterFreePlayer.get(1).getShadowCharacters().contains(urukHaiRaidingParty2));
    }

    @Test
    public void assignmentsWithDefender() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl pippin = new PhysicalCardImpl(101, "1_306", P1, _library.getLotroCardBlueprint("1_306"));
        PhysicalCardImpl urukHaiRaidingParty1 = new PhysicalCardImpl(102, "1_158", P2, _library.getLotroCardBlueprint("1_158"));
        PhysicalCardImpl urukHaiRaidingParty2 = new PhysicalCardImpl(103, "1_158", P2, _library.getLotroCardBlueprint("1_158"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty1, Zone.SHADOW_CHARACTERS);
        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty2, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Merry gets Defender +1
        _game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                new KeywordModifier(null, merry, Keyword.DEFENDER, 1), Phase.ASSIGNMENT);

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        final List<Assignment> assignmentsBeforeFreePlayer = _game.getGameState().getAssignments();
        assertEquals(0, assignmentsBeforeFreePlayer.size());

        AwaitingDecision assignmentDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ASSIGN_MINIONS, assignmentDecision.getDecisionType());
        validateContents(toCardIdArray(urukHaiRaidingParty1, urukHaiRaidingParty2), (String[]) assignmentDecision.getDecisionParameters().get("minions"));
        validateContents(toCardIdArray(merry, pippin, _game.getGameState().getRingBearer(P1)), (String[]) assignmentDecision.getDecisionParameters().get("freeCharacters"));
        try {
            playerDecided(P1, pippin.getCardId() + " " + urukHaiRaidingParty1.getCardId() + " " + urukHaiRaidingParty2.getCardId());
            fail("Pippin can't have multiple minions assigned by FP player");
        } catch (DecisionResultInvalidException exp) {
            // Expected
        }
        // Merry gets two minions (he has Defender +1)
        playerDecided(P1, merry.getCardId() + " " + urukHaiRaidingParty1.getCardId() + " " + urukHaiRaidingParty2.getCardId());

        final List<Assignment> assignmentsAfterFreePlayer = _game.getGameState().getAssignments();
        assertEquals(1, assignmentsAfterFreePlayer.size());
        assertEquals(merry, assignmentsAfterFreePlayer.get(0).getFellowshipCharacter());
        assertEquals(2, assignmentsAfterFreePlayer.get(0).getShadowCharacters().size());
        assertTrue(assignmentsAfterFreePlayer.get(0).getShadowCharacters().contains(urukHaiRaidingParty1));
        assertTrue(assignmentsAfterFreePlayer.get(0).getShadowCharacters().contains(urukHaiRaidingParty2));
    }
}
