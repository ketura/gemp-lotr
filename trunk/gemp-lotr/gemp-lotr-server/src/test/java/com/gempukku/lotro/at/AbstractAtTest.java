package com.gempukku.lotro.at;

import com.gempukku.lotro.game.DefaultUserFeedback;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.formats.MovieFormat;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.DefaultLotroGame;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.junit.BeforeClass;

import java.util.*;

import static org.junit.Assert.fail;

public abstract class AbstractAtTest {
    protected static LotroCardBlueprintLibrary _library;

    protected DefaultLotroGame _game;
    protected DefaultUserFeedback _userFeedback;
    protected static final String P1 = "player1";
    protected static final String P2 = "player2";

    @BeforeClass
    public static void initializeCardLibrary() {
        _library = new LotroCardBlueprintLibrary();
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 365; j++) {
//                String blueprintId = i + "_" + j;
//                try {
//                    _library.getLotroCardBlueprint(blueprintId);
//                } catch (IllegalArgumentException exp) {
//
//                }
//            }
//        }
    }

    protected void initializeSimplestGame() throws DecisionResultInvalidException {
        this.initializeSimplestGame(null);
    }

    protected void initializeSimplestGame(Map<String, Collection<String>> additionalCardsInDeck) throws DecisionResultInvalidException {
        Map<String, LotroDeck> decks = new HashMap<String, LotroDeck>();
        addPlayerDeck(P1, decks, additionalCardsInDeck);
        addPlayerDeck(P2, decks, additionalCardsInDeck);

        initializeGameWithDecks(decks);
    }

    protected void initializeGameWithDecks(Map<String, LotroDeck> decks) throws DecisionResultInvalidException {
        _userFeedback = new DefaultUserFeedback();

        MovieFormat format = new MovieFormat(_library);

        _game = new DefaultLotroGame(format, decks, _userFeedback, _library);
        _userFeedback.setGame(_game);
        _game.startGame();

        // Bidding
        playerDecided(P1, "1");
        playerDecided(P2, "0");

        // Seating choice
        playerDecided(P1, "0");
    }

    protected void skipMulligans() throws DecisionResultInvalidException {
        // Mulligans
        playerDecided(P1, "0");
        playerDecided(P2, "0");
    }

    protected void validateContents(String[] array1, String[] array2) {
        if (array1.length != array2.length)
            fail("Array sizes differ");
        List<String> values = new ArrayList<String>(Arrays.asList(array1));
        for (String s : array2) {
            if (!values.remove(s))
                fail("Arrays contents differ");
        }
    }

    protected String[] toCardIdArray(PhysicalCard... cards) {
        String[] result = new String[cards.length];
        for (int i = 0; i < cards.length; i++)
            result[i] = String.valueOf(cards[i].getCardId());
        return result;
    }

    protected String getArbitraryCardId(AwaitingDecision awaitingDecision, String blueprintId) {
        String[] blueprints = (String[]) awaitingDecision.getDecisionParameters().get("blueprintId");
        for (int i = 0; i < blueprints.length; i++)
            if (blueprints[i].equals(blueprintId))
                return ((String[]) awaitingDecision.getDecisionParameters().get("cardId"))[i];
        return null;
    }

    protected String getCardActionId(AwaitingDecision awaitingDecision, String actionTextStart) {
        String[] actionTexts = (String[]) awaitingDecision.getDecisionParameters().get("actionText");
        for (int i = 0; i < actionTexts.length; i++)
            if (actionTexts[i].startsWith(actionTextStart))
                return ((String[]) awaitingDecision.getDecisionParameters().get("actionId"))[i];
        return null;
    }

    protected String getMultipleDecisionIndex(AwaitingDecision awaitingDecision, String result) {
        String[] actionTexts = (String[]) awaitingDecision.getDecisionParameters().get("results");
        for (int i = 0; i < actionTexts.length; i++)
            if (actionTexts[i].equals(result))
                return String.valueOf(i);
        return null;
    }

    private void addPlayerDeck(String player, Map<String, LotroDeck> decks, Map<String, Collection<String>> additionalCardsInDeck) {
        LotroDeck deck = createSimplestDeck();
        if (additionalCardsInDeck != null) {
            Collection<String> extraCards = additionalCardsInDeck.get(player);
            if (extraCards != null)
                for (String extraCard : extraCards)
                    deck.addCard(extraCard);
        }
        decks.put(player, deck);
    }

    protected void playerDecided(String player, String answer) throws DecisionResultInvalidException {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(player);
        _userFeedback.participantDecided(player);
        try {
            decision.decisionMade(answer);
        } catch (DecisionResultInvalidException exp) {
            _userFeedback.sendAwaitingDecision(player, decision);
            throw exp;
        }
        _game.carryOutPendingActionsUntilDecisionNeeded();
    }

    protected LotroDeck createSimplestDeck() {
        LotroDeck lotroDeck = new LotroDeck();
        // 10_121,1_2
        lotroDeck.setRingBearer("10_121");
        lotroDeck.setRing("1_2");
        // 7_330,7_336,8_117,7_342,7_345,7_350,8_120,10_120,7_360
        lotroDeck.addSite("7_330");
        lotroDeck.addSite("7_335");
        lotroDeck.addSite("8_117");
        lotroDeck.addSite("7_342");
        lotroDeck.addSite("7_345");
        lotroDeck.addSite("7_350");
        lotroDeck.addSite("8_120");
        lotroDeck.addSite("10_120");
        lotroDeck.addSite("7_360");
        return lotroDeck;
    }
}
