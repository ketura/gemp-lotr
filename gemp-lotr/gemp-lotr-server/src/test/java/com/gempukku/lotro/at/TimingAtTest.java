package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class TimingAtTest extends AbstractAtTest {
    @Test
    public void playStartingFellowshipWithDiscount() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        extraCards.put(P1, Arrays.asList("7_88", "6_121"));
        initializeSimplestGame(extraCards);

        // Play first character
        AwaitingDecision firstCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, firstCharacterDecision.getDecisionType());
        validateContents(new String[]{"7_88", "6_121"}, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(firstCharacterDecision, "7_88"));

        // Play second character with discount
        AwaitingDecision secondCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, secondCharacterDecision.getDecisionType());
        validateContents(new String[]{"6_121"}, ((String[]) secondCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(secondCharacterDecision, "6_121"));
    }

    @Test
    public void playStartingFellowshipWithSpotRequirement() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        extraCards.put(P1, Arrays.asList("1_50", "1_48"));
        initializeSimplestGame(extraCards);

        // Play first character
        AwaitingDecision firstCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, firstCharacterDecision.getDecisionType());
        validateContents(new String[]{"1_50"}, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(firstCharacterDecision, "1_50"));

        // Play second character with spot requirement
        AwaitingDecision secondCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, secondCharacterDecision.getDecisionType());
        validateContents(new String[]{"1_48"}, ((String[]) secondCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(secondCharacterDecision, "1_48"));
    }

    @Test
    public void playMultipleRequiredEffectsInOrder() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl elrond = new PhysicalCardImpl(100, "1_40", P1, _library.getLotroCardBlueprint("1_40"));
        PhysicalCardImpl gimli = new PhysicalCardImpl(101, "1_13", P1, _library.getLotroCardBlueprint("1_13"));
        PhysicalCardImpl dwarvenHeart = new PhysicalCardImpl(102, "1_10", P1, _library.getLotroCardBlueprint("1_10"));

        _game.getGameState().addCardToZone(_game, gimli, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, elrond, Zone.SUPPORT);
        _game.getGameState().attachCard(_game, dwarvenHeart, gimli);

        skipMulligans();

        AwaitingDecision requiredActionChoice = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ACTION_CHOICE, requiredActionChoice.getDecisionType());
        validateContents(new String[]{"1_40", "1_10"}, (String[]) requiredActionChoice.getDecisionParameters().get("blueprintId"));
        playerDecided(P1, "0");

        assertTrue(_game.getGameState().getCurrentPhase() != Phase.BETWEEN_TURNS);
    }

    @Test
    public void playMultipleOptionalEffectsInOrder() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl aragorn = new PhysicalCardImpl(100, "1_365", P1, _library.getLotroCardBlueprint("1_365"));
        PhysicalCardImpl gandalf = new PhysicalCardImpl(101, "2_122", P1, _library.getLotroCardBlueprint("2_122"));

        _game.getGameState().addCardToZone(_game, aragorn, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, gandalf, Zone.FREE_CHARACTERS);

        skipMulligans();

        AwaitingDecision optionalActionChoice = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalActionChoice.getDecisionType());
        System.out.println(optionalActionChoice);
    }

    @Test
    public void playEffectFromDiscard() throws DecisionResultInvalidException {

    }

    @Test
    public void playEffectFromStacked() throws DecisionResultInvalidException {

    }

    @Test
    public void stackedCardAffectsGame() throws DecisionResultInvalidException {

    }

    @Test
    public void charactersDontDieIfPrintedVitalityEqualToWoundsButCurrentVitalityMoreThanZero() throws DecisionResultInvalidException {

    }

    @Test
    public void charactersDieIfCurrentVitalityIsZero() throws DecisionResultInvalidException {

    }
}
