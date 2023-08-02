package com.gempukku.lotro.cards;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.filter.FilterFactory;
import com.gempukku.lotro.cards.lotronly.LotroDeck;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.processes.lotronly.assign.Assignment;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.lotronly.skirmish.Skirmish;
import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.actions.ActionProxy;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.modifiers.Modifier;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.game.PlayConditions;
import com.gempukku.lotro.rules.RuleUtils;
import org.junit.Assert;

import java.util.*;

public class GenericCardTestHelper extends AbstractAtTest {

    private final int LastCardID = 100;

    public static final HashMap<String, String> FellowshipSites = new HashMap<>() {{
        put("site1", "1_319");
        put("site2", "1_327");
        put("site3", "1_337");
        put("site4", "1_343");
        put("site5", "1_349");
        put("site6", "1_350");
        put("site7", "1_353");
        put("site8", "1_356");
        put("site9", "1_360");
    }};

    public static final HashMap<String, String> KingSites = new HashMap<>() {{
        put("site1", "7_330");
        put("site2", "7_335");
        put("site3", "8_117");
        put("site4", "7_342");
        put("site5", "7_345");
        put("site6", "7_350");
        put("site7", "8_120");
        put("site8", "10_120");
        put("site9", "7_360");
    }};

    public static final String FOTRFrodo = "1_290";
    public static final String GimliRB = "9_4";
    public static final String GaladrielRB = "9_14";

    public static final String RulingRing = "1_2";
    public static final String IsildursBaneRing = "1_1";
    public static final String ATARRing = "4_1";
    public static final String GreatRing = "19_1";

    private final FilterFactory FilterFactory = new FilterFactory();
    private final CardGenerationEnvironment Environment = new LotroCardBlueprintBuilder();
    private final DefaultActionContext FreepsFilterContext = new DefaultActionContext<DefaultGame>(P1, _game, null, null, null);
    private final DefaultActionContext ShadowFilterContext = new DefaultActionContext<DefaultGame>(P2, _game, null, null, null);


    // Player key, then name/card
    public Map<String, Map<String, LotroPhysicalCardImpl>> Cards = new HashMap<>();

    public GenericCardTestHelper(HashMap<String, String> cardIDs) throws CardNotFoundException, DecisionResultInvalidException {
        this(cardIDs, null, null, null, "multipath");
    }

    public GenericCardTestHelper(HashMap<String, String> cardIDs, HashMap<String, String> siteIDs, String ringBearerID, String ringID) throws CardNotFoundException, DecisionResultInvalidException {
        this(cardIDs, siteIDs, ringBearerID, ringID, "multipath");
    }

    public GenericCardTestHelper(HashMap<String, String> cardIDs, HashMap<String, String> siteIDs, String ringBearerID, String ringID, String path) throws CardNotFoundException, DecisionResultInvalidException {
        super();

        if(siteIDs == null || ringBearerID == null || ringID == null) {
            initializeSimplestGame();
        }
        else {
            Map<String, LotroDeck> decks = new HashMap<>();
            decks.put(P1, new LotroDeck(P1));
            decks.put(P2, new LotroDeck(P2));

            for(String name : siteIDs.keySet()) {
                String id = siteIDs.get(name);
                decks.get(P1).addSite(id);
                decks.get(P2).addSite(id);
            }

            decks.get(P1).setRingBearer(ringBearerID);
            decks.get(P2).setRingBearer(ringBearerID);

            decks.get(P1).setRing(ringID);
            decks.get(P2).setRing(ringID);

            initializeGameWithDecks(decks, path);
        }

        Cards.put(P1, new HashMap<>());
        Cards.put(P2, new HashMap<>());

        if(cardIDs != null) {
            for(String name : cardIDs.keySet()) {
                String id = cardIDs.get(name);
                LotroPhysicalCardImpl card = createCard(P1, id);
                Cards.get(P1).put(name, card);
                FreepsMoveCardsToBottomOfDeck(card);

                card = createCard(P2, id);
                Cards.get(P2).put(name, card);
                ShadowMoveCardsToBottomOfDeck(card);
            }
        }

        if(siteIDs != null) {
            for (var card : _game.getGameState().getAdventureDeck(P1)) {
                String name = siteIDs.entrySet()
                        .stream()
                        .filter(x -> x.getValue().equals(card.getBlueprintId()))
                        .map(Map.Entry::getKey)
                        .findFirst().get();

                Cards.get(P1).put(name, (LotroPhysicalCardImpl) card);
            }

            for (var card : _game.getGameState().getAdventureDeck(P2)) {
                String name = siteIDs.entrySet()
                        .stream()
                        .filter(x -> x.getValue().equals(card.getBlueprintId()))
                        .map(Map.Entry::getKey)
                        .findFirst().get();

                Cards.get(P2).put(name, (LotroPhysicalCardImpl) card);
            }
        }
    }


    public void StartGame() throws DecisionResultInvalidException {
        skipMulligans();
    }

    public void SkipStartingFellowships() throws DecisionResultInvalidException {
        if(FreepsDecisionAvailable("Starting fellowship")) {
            FreepsChoose("");
        }
        if(ShadowDecisionAvailable("Starting fellowship")) {
            ShadowChoose("");
        }
    }

    public LotroPhysicalCardImpl GetFreepsCard(String cardName) { return Cards.get(P1).get(cardName); }
    public LotroPhysicalCardImpl GetShadowCard(String cardName) { return Cards.get(P2).get(cardName); }
    public LotroPhysicalCardImpl GetCard(String player, String cardName) { return Cards.get(player).get(cardName); }
    public LotroPhysicalCardImpl GetFreepsCardByID(String id) { return GetCardByID(P1, Integer.parseInt(id)); }
    public LotroPhysicalCardImpl GetFreepsCardByID(int id) { return GetCardByID(P1, id); }
    public LotroPhysicalCardImpl GetShadowCardByID(String id) { return GetCardByID(P2, Integer.parseInt(id)); }
    public LotroPhysicalCardImpl GetShadowCardByID(int id) { return GetCardByID(P2, id); }
    public LotroPhysicalCardImpl GetCardByID(String player, int id) {
        return Cards.get(player).values().stream()
                .filter(x -> x.getCardId() == id)
                .findFirst().orElse(null);
    }

    public LotroPhysicalCardImpl GetFreepsSite(int siteNum) { return GetSite(P1, siteNum); }
    public LotroPhysicalCardImpl GetShadowSite(int siteNum) { return GetSite(P2, siteNum); }
    public LotroPhysicalCardImpl GetSite(String playerID, int siteNum)
    {
        LotroPhysicalCardImpl site = (LotroPhysicalCardImpl)_game.getGameState().getSite(siteNum);
        if(site != null && site.getOwner() == playerID)
            return site;

        List<LotroPhysicalCardImpl> advDeck = (List<LotroPhysicalCardImpl>)_game.getGameState().getAdventureDeck(playerID);
        return advDeck.stream().filter(x -> x.getBlueprint().getSiteNumber() == siteNum).findFirst().get();
    }

    public LotroPhysicalCardImpl GetSite(int siteNum)
    {
        return (LotroPhysicalCardImpl) _game.getGameState().getSite(siteNum);
    }
    public LotroPhysicalCardImpl GetFreepsSite(String name) { return GetSiteByName(P1, name); }
    public LotroPhysicalCardImpl GetShadowSite(String name) { return GetSiteByName(P2, name); }
    public LotroPhysicalCardImpl GetSiteByName(String player, String name)
    {
        var attempt = GetCard(player, name);
        if(attempt != null)
            return attempt;

        final String lowername = name.toLowerCase();
        List<LotroPhysicalCardImpl> advDeck = (List<LotroPhysicalCardImpl>)_game.getGameState().getAdventureDeck(player);
        return advDeck.stream().filter(x -> x.getBlueprint().getTitle().toLowerCase().contains(lowername)).findFirst().get();
    }

    public List<String> FreepsGetAvailableActions() { return GetAvailableActions(P1); }
    public List<String> ShadowGetAvailableActions() { return GetAvailableActions(P2); }
    public List<String> GetAvailableActions(String playerID) {
        AwaitingDecision decision = GetAwaitingDecision(playerID);
        if(decision == null) {
            return new ArrayList<>();
        }
        return Arrays.asList((String[])decision.getDecisionParameters().get("actionText"));
    }

    public AwaitingDecision FreepsGetAwaitingDecision() { return GetAwaitingDecision(P1); }
    public AwaitingDecision ShadowGetAwaitingDecision() { return GetAwaitingDecision(P2); }
    public AwaitingDecision GetAwaitingDecision(String playerID) { return _userFeedback.getAwaitingDecision(playerID); }

    public Boolean FreepsDecisionAvailable(String text) { return DecisionAvailable(P1, text); }
    public Boolean ShadowDecisionAvailable(String text) { return DecisionAvailable(P2, text); }
    public Boolean DecisionAvailable(String playerID, String text)
    {
        AwaitingDecision ad = GetAwaitingDecision(playerID);
        if(ad == null)
            return false;
        String lowerText = text.toLowerCase();
        return ad.getText().toLowerCase().contains(lowerText);
    }

    public Boolean FreepsActionAvailable(String action) { return ActionAvailable(P1, action); }
    public Boolean FreepsActionAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P1, "Use " + GameUtils.getFullName(card)); }
    public Boolean FreepsPlayAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P1, "Play " + GameUtils.getFullName(card)); }
    public Boolean FreepsTransferAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P1, "Transfer " + GameUtils.getFullName(card)); }
    public Boolean ShadowActionAvailable(String action) { return ActionAvailable(P2, action); }
    public Boolean ShadowActionAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P2, "Use " + GameUtils.getFullName(card)); }
    public Boolean ShadowPlayAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P2, "Play " + GameUtils.getFullName(card)); }
    public Boolean ShadowTransferAvailable(LotroPhysicalCardImpl card) { return ActionAvailable(P2, "Transfer " + GameUtils.getFullName(card)); }
    public Boolean ActionAvailable(String player, String action) {
        List<String> actions = GetAvailableActions(player);
        if(actions == null)
            return false;
        String lowerAction = action.toLowerCase();
        return actions.stream().anyMatch(x -> x.toLowerCase().contains(lowerAction));
    }

    public Boolean FreepsChoiceAvailable(String choice) { return ChoiceAvailable(P1, choice); }
    public Boolean ShadowChoiceAvailable(String choice) { return ChoiceAvailable(P2, choice); }
    public Boolean ChoiceAvailable(String player, String choice) {
        List<String> actions = GetADParamAsList(player, "results");
        if(actions == null)
            return false;
        String lowerChoice = choice.toLowerCase();
        return actions.stream().anyMatch(x -> x.toLowerCase().contains(lowerChoice));
    }

    public Boolean FreepsAnyActionsAvailable() { return AnyActionsAvailable(P1); }
    public Boolean ShadowAnyActionsAvailable() { return AnyActionsAvailable(P2); }
    public Boolean AnyActionsAvailable(String player) {
        List<String> actions = GetAvailableActions(player);
        return actions.size() > 0;
    }

    public Boolean FreepsAnyDecisionsAvailable() { return AnyDecisionsAvailable(P1); }
    public Boolean ShadowAnyDecisionsAvailable() { return AnyDecisionsAvailable(P2); }
    public Boolean AnyDecisionsAvailable(String player) {
        AwaitingDecision ad = GetAwaitingDecision(player);
        return ad != null;
    }

    public List<String> FreepsGetCardChoices() { return GetADParamAsList(P1, "cardId"); }
    public List<String> ShadowGetCardChoices() { return GetADParamAsList(P2, "cardId"); }
    public List<String> FreepsGetBPChoices() { return GetADParamAsList(P1, "blueprintId"); }
    public List<String> ShadowGetBPChoices() { return GetADParamAsList(P2, "blueprintId"); }
    public List<String> FreepsGetMultipleChoices() { return GetADParamAsList(P1, "results"); }
    public List<String> ShadowGetMultipleChoices() { return GetADParamAsList(P2, "results"); }
    public List<String> FreepsGetADParamAsList(String paramName) { return GetADParamAsList(P1, paramName); }
    public List<String> ShadowGetADParamAsList(String paramName) { return GetADParamAsList(P2, paramName); }
    public List<String> GetADParamAsList(String playerID, String paramName) { return Arrays.asList(GetAwaitingDecisionParam(playerID, paramName)); }
    public String[] FreepsGetADParam(String paramName) { return GetAwaitingDecisionParam(P1, paramName); }
    public String[] ShadowGetADParam(String paramName) { return GetAwaitingDecisionParam(P2, paramName); }
    public String FreepsGetFirstADParam(String paramName) { return GetAwaitingDecisionParam(P1, paramName)[0]; }
    public String ShadowGetFirstADParam(String paramName) { return GetAwaitingDecisionParam(P2, paramName)[0]; }
    public String[] GetAwaitingDecisionParam(String playerID, String paramName) {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(playerID);
        return decision.getDecisionParameters().get(paramName);
    }

    public Map<String, String[]> GetAwaitingDecisionParams(String playerID) {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(playerID);
        return decision.getDecisionParameters();
    }

    //public boolean HasItemIn

    public void FreepsUseCardAction(String name) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, name)); }
    public void FreepsUseCardAction(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, "Use " + GameUtils.getFullName(card))); }
    public void FreepsTransferCard(String name) throws DecisionResultInvalidException { FreepsTransferCard(GetFreepsCard(name)); }
    public void FreepsTransferCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, "Transfer " + GameUtils.getFullName(card))); }
    public void ShadowUseCardAction(String name) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, name)); }
    public void ShadowUseCardAction(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, "Use " + GameUtils.getFullName(card))); }
    public void ShadowTransferCard(String name) throws DecisionResultInvalidException { ShadowTransferCard(GetShadowCard(name)); }
    public void ShadowTransferCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, "Transfer " + GameUtils.getFullName(card))); }

    public void FreepsPlayCard(String name) throws DecisionResultInvalidException { FreepsPlayCard(GetFreepsCard(name)); }
    public void FreepsPlayCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, "Play " + GameUtils.getFullName(card))); }
    public void ShadowPlayCard(String name) throws DecisionResultInvalidException { ShadowPlayCard(GetShadowCard(name)); }
    public void ShadowPlayCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, "Play " + GameUtils.getFullName(card))); }

    public int FreepsGetWoundsOn(String cardName) { return GetWoundsOn(GetFreepsCard(cardName)); }
    public int ShadowGetWoundsOn(String cardName) { return GetWoundsOn(GetShadowCard(cardName)); }
    public int GetWoundsOn(LotroPhysicalCardImpl card) { return _game.getGameState().getWounds(card); }

    public int FreepsGetCultureTokensOn(String cardName) { return GetCultureTokensOn(GetFreepsCard(cardName)); }
    public int ShadowGetCultureTokensOn(String cardName) { return GetCultureTokensOn(GetShadowCard(cardName)); }
    public int GetCultureTokensOn(LotroPhysicalCardImpl card) { return _game.getGameState().getTokenCount(card, Token.findTokenForCulture(card.getBlueprint().getCulture())); }

    public int GetBurdens() { return _game.getGameState().getBurdens(); }

    public boolean FreepsHasInitiative() { return PlayConditions.hasInitiative(_game, Side.FREE_PEOPLE); }

    public boolean ShadowHasInitiative() { return PlayConditions.hasInitiative(_game, Side.SHADOW); }

    public int GetFreepsArcheryTotal() { return RuleUtils.calculateFellowshipArcheryTotal(_game); }
    public int GetShadowArcheryTotal() { return RuleUtils.calculateShadowArcheryTotal(_game); }

    public int GetFreepsHandCount() { return GetFreepsHand().size(); }
    public int GetShadowHandCount() { return GetShadowHand().size(); }

    public List<? extends LotroPhysicalCard> GetFreepsHand() { return GetPlayerHand(P1); }
    public List<? extends LotroPhysicalCard> GetShadowHand() { return GetPlayerHand(P2); }
    public List<? extends LotroPhysicalCard> GetPlayerHand(String player)
    {
        return _game.getGameState().getHand(player);
    }

    public int GetFreepsDeckCount() { return GetPlayerDeckCount(P1); }
    public int GetShadowDeckCount() { return GetPlayerDeckCount(P2); }
    public int GetPlayerDeckCount(String player)
    {
        return _game.getGameState().getDeck(player).size();
    }

    public LotroPhysicalCardImpl GetFreepsBottomOfDeck() { return GetPlayerBottomOfDeck(P1); }
    public LotroPhysicalCardImpl GetShadowBottomOfDeck() { return GetPlayerBottomOfDeck(P2); }
    public LotroPhysicalCardImpl GetFromBottomOfFreepsDeck(int index) { return GetFromBottomOfPlayerDeck(P1, index); }
    public LotroPhysicalCardImpl GetFromBottomOfShadowDeck(int index) { return GetFromBottomOfPlayerDeck(P2, index); }
    public LotroPhysicalCardImpl GetPlayerBottomOfDeck(String player) { return GetFromBottomOfPlayerDeck(player, 1); }
    public LotroPhysicalCardImpl GetFromBottomOfPlayerDeck(String player, int index)
    {
        var deck = _game.getGameState().getDeck(player);
        return (LotroPhysicalCardImpl) deck.get(deck.size() - index);
    }

    public LotroPhysicalCardImpl GetFreepsTopOfDeck() { return GetPlayerTopOfDeck(P1); }
    public LotroPhysicalCardImpl GetShadowTopOfDeck() { return GetPlayerTopOfDeck(P2); }
    public LotroPhysicalCardImpl GetFromTopOfFreepsDeck(int index) { return GetFromTopOfPlayerDeck(P1, index); }
    public LotroPhysicalCardImpl GetFromTopOfShadowDeck(int index) { return GetFromTopOfPlayerDeck(P2, index); }
    public LotroPhysicalCardImpl GetPlayerTopOfDeck(String player) { return GetFromTopOfPlayerDeck(player, 1); }

    /**
     * Index is 1-based (1 is first, 2 is second, etc)
     */
    public LotroPhysicalCardImpl GetFromTopOfPlayerDeck(String player, int index)
    {
        var deck = _game.getGameState().getDeck(player);
        return (LotroPhysicalCardImpl) deck.get(index - 1);
    }
    public int GetFreepsDiscardCount() { return GetPlayerDiscardCount(P1); }
    public int GetShadowDiscardCount() { return GetPlayerDiscardCount(P2); }
    public int GetPlayerDiscardCount(String player) { return _game.getGameState().getDiscard(player).size(); }

    public int GetFreepsDeadCount() { return _game.getGameState().getDeadPile(P1).size(); }

    public Phase GetCurrentPhase() { return _game.getGameState().getCurrentPhase(); }



    public void FreepsMoveCardToHand(String...names) {
        for(String name : names) {
            FreepsMoveCardToHand(GetFreepsCard(name));
        }
    }
    public void FreepsMoveCardToHand(LotroPhysicalCardImpl...cards) {
        for(LotroPhysicalCardImpl card : cards) {
            RemoveCardZone(P1, card);
            MoveCardToZone(P1, card, Zone.HAND);
        }
    }
    public void ShadowMoveCardToHand(String...names) {
        for(String name : names) {
            ShadowMoveCardToHand(GetShadowCard(name));
        }
    }
    public void ShadowMoveCardToHand(LotroPhysicalCardImpl...cards) {
        for(LotroPhysicalCardImpl card : cards) {
            RemoveCardZone(P2, card);
            MoveCardToZone(P2, card, Zone.HAND);
        }
    }

    public void FreepsAttachCardsTo(LotroPhysicalCardImpl bearer, LotroPhysicalCardImpl...cards) { AttachCardsTo(bearer, cards); }
    public void FreepsAttachCardsTo(LotroPhysicalCardImpl bearer, String...names) {
        Arrays.stream(names).forEach(name -> AttachCardsTo(bearer, GetFreepsCard(name)));
    }
    public void ShadowAttachCardsTo(LotroPhysicalCardImpl bearer, LotroPhysicalCardImpl...cards) { AttachCardsTo(bearer, cards); }
    public void ShadowAttachCardsTo(LotroPhysicalCardImpl bearer, String...names) {
        Arrays.stream(names).forEach(name -> AttachCardsTo(bearer, GetShadowCard(name)));
    }
    public void AttachCardsTo(LotroPhysicalCardImpl bearer, LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().attachCard(_game, card, bearer);
        });
    }

    public void FreepsStackCardsOn(LotroPhysicalCardImpl on, String...cardNames) {
        Arrays.stream(cardNames).forEach(name -> StackCardsOn(on, GetFreepsCard(name)));
    }
    public void ShadowStackCardsOn(LotroPhysicalCardImpl on, String...cardNames) {
        Arrays.stream(cardNames).forEach(name -> StackCardsOn(on, GetShadowCard(name)));
    }
    public void StackCardsOn(LotroPhysicalCardImpl on, LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().stackCard(_game, card, on);
        });
    }

    public void FreepsMoveCardsToTopOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsMoveCardsToTopOfDeck(GetFreepsCard(cardName)));
    }
    public void FreepsMoveCardsToTopOfDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }
    public void ShadowMoveCardsToTopOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowMoveCardsToTopOfDeck(GetShadowCard(cardName)));
    }
    public void ShadowMoveCardsToTopOfDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }

    public void FreepsMoveCardsToBottomOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsMoveCardsToBottomOfDeck(GetFreepsCard(cardName)));
    }
    public void FreepsMoveCardsToBottomOfDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnBottomOfDeck(card);
        });
    }
    public void ShadowMoveCardsToBottomOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowMoveCardsToBottomOfDeck(GetShadowCard(cardName)));
    }
    public void ShadowMoveCardsToBottomOfDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnBottomOfDeck(card);
        });
    }

    public void FreepsDrawCard() { FreepsDrawCards(1); }
    public void FreepsDrawCards() { FreepsDrawCards(1); }

    public void FreepsDrawCards(int count) {
        for(int i = 0; i < count; i++) {
            _game.getGameState().playerDrawsCard(P1);
        }
    }

    public void ShadowDrawCard() { ShadowDrawCards(1); }
    public void ShadowDrawCards() { ShadowDrawCards(1); }

    public void ShadowDrawCards(int count) {
        for(int i = 0; i < count; i++) {
            _game.getGameState().playerDrawsCard(P2);
        }
    }

    public void FreepsShuffleCardsInDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsShuffleCardsInDeck(GetFreepsCard(cardName)));
    }
    public void FreepsShuffleCardsInDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });

        ShuffleFreepsDeck();
    }
    public void ShadowShuffleCardsInDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowShuffleCardsInDeck(GetShadowCard(cardName)));
    }
    public void ShadowShuffleCardsInDeck(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });

        ShuffleShadowDeck();
    }

    public void ShuffleFreepsDeck() { ShuffleDeck(P1); }
    public void ShuffleShadowDeck() { ShuffleDeck(P2); }
    public void ShuffleDeck(String player) {
        _game.getGameState().shuffleDeck(player);
    }

    public void FreepsMoveCharToTable(String...names) {
        Arrays.stream(names).forEach(name -> FreepsMoveCharToTable(GetFreepsCard(name)));
    }
    public void FreepsMoveCharToTable(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.FREE_CHARACTERS));
    }
    public void ShadowMoveCharToTable(String...names) {
        Arrays.stream(names).forEach(name -> ShadowMoveCharToTable(GetShadowCard(name)));
    }
    public void ShadowMoveCharToTable(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P2, card, Zone.SHADOW_CHARACTERS));
    }


    public void FreepsMoveCardToSupportArea(String...names) {
        Arrays.stream(names).forEach(name -> FreepsMoveCardToSupportArea(GetFreepsCard(name)));
    }
    public void FreepsMoveCardToSupportArea(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.SUPPORT));
    }
    public void ShadowMoveCardToSupportArea(String...names) {
        Arrays.stream(names).forEach(name -> ShadowMoveCardToSupportArea(GetShadowCard(name)));
    }
    public void ShadowMoveCardToSupportArea(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P2, card, Zone.SUPPORT));
    }
    public void FreepsMoveCardToDiscard(String...names) {
        Arrays.stream(names).forEach(name -> FreepsMoveCardToDiscard(GetFreepsCard(name)));
    }
    public void FreepsMoveCardToDiscard(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.DISCARD));
    }
    public void ShadowMoveCardToDiscard(String...names) {
        Arrays.stream(names).forEach(name -> ShadowMoveCardToDiscard(GetShadowCard(name)));
    }
    public void ShadowMoveCardToDiscard(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P2, card, Zone.DISCARD));
    }


    public void FreepsMoveCardToDeadPile(String...names) {
        Arrays.stream(names).forEach(name -> FreepsMoveCardToDeadPile(GetFreepsCard(name)));
    }
    public void FreepsMoveCardToDeadPile(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.DEAD));
    }
    public void ShadowMoveCardToDeadPile(String...names) {
        Arrays.stream(names).forEach(name -> ShadowMoveCardToDeadPile(GetFreepsCard(name)));
    }
    public void ShadowMoveCardToDeadPile(LotroPhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P2, card, Zone.DEAD));
    }


    public void RemoveCardZone(String player, LotroPhysicalCardImpl card) {
        if(card.getZone() != null)
        {
            _game.getGameState().removeCardsFromZone(player, new ArrayList<>() {{
                add(card);
            }});
        }
    }

    public void MoveCardToZone(String player, LotroPhysicalCardImpl card, Zone zone) {
        RemoveCardZone(player, card);
        _game.getGameState().addCardToZone(_game, card, zone);
    }

    public void FreepsAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetFreepsCard(cardName), count); }
    public void ShadowAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetShadowCard(cardName), count); }
    public void AddWoundsToChar(LotroPhysicalCardImpl card, int count) {
        for(int i = 0; i < count; i++)
        {
            _game.getGameState().addWound(card);
        }
    }

    public void AddTokensToCard(LotroPhysicalCardImpl card, int count) {
        _game.getGameState().addTokens(card, Token.findTokenForCulture(card.getBlueprint().getCulture()), count);
    }

    public void RemoveTokensFromCard(LotroPhysicalCardImpl card, int count) {
        _game.getGameState().removeTokens(card, Token.findTokenForCulture(card.getBlueprint().getCulture()), count);
    }


    public void AddThreats(int count) {
        _game.getGameState().addThreats(_game.getGameState().getCurrentPlayerId(), count);
    }

    public void RemoveThreats(int count) {
        _game.getGameState().removeThreats(_game.getGameState().getCurrentPlayerId(), count);
    }

    public int GetThreats() {
        return _game.getGameState().getThreats();
    }

    public void FreepsRemoveWoundsFromChar(String cardName, int count) { RemoveWoundsFromChar(GetFreepsCard(cardName), count); }
    public void ShadowRemoveWoundsFromChar(String cardName, int count) { RemoveWoundsFromChar(GetShadowCard(cardName), count); }
    public void RemoveWoundsFromChar(LotroPhysicalCardImpl card, int count) {
        for(int i = 0; i < count; i++)
        {
            _game.getGameState().removeWound(card);
        }
    }

    public void AddBurdens(int count) {
        _game.getGameState().addBurdens(count);
    }

    public void RemoveBurdens(int count) {
        _game.getGameState().removeBurdens(count);
    }

    public int GetTwilight() { return _game.getGameState().getTwilightPool(); }
    public void SetTwilight(int amount) { _game.getGameState().setTwilight(amount); }

    public int GetMoveLimit() { return _game.getModifiersQuerying().getMoveLimit(_game, 2); }

    public int GetMoveCount() { return _game.getGameState().getMoveCount(); }

    public LotroPhysicalCardImpl GetRingBearer() { return (LotroPhysicalCardImpl)_game.getGameState().getRingBearer(P1); }

    public boolean RBWearingOneRing() { return _game.getGameState().isWearingRing(); }
    public LotroPhysicalCardImpl GetCurrentSite() { return (LotroPhysicalCardImpl)_game.getGameState().getCurrentSite(); }

    public void SkipToAssignments() throws DecisionResultInvalidException {
        SkipToPhase(Phase.ASSIGNMENT);
        PassCurrentPhaseActions();
    }
    public void SkipToPhase(Phase target) throws DecisionResultInvalidException {
        for(int attempts = 1; attempts <= 20; attempts++)
        {
            Phase current = _game.getGameState().getCurrentPhase();
            if(current == target)
                break;

            if(current == Phase.FELLOWSHIP) {
                FreepsPassCurrentPhaseAction();
            }
            else if(current == Phase.SHADOW) {
                ShadowPassCurrentPhaseAction();
            }
            else {
                PassCurrentPhaseActions();
            }

            if(attempts == 20)
            {
                throw new DecisionResultInvalidException("Could not arrive at target '" + target + "' after 20 attempts!");
            }
        }
    }

    public void SkipToPhaseInverted(Phase target) throws DecisionResultInvalidException {
        for(int attempts = 1; attempts <= 20; attempts++)
        {
            Phase current = _game.getGameState().getCurrentPhase();
            if(current == target)
                break;

            if(current == Phase.FELLOWSHIP) {
                ShadowPassCurrentPhaseAction();
            }
            else if(current == Phase.SHADOW) {
                FreepsPassCurrentPhaseAction();
            }
            else {
                PassCurrentPhaseActions();
            }

            if(attempts == 20)
            {
                throw new DecisionResultInvalidException("Could not arrive at target '" + target + "' after 20 attempts!");
            }
        }
    }

    public void PassCurrentPhaseActions() throws DecisionResultInvalidException {
        FreepsPassCurrentPhaseAction();
        ShadowPassCurrentPhaseAction();
    }

    public void FreepsPassCurrentPhaseAction() throws DecisionResultInvalidException {
        if(_userFeedback.getAwaitingDecision(P1) != null) {
            playerDecided(P1, "");
        }
    }

    public void ShadowPassCurrentPhaseAction() throws DecisionResultInvalidException {
        if(_userFeedback.getAwaitingDecision(P2) != null) {
            playerDecided(P2, "");
        }
    }

    public void FreepsDismissRevealedCards() throws DecisionResultInvalidException { FreepsPassCurrentPhaseAction(); }
    public void ShadowDismissRevealedCards() throws DecisionResultInvalidException { ShadowPassCurrentPhaseAction(); }
    public void DismissRevealedCards() throws DecisionResultInvalidException {
        FreepsDismissRevealedCards();
        ShadowDismissRevealedCards();
    }

    public void FreepsDeclineAssignments() throws DecisionResultInvalidException { FreepsPassCurrentPhaseAction(); }
    public void ShadowDeclineAssignments() throws DecisionResultInvalidException { ShadowPassCurrentPhaseAction(); }

    public void FreepsAssignToMinions(LotroPhysicalCardImpl comp, LotroPhysicalCardImpl...minions) throws DecisionResultInvalidException { AssignToMinions(P1, comp, minions); }
    public void ShadowAssignToMinions(LotroPhysicalCardImpl comp, LotroPhysicalCardImpl...minions) throws DecisionResultInvalidException { AssignToMinions(P2, comp, minions); }
    public void AssignToMinions(String player, LotroPhysicalCardImpl comp, LotroPhysicalCardImpl...minions) throws DecisionResultInvalidException {
        String result = comp.getCardId() + "";

        for (LotroPhysicalCardImpl minion : minions) {
            result += " " + minion.getCardId();
        }

        playerDecided(player, result);
    }

    public void FreepsAssignToMinions(LotroPhysicalCardImpl[]...groups) throws DecisionResultInvalidException { AssignToMinions(P1, groups); }
    public void ShadowAssignToMinions(LotroPhysicalCardImpl[]...groups) throws DecisionResultInvalidException { AssignToMinions(P2, groups); }
    public void AssignToMinions(String player, LotroPhysicalCardImpl[]...groups) throws DecisionResultInvalidException {
        String result = "";

        for (LotroPhysicalCardImpl[] group : groups) {
            result += group[0].getCardId();
            for(int i = 1; i < group.length; i++)
            {
                result += " " + group[i].getCardId();
            }
            result += ",";
        }

        playerDecided(player, result);
    }


    public List<LotroPhysicalCardImpl> FreepsGetAttachedCards(String name) { return GetAttachedCards(GetFreepsCard(name)); }
    public List<LotroPhysicalCardImpl> ShadowGetAttachedCards(String name) { return GetAttachedCards(GetShadowCard(name)); }
    public List<LotroPhysicalCardImpl> GetAttachedCards(LotroPhysicalCardImpl card) {
        return (List<LotroPhysicalCardImpl>)(List<?>)_game.getGameState().getAttachedCards(card);
    }

    public List<LotroPhysicalCardImpl> FreepsGetStackedCards(String name) { return GetStackedCards(GetFreepsCard(name)); }
    public List<LotroPhysicalCardImpl> ShadowGetStackedCards(String name) { return GetStackedCards(GetShadowCard(name)); }
    public List<LotroPhysicalCardImpl> GetStackedCards(LotroPhysicalCardImpl card) {
        return (List<LotroPhysicalCardImpl>)(List<?>)_game.getGameState().getStackedCards(card);
    }

    public void FreepsResolveSkirmish(String name) throws DecisionResultInvalidException { FreepsResolveSkirmish(GetFreepsCard(name)); }
    public void FreepsResolveSkirmish(LotroPhysicalCardImpl comp) throws DecisionResultInvalidException { FreepsChooseCard(comp); }

    public void FreepsChooseCard(String name) throws DecisionResultInvalidException { FreepsChooseCard(GetFreepsCard(name)); }
    public void FreepsChooseCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, String.valueOf(card.getCardId())); }
    public void ShadowChooseCard(String name) throws DecisionResultInvalidException { ShadowChooseCard(GetShadowCard(name)); }
    public void ShadowChooseCard(LotroPhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, String.valueOf(card.getCardId())); }

    public void FreepsChooseAnyCard() throws DecisionResultInvalidException { FreepsChoose(FreepsGetCardChoices().get(0)); }
    public void ShadowChooseAnyCard() throws DecisionResultInvalidException { ShadowChoose(ShadowGetCardChoices().get(0)); }

    public void FreepsChooseCards(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCards(P1, cards); }
    public void ShadowChooseCards(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCards(P2, cards); }
    public void ChooseCards(String player, LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException {
        String[] ids = new String[cards.length];

        for(int i = 0; i < cards.length; i++)
        {
            ids[i] = String.valueOf(cards[i].getCardId());
        }

        playerDecided(player, String.join(",", ids));
    }



    public boolean FreepsCanChooseCharacter(LotroPhysicalCardImpl card) { return FreepsGetCardChoices().contains(String.valueOf(card.getCardId())); }
    public boolean ShadowCanChooseCharacter(LotroPhysicalCardImpl card) { return ShadowGetCardChoices().contains(String.valueOf(card.getCardId())); }

    public int GetFreepsCardChoiceCount() { return FreepsGetCardChoices().size(); }
    public int GetShadowCardChoiceCount() { return ShadowGetCardChoices().size(); }

    public void FreepsChooseCardBPFromSelection(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCardBPFromSelection(P1, cards);}
    public void ShadowChooseCardBPFromSelection(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCardBPFromSelection(P2, cards);}

    public void ChooseCardBPFromSelection(String player, LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException {
        String[] choices = GetAwaitingDecisionParam(player,"blueprintId");
        ArrayList<String> bps = new ArrayList<>();
        ArrayList<LotroPhysicalCardImpl> found = new ArrayList<>();

        for(int i = 0; i < choices.length; i++)
        {
            for(LotroPhysicalCardImpl card : cards)
            {
                if(found.contains(card))
                    continue;

                if(card.getBlueprintId() == choices[i])
                {
                    // I have no idea why the spacing is required, but the BP parser skips to the fourth position
                    bps.add("    " + i);
                    found.add(card);
                    break;
                }
            }
        }

        playerDecided(player, String.join(",", bps));
    }

    public void FreepsChooseCardIDFromSelection(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCardIDFromSelection(P1, cards);}
    public void ShadowChooseCardIDFromSelection(LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCardIDFromSelection(P2, cards);}

    public void ChooseCardIDFromSelection(String player, LotroPhysicalCardImpl...cards) throws DecisionResultInvalidException {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(player);
        //playerDecided(player, "" + card.getCardId());

        String[] choices = GetAwaitingDecisionParam(player,"cardId");
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<LotroPhysicalCardImpl> found = new ArrayList<>();

        for (String choice : choices) {
            for (LotroPhysicalCardImpl card : cards) {
                if (found.contains(card))
                    continue;

                if (("" + card.getCardId()).equals(choice)) {
                    ids.add(choice);
                    found.add(card);
                    break;
                }
            }
        }

        playerDecided(player, String.join(",", ids));
    }

    public boolean IsCharAssigned(LotroPhysicalCardImpl card) {
        List<Assignment> assigns = _game.getGameState().getAssignments();
        return assigns.stream().anyMatch(x -> x.getFellowshipCharacter() == card || x.getShadowCharacters().contains(card));
    }

    public boolean IsCharSkirmishing(LotroPhysicalCardImpl card) {
        var skirmish = _game.getGameState().getSkirmish();
        return skirmish.getFellowshipCharacter() == card ||
                skirmish.getShadowCharacters().stream().anyMatch(x -> x == card);
    }

    public Skirmish GetActiveSkirmish() { return _game.getGameState().getSkirmish(); }

    public boolean IsAttachedTo(LotroPhysicalCardImpl card, LotroPhysicalCardImpl bearer) {
        if(card.getZone() != Zone.ATTACHED) {
            return false;
        }

        return bearer == card.getAttachedTo();
    }


    public int FreepsGetStrength(String name) { return GetStrength(GetFreepsCard(name)); }
    public int ShadowGetStrength(String name) { return GetStrength(GetShadowCard(name)); }
    public int GetStrength(LotroPhysicalCardImpl card)
    {
        return _game.getModifiersQuerying().getStrength(_game, card);
    }
    public int GetVitality(LotroPhysicalCardImpl card) { return _game.getModifiersQuerying().getVitality(_game, card); }
    public int GetResistance(LotroPhysicalCardImpl card) { return _game.getModifiersQuerying().getResistance(_game, card); }
    public int GetMinionSiteNumber(LotroPhysicalCardImpl card) { return _game.getModifiersQuerying().getMinionSiteNumber(_game, card); }
    public int GetGeneralSiteNumber(LotroPhysicalCardImpl card)
    {
        int bpNumber = card.getBlueprint().getSiteNumber();
        Integer siteNumber = card.getSiteNumber();
        if(siteNumber == null)
            return bpNumber;

        return siteNumber;
    }

    public boolean HasKeyword(LotroPhysicalCardImpl card, Keyword keyword)
    {
        return _game.getModifiersQuerying().hasKeyword(_game, card, keyword);
    }

    public int GetKeywordCount(LotroPhysicalCardImpl card, Keyword keyword)
    {
        return _game.getModifiersQuerying().getKeywordCount(_game, card, keyword);
    }

    public boolean IsType(LotroPhysicalCardImpl card, CardType type)
    {
        return card.getBlueprint().getCardType() == type
            || _game.getModifiersQuerying().isAdditionalCardType(_game, card, type);
    }

    public Boolean CanBeAssigned(LotroPhysicalCardImpl card)
    {
        return CanBeAssigned(card, Side.SHADOW) || CanBeAssigned(card, Side.FREE_PEOPLE);
    }

    public Boolean CanBeAssigned(LotroPhysicalCardImpl card, Side side)
    {
        return _game.getModifiersQuerying().canBeAssignedToSkirmish(_game, side, card);
    }




    public void ApplyAdHocModifier(Modifier mod)
    {
        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(mod);
    }

    public void ApplyAdHocAction(ActionProxy action)
    {
        _game.getActionsEnvironment().addUntilEndOfTurnActionProxy(action);
    }

    public void FreepsChoose(String choice) throws DecisionResultInvalidException { playerDecided(P1, choice); }
    public void FreepsChoose(String...choices) throws DecisionResultInvalidException { playerDecided(P1, String.join(",", choices)); }
    public void ShadowChoose(String choice) throws DecisionResultInvalidException { playerDecided(P2, choice); }
    public void ShadowChoose(String...choices) throws DecisionResultInvalidException { playerDecided(P2, String.join(",", choices)); }


    public void FreepsChooseToMove() throws DecisionResultInvalidException { playerDecided(P1, "0"); }
    public void FreepsChooseToStay() throws DecisionResultInvalidException { playerDecided(P1, "1"); }
    public void ShadowChooseToStay() throws DecisionResultInvalidException { playerDecided(P2, "1"); }

    public boolean FreepsHasOptionalTriggerAvailable() { return FreepsDecisionAvailable("Optional"); }
    public boolean ShadowHasOptionalTriggerAvailable() { return ShadowDecisionAvailable("Optional"); }

    public void FreepsAcceptOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P1, "0"); }
    public void FreepsDeclineOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P1, ""); }
    public void ShadowAcceptOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P2, "0"); }
    public void ShadowDeclineOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P2, ""); }

    public void FreepsDeclineReconciliation() throws DecisionResultInvalidException { FreepsPassCurrentPhaseAction(); }
    public void ShadowDeclineReconciliation() throws DecisionResultInvalidException { ShadowPassCurrentPhaseAction(); }

    public void FreepsChooseYes() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, "Yes"); }
    public void ShadowChooseYes() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, "Yes"); }
    public void FreepsChooseNo() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, "No"); }
    public void ShadowChooseNo() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, "No"); }
    public void FreepsChooseMultipleChoiceOption(String option) throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, option); }
    public void ShadowChooseMultipleChoiceOption(String option) throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, option); }
    public void ChooseMultipleChoiceOption(String playerID, String option) throws DecisionResultInvalidException { ChooseAction(playerID, "results", option); }
    public void FreepsChooseAction(String paramName, String option) throws DecisionResultInvalidException { ChooseAction(P1, paramName, option); }
    public void FreepsChooseAction(String option) throws DecisionResultInvalidException { ChooseAction(P1, "actionText", option); }
    public void ShadowChooseAction(String paramName, String option) throws DecisionResultInvalidException { ChooseAction(P2, paramName, option); }
    public void ShadowChooseAction(String option) throws DecisionResultInvalidException { ChooseAction(P2, "actionText", option); }
    public void ChooseAction(String playerID, String paramName, String option) throws DecisionResultInvalidException {
        List<String> choices = GetADParamAsList(playerID, paramName);
        for(String choice : choices){
            if(choice.toLowerCase().contains(option.toLowerCase())) {
                playerDecided(playerID, String.valueOf(choices.indexOf(choice)));
                return;
            }
        }
        //couldn't find an exact match, so maybe it's a direct index:
        playerDecided(playerID, option);
    }

    public void FreepsResolveActionOrder(String option) throws DecisionResultInvalidException { ChooseAction(P1, "actionText", option); }

    public Filterable GenerateFreepsFilter(String filter) throws InvalidCardDefinitionException {
        return FilterFactory.generateFilter(filter, Environment).getFilterable(FreepsFilterContext);
    }
    public Filterable GenerateShadowFilter(String filter) throws InvalidCardDefinitionException {
        return FilterFactory.generateFilter(filter, Environment).getFilterable(ShadowFilterContext);
    }

    public void ApplyAdHocFreepsAutoDiscard(String filter)  {
        try{
            ApplyAdHocAutoDiscard(GenerateFreepsFilter(filter));
        }
        catch(InvalidCardDefinitionException ex) {}

    }

    public void ApplyAdHocShadowAutoDiscard(String filter)  {
        try {
            ApplyAdHocAutoDiscard(GenerateShadowFilter(filter));
        }
        catch(InvalidCardDefinitionException ex) {}
    }

    private void ApplyAdHocAutoDiscard(Filterable...filterables)  {

        ApplyAdHocAction(new AbstractActionProxy() {
            @Override
            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult)  {
                RequiredTriggerAction action = new RequiredTriggerAction(null);
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(P2, null, filterables));
                return Collections.singletonList(action);
            }
        });
    }

    public void AcknowledgeReveal() throws DecisionResultInvalidException
    {
        playerDecided(P1, "");
        playerDecided(P2, "");
    }

    public int GetOverwhelmMultiplier(LotroPhysicalCardImpl card)
    {
        return _game.getModifiersQuerying().getOverwhelmMultiplier(_game, card);
    }

    public void QuickSkip(int times) throws DecisionResultInvalidException {
        for(int i = 0; i <= times; i++) {
            SkipToPhase(Phase.REGROUP);
            PassCurrentPhaseActions();
            ShadowDeclineReconciliation();
            FreepsChooseToMove();
        }
    }

    public void SkipToSite(int siteNum) throws DecisionResultInvalidException {
        for(int i = GetCurrentSite().getSiteNumber(); i < siteNum; i++)
        {
            SkipCurrentSite();
        }
    }

    public void SkipCurrentSite() throws DecisionResultInvalidException {
        SkipToPhase(Phase.REGROUP);
        LotroPhysicalCardImpl site = GetCurrentSite();
        if(site.getSiteNumber() == 9)
            return; // Game finished
        PassCurrentPhaseActions();
        if(ShadowDecisionAvailable("reconcile"))
        {
            ShadowDeclineReconciliation();
        }
        if(ShadowDecisionAvailable("discard down"))
        {
            ShadowChooseCard((LotroPhysicalCardImpl) GetShadowHand().get(0));
        }
        if(FreepsDecisionAvailable("another move"))
        {
            FreepsChooseToStay();
        }
        if(FreepsDecisionAvailable("reconcile"))
        {
            FreepsDeclineReconciliation();
        }
        if(FreepsDecisionAvailable("discard down"))
        {
            FreepsChooseCard((LotroPhysicalCardImpl) GetFreepsHand().get(0));
        }

        //Shadow player
        SkipToPhaseInverted(Phase.REGROUP);
        ShadowPassCurrentPhaseAction(); // actually freeps with the swap
        FreepsPassCurrentPhaseAction(); // actually shadow with the swap
        if(FreepsDecisionAvailable("reconcile"))
        {
            FreepsDeclineReconciliation();
        }
        if(FreepsDecisionAvailable("discard down"))
        {
            FreepsChooseCard((LotroPhysicalCardImpl) GetFreepsHand().get(0));
        }
        if(ShadowDecisionAvailable("another move"))
        {
            ShadowChoose("1"); // Choose to stay
        }
        if(ShadowDecisionAvailable("reconcile"))
        {
            ShadowDeclineReconciliation();
        }
        if(ShadowDecisionAvailable("discard down"))
        {
            ShadowChooseCard((LotroPhysicalCardImpl) GetShadowHand().get(0));
        }

        Assert.assertTrue(GetCurrentPhase() == Phase.BETWEEN_TURNS
                || GetCurrentPhase() == Phase.FELLOWSHIP);
    }
}
