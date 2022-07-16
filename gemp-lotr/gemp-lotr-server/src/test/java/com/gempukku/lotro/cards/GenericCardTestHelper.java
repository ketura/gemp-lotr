package com.gempukku.lotro.cards;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class GenericCardTestHelper extends AbstractAtTest {

    private int LastCardID = 100;

    public static final HashMap<String, String> FellowshipSites = new HashMap<String, String>() {{
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

    public static final HashMap<String, String> KingSites = new HashMap<String, String>() {{
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
    public static final String FOTRRing = "1_2";


    // Player key, then name/card
    public Map<String, Map<String, PhysicalCardImpl>> Cards = new HashMap<>();

    public GenericCardTestHelper(Map<String, String> cardIDs) throws CardNotFoundException, DecisionResultInvalidException {
        this(cardIDs, null, null, null);
    }

    public GenericCardTestHelper(Map<String, String> cardIDs, Map<String, String> siteIDs, String ringBearerID, String ringID) throws CardNotFoundException, DecisionResultInvalidException {
        super();

        if(siteIDs == null || ringBearerID == null || ringID == null) {
            initializeSimplestGame();
        }
        else {
            Map<String, LotroDeck> decks = new HashMap<String, LotroDeck>();
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

            initializeGameWithDecks(decks);
        }

        Cards.put(P1, new HashMap<>());
        Cards.put(P2, new HashMap<>());

        if(cardIDs != null) {
            for(String name : cardIDs.keySet()) {
                String id = cardIDs.get(name);
                PhysicalCardImpl card = CreateCard(P1, id);
                Cards.get(P1).put(name, card);
                FreepsMoveCardsToTopOfDeck(card);

                card = CreateCard(P2, id);
                Cards.get(P2).put(name, card);
                FreepsMoveCardsToTopOfDeck(card);
            }
        }
    }

    public PhysicalCardImpl CreateCard(String playerID, String cardID) throws CardNotFoundException {
        return new PhysicalCardImpl(LastCardID++, cardID, playerID, _library.getLotroCardBlueprint(cardID));
    }

    public void StartGame() throws DecisionResultInvalidException {
        skipMulligans();
    }

    public PhysicalCardImpl GetFreepsCard(String cardName) { return Cards.get(P1).get(cardName); }
    public PhysicalCardImpl GetShadowCard(String cardName) { return Cards.get(P2).get(cardName); }
    public PhysicalCardImpl GetFreepsCardByID(String id) { return GetCardByID(P1, Integer.parseInt(id)); }
    public PhysicalCardImpl GetFreepsCardByID(int id) { return GetCardByID(P1, id); }
    public PhysicalCardImpl GetShadowCardByID(String id) { return GetCardByID(P2, Integer.parseInt(id)); }
    public PhysicalCardImpl GetShadowCardByID(int id) { return GetCardByID(P2, id); }
    public PhysicalCardImpl GetCardByID(String player, int id) {
        return Cards.get(player).values().stream()
                .filter(x -> x.getCardId() == id)
                .findFirst().orElse(null);
    }

    public PhysicalCardImpl GetFreepsSite(int siteNum) { return GetSite(P1, siteNum); }
    public PhysicalCardImpl GetShadowSite(int siteNum) { return GetSite(P2, siteNum); }
    public PhysicalCardImpl GetSite(String playerID, int siteNum)
    {
        List<PhysicalCardImpl> advDeck = (List<PhysicalCardImpl>)_game.getGameState().getAdventureDeck(playerID);
//        for (PhysicalCardImpl card : advDeck) {
//            if (card.getSiteNumber() == siteNum){
//                return card;
//            }
//        }
        return advDeck.stream().filter(x -> x.getBlueprint().getSiteNumber() == siteNum).findFirst().get();
    }

    public List<String> FreepsGetAvailableActions() { return GetAvailableActions(P1); }
    public List<String> ShadowGetAvailableActions() { return GetAvailableActions(P2); }
    public List<String> GetAvailableActions(String playerID) {
        AwaitingDecision decision = GetAwaitingDecision(playerID);
        if(decision == null) {
            return new ArrayList<String>();
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
    public Boolean FreepsCardActionAvailable(PhysicalCardImpl card) { return ActionAvailable(P1, "Use " + GameUtils.getFullName(card)); }
    public Boolean FreepsCardPlayAvailable(PhysicalCardImpl card) { return ActionAvailable(P1, "Play " + GameUtils.getFullName(card)); }
    public Boolean ShadowActionAvailable(String action) { return ActionAvailable(P2, action); }
    public Boolean ShadowCardActionAvailable(PhysicalCardImpl card) { return ActionAvailable(P2, "Use " + GameUtils.getFullName(card)); }
    public Boolean ShadowCardPlayAvailable(PhysicalCardImpl card) { return ActionAvailable(P2, "Play " + GameUtils.getFullName(card)); }
    public Boolean ActionAvailable(String player, String action) {
        List<String> actions = GetAvailableActions(player);
        if(actions == null)
            return false;
        String lowerAction = action.toLowerCase();
        return actions.stream().anyMatch(x -> x.toLowerCase().contains(lowerAction));
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
    public List<String> FreepsGetMultipleChoices() { return GetADParamAsList(P1, "results"); }
    public List<String> ShadowGetMultipleChoices() { return GetADParamAsList(P2, "results"); }
    public List<String> FreepsGetADParamAsList(String paramName) { return GetADParamAsList(P1, paramName); }
    public List<String> ShadowGetADParamAsList(String paramName) { return GetADParamAsList(P2, paramName); }
    public List<String> GetADParamAsList(String playerID, String paramName) { return Arrays.asList(GetAwaitingDecisionParam(playerID, paramName)); }
    public String[] FreepsGetADParam(String paramName) { return GetAwaitingDecisionParam(P1, paramName); }
    public String[] ShadowGetADParam(String paramName) { return GetAwaitingDecisionParam(P2, paramName); }
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
    public void FreepsUseCardAction(PhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, "Use " + GameUtils.getFullName(card))); }
    public void ShadowUseCardAction(String name) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, name)); }
    public void ShadowUseCardAction(PhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, "Use " + GameUtils.getFullName(card))); }

    public void FreepsPlayCard(String name) throws DecisionResultInvalidException { FreepsPlayCard(GetFreepsCard(name)); }
    public void FreepsPlayCard(PhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, "Play " + GameUtils.getFullName(card))); }
    public void ShadowPlayCard(String name) throws DecisionResultInvalidException { ShadowPlayCard(GetShadowCard(name)); }
    public void ShadowPlayCard(PhysicalCardImpl card) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, "Play " + GameUtils.getFullName(card))); }

    public int FreepsGetWoundsOn(String cardName) { return GetWoundsOn(GetFreepsCard(cardName)); }
    public int ShadowGetWoundsOn(String cardName) { return GetWoundsOn(GetShadowCard(cardName)); }
    public int GetWoundsOn(PhysicalCardImpl card) { return _game.getGameState().getWounds(card); }

    public int GetBurdens() { return _game.getGameState().getBurdens(); }

    public int GetFreepsArcheryTotal() { return RuleUtils.calculateFellowshipArcheryTotal(_game); }
    public int GetShadowArcheryTotal() { return RuleUtils.calculateShadowArcheryTotal(_game); }

    public int GetFreepsHandCount() { return GetPlayerHandCount(P1); }
    public int GetShadowHandCount() { return GetPlayerHandCount(P2); }
    public int GetPlayerHandCount(String player)
    {
        return _game.getGameState().getHand(player).size();
    }

    public int GetFreepsDeckCount() { return GetPlayerDeckCount(P1); }
    public int GetShadowDeckCount() { return GetPlayerDeckCount(P2); }
    public int GetPlayerDeckCount(String player)
    {
        return _game.getGameState().getDeck(player).size();
    }

    public PhysicalCardImpl GetFreepsBottomOfDeck() { return GetPlayerBottomOfDeck(P1); }
    public PhysicalCardImpl GetShadowBottomOfDeck() { return GetPlayerBottomOfDeck(P2); }
    public PhysicalCardImpl GetPlayerBottomOfDeck(String player)
    {
        List deck = _game.getGameState().getDeck(player);
        return (PhysicalCardImpl) deck.get(deck.size() - 1);
    }

    public PhysicalCardImpl GetFreepsTopOfDeck() { return GetPlayerTopOfDeck(P1); }
    public PhysicalCardImpl GetShadowTopOfDeck() { return GetPlayerTopOfDeck(P2); }
    public PhysicalCardImpl GetPlayerTopOfDeck(String player) { return (PhysicalCardImpl) _game.getGameState().getDeck(player).get(0); }

    public int GetFreepsDiscardCount() { return GetPlayerDiscardCount(P1); }
    public int GetShadowDiscardCount() { return GetPlayerDiscardCount(P2); }
    public int GetPlayerDiscardCount(String player)
    {
        return _game.getGameState().getDiscard(player).size();
    }

    public Phase GetCurrentPhase() { return _game.getGameState().getCurrentPhase(); }



    public void FreepsMoveCardToHand(String...names) {
        for(String name : names) {
            FreepsMoveCardToHand(GetFreepsCard(name));
        }
    }
    public void FreepsMoveCardToHand(PhysicalCardImpl...cards) {
        for(PhysicalCardImpl card : cards) {
            RemoveCardZone(P1, card);
            MoveCardToZone(P1, card, Zone.HAND);
        }
    }
    public void ShadowMoveCardToHand(String...names) {
        for(String name : names) {
            ShadowMoveCardToHand(GetShadowCard(name));
        }
    }
    public void ShadowMoveCardToHand(PhysicalCardImpl...cards) {
        for(PhysicalCardImpl card : cards) {
            RemoveCardZone(P2, card);
            MoveCardToZone(P2, card, Zone.HAND);
        }
    }

    public void FreepsAttachCardsTo(PhysicalCardImpl bearer, String...names) {
        Arrays.stream(names).forEach(name -> AttachCardsTo(bearer, GetFreepsCard(name)));
    }
    public void ShadowAttachCardsTo(PhysicalCardImpl bearer, String...names) {
        Arrays.stream(names).forEach(name -> AttachCardsTo(bearer, GetShadowCard(name)));
    }
    public void AttachCardsTo(PhysicalCardImpl bearer, PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().attachCard(_game, card, bearer);
        });
    }

    public void FreepsStackCardsOn(PhysicalCardImpl on, String...cardNames) {
        Arrays.stream(cardNames).forEach(name -> StackCardsOn(on, GetFreepsCard(name)));
    }
    public void ShadowStackCardsOn(PhysicalCardImpl on, String...cardNames) {
        Arrays.stream(cardNames).forEach(name -> StackCardsOn(on, GetShadowCard(name)));
    }
    public void StackCardsOn(PhysicalCardImpl on, PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().stackCard(_game, card, on);
        });
    }

    public void FreepsMoveCardsToTopOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsMoveCardsToTopOfDeck(GetFreepsCard(cardName)));
    }
    public void FreepsMoveCardsToTopOfDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }
    public void ShadowMoveCardsToTopOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowMoveCardsToTopOfDeck(GetShadowCard(cardName)));
    }
    public void ShadowMoveCardsToTopOfDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }

    public void FreepsMoveCardsToBottomOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsMoveCardsToBottomOfDeck(GetFreepsCard(cardName)));
    }
    public void FreepsMoveCardsToBottomOfDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }
    public void ShadowMoveCardsToBottomOfDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowMoveCardsToBottomOfDeck(GetShadowCard(cardName)));
    }
    public void ShadowMoveCardsToBottomOfDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }

    public void FreepsShuffleCardsInDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> FreepsShuffleCardsInDeck(GetFreepsCard(cardName)));
    }
    public void FreepsShuffleCardsInDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }
    public void ShadowShuffleCardsInDeck(String...cardNames) {
        Arrays.stream(cardNames).forEach(cardName -> ShadowShuffleCardsInDeck(GetShadowCard(cardName)));
    }
    public void ShadowShuffleCardsInDeck(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> {
            RemoveCardZone(card.getOwner(), card);
            _game.getGameState().putCardOnTopOfDeck(card);
        });
    }

    public void FreepsMoveCharToTable(String...names) {
        Arrays.stream(names).forEach(name -> FreepsMoveCharToTable(GetFreepsCard(name)));
    }
    public void FreepsMoveCharToTable(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.FREE_CHARACTERS));
    }
    public void ShadowMoveCharToTable(String...names) {
        Arrays.stream(names).forEach(name -> ShadowMoveCharToTable(GetShadowCard(name)));
    }
    public void ShadowMoveCharToTable(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P2, card, Zone.SHADOW_CHARACTERS));
    }


    public void FreepsMoveCardToSupportArea(String cardName) { FreepsMoveCardToSupportArea(GetFreepsCard(cardName)); }
    public void FreepsMoveCardToSupportArea(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.SUPPORT));
    }
    public void ShadowMoveCardToSupportArea(String cardName) { ShadowMoveCardToSupportArea(GetShadowCard(cardName)); }
    public void ShadowMoveCardToSupportArea(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.SUPPORT));
    }

    public void FreepsMoveCardToDiscard(String cardName) { FreepsMoveCardToDiscard(GetFreepsCard(cardName)); }
    public void FreepsMoveCardToDiscard(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.DISCARD));
    }
    public void ShadowMoveCardToDiscard(String cardName) { ShadowMoveCardToDiscard(GetShadowCard(cardName)); }
    public void ShadowMoveCardToDiscard(PhysicalCardImpl...cards) {
        Arrays.stream(cards).forEach(card -> MoveCardToZone(P1, card, Zone.DISCARD));
    }


    public void RemoveCardZone(String player, PhysicalCardImpl card) {
        if(card.getZone() != null)
        {
            _game.getGameState().removeCardsFromZone(player, new ArrayList<PhysicalCard>() {{ add(card); }});
        }
    }

    public void MoveCardToZone(String player, PhysicalCardImpl card, Zone zone) {
        RemoveCardZone(player, card);
        _game.getGameState().addCardToZone(_game, card, zone);
    }

    public void FreepsAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetFreepsCard(cardName), count); }
    public void ShadowAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetShadowCard(cardName), count); }
    public void AddWoundsToChar(PhysicalCardImpl card, int count) {
        for(int i = 0; i < count; i++)
        {
            _game.getGameState().addWound(card);
        }
    }

    public void FreepsRemoveWoundsFromChar(String cardName, int count) { RemoveWoundsFromChar(GetFreepsCard(cardName), count); }
    public void ShadowRemoveWoundsFromChar(String cardName, int count) { RemoveWoundsFromChar(GetShadowCard(cardName), count); }
    public void RemoveWoundsFromChar(PhysicalCardImpl card, int count) {
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

    public int GetMoveLimit() { return _game.getGameState().getMoveCount(); }

    public PhysicalCardImpl GetRingBearer() { return (PhysicalCardImpl)_game.getGameState().getRingBearer(P1); }

    public PhysicalCardImpl GetCurrentSite() { return (PhysicalCardImpl)_game.getGameState().getCurrentSite(); }

    public void SkipToPhase(Phase target) throws DecisionResultInvalidException {
        for(int attempts = 1; attempts <= 20; attempts++)
        {
            Phase current = _game.getGameState().getCurrentPhase();
            if(current == target)
                break;

            SkipCurrentPhaseActions();

            if(attempts == 20)
            {
                throw new DecisionResultInvalidException("Could not arrive at target '" + target + "' after 20 attempts!");
            }
        }
    }

    public void SkipCurrentPhaseActions() throws DecisionResultInvalidException {
        FreepsSkipCurrentPhaseAction();
        ShadowSkipCurrentPhaseAction();
    }

    public void FreepsSkipCurrentPhaseAction() throws DecisionResultInvalidException {
        if(_userFeedback.getAwaitingDecision(P1) != null) {
            playerDecided(P1, "");
        }
    }

    public void ShadowSkipCurrentPhaseAction() throws DecisionResultInvalidException {
        if(_userFeedback.getAwaitingDecision(P2) != null) {
            playerDecided(P2, "");
        }
    }


    public void FreepsAssignToMinions(PhysicalCardImpl comp, PhysicalCardImpl...minions) throws DecisionResultInvalidException { AssignToMinions(P1, comp, minions); }
    public void ShadowAssignToMinions(PhysicalCardImpl comp, PhysicalCardImpl...minions) throws DecisionResultInvalidException { AssignToMinions(P2, comp, minions); }
    public void AssignToMinions(String player, PhysicalCardImpl comp, PhysicalCardImpl...minions) throws DecisionResultInvalidException {
        String result = comp.getCardId() + "";

        for (PhysicalCardImpl minion : minions) {
            result += " " + minion.getCardId();
        }

        playerDecided(player, result);
    }

    public void FreepsAssignToMinions(PhysicalCardImpl[]...groups) throws DecisionResultInvalidException { AssignToMinions(P1, groups); }
    public void ShadowAssignToMinions(PhysicalCardImpl[]...groups) throws DecisionResultInvalidException { AssignToMinions(P2, groups); }
    public void AssignToMinions(String player, PhysicalCardImpl[]...groups) throws DecisionResultInvalidException {
        String result = "";

        for (PhysicalCardImpl[] group : groups) {
            result += group[0].getCardId();
            for(int i = 1; i < group.length; i++)
            {
                result += " " + group[i].getCardId();
            }
            result += ",";
        }

        playerDecided(player, result);
    }


    public List<PhysicalCardImpl> FreepsGetAttachedCards(String name) { return GetAttachedCards(GetFreepsCard(name)); }
    public List<PhysicalCardImpl> ShadowGetAttachedCards(String name) { return GetAttachedCards(GetShadowCard(name)); }
    public List<PhysicalCardImpl> GetAttachedCards(PhysicalCardImpl card) {
        return (List<PhysicalCardImpl>)(List<?>)_game.getGameState().getAttachedCards(card);
    }

    public List<PhysicalCardImpl> FreepsGetStackedCards(String name) { return GetStackedCards(GetFreepsCard(name)); }
    public List<PhysicalCardImpl> ShadowGetStackedCards(String name) { return GetStackedCards(GetShadowCard(name)); }
    public List<PhysicalCardImpl> GetStackedCards(PhysicalCardImpl card) {
        return (List<PhysicalCardImpl>)(List<?>)_game.getGameState().getStackedCards(card);
    }

    public void FreepsResolveSkirmish(String name) throws DecisionResultInvalidException { FreepsResolveSkirmish(GetFreepsCard(name)); }
    public void FreepsResolveSkirmish(PhysicalCardImpl comp) throws DecisionResultInvalidException { FreepsChooseCard(comp); }
    public void FreepsChooseCard(PhysicalCardImpl card) throws DecisionResultInvalidException {
        playerDecided(P1, String.valueOf(card.getCardId()));
    }
    public void ShadowChooseCard(PhysicalCardImpl card) throws DecisionResultInvalidException {
        playerDecided(P2, String.valueOf(card.getCardId()));
    }

    public void FreepsChooseCards(PhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCards(P1, cards); }
    public void ShadowChooseCards(PhysicalCardImpl...cards) throws DecisionResultInvalidException { ChooseCards(P2, cards); }
    public void ChooseCards(String player, PhysicalCardImpl...cards) throws DecisionResultInvalidException {
        String[] ids = new String[cards.length];

        for(int i = 0; i < cards.length; i++)
        {
            ids[i] = String.valueOf(cards[i].getCardId());
        }

        playerDecided(player, String.join(",", ids));
    }



    public boolean FreepsCanChooseCharacter(PhysicalCardImpl card) { return FreepsGetCardChoices().contains(String.valueOf(card.getCardId())); }
    public boolean ShadowCanChooseCharacter(PhysicalCardImpl card) { return ShadowGetCardChoices().contains(String.valueOf(card.getCardId())); }

    public int GetFreepsCardChoiceCount() { return FreepsGetCardChoices().size(); }
    public int GetShadowCardChoiceCount() { return ShadowGetCardChoices().size(); }

    public void FreepsChooseCardBPFromSelection(PhysicalCardImpl card) throws DecisionResultInvalidException { ChooseCardBPFromSelection(P1, card);}
    public void ShadowChooseCardBPFromSelection(PhysicalCardImpl card) throws DecisionResultInvalidException { ChooseCardBPFromSelection(P2, card);}

    public void ChooseCardBPFromSelection(String player, PhysicalCardImpl card) throws DecisionResultInvalidException {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(player);

        String[] ids = GetAwaitingDecisionParam(player,"blueprintId");
        for(int i = 0; i < ids.length; i++)
        {
            String id = ids[i];
            if(id == card.getBlueprintId())
            {
                // I have no idea why the spacing is required, but the BP parser skips to the fourth position
                playerDecided(player, "    " + i);
                return;
            }
        }

        playerDecided(player, card.getBlueprintId());
    }

    public void FreepsChooseCardIDFromSelection(PhysicalCardImpl card) throws DecisionResultInvalidException { ChooseCardIDFromSelection(P1, card);}
    public void ShadowChooseCardIDFromSelection(PhysicalCardImpl card) throws DecisionResultInvalidException { ChooseCardIDFromSelection(P2, card);}

    public void ChooseCardIDFromSelection(String player, PhysicalCardImpl card) throws DecisionResultInvalidException {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(player);
        playerDecided(player, "" + card.getCardId());
    }

    public boolean IsCharAssigned(PhysicalCardImpl card) {
        List<Assignment> assigns = _game.getGameState().getAssignments();
        return assigns.stream().anyMatch(x -> x.getFellowshipCharacter() == card || x.getShadowCharacters().contains(card));
    }

    public boolean IsAttachedTo(PhysicalCardImpl card, PhysicalCardImpl bearer) {
        if(card.getZone() != Zone.ATTACHED) {
            return false;
        }

        return bearer == card.getAttachedTo();
    }


    public int FreepsGetStrength(String name) { return GetStrength(GetFreepsCard(name)); }
    public int ShadowGetStrength(String name) { return GetStrength(GetShadowCard(name)); }
    public int GetStrength(PhysicalCardImpl card)
    {
        return _game.getModifiersQuerying().getStrength(_game, card);
    }
    public int GetVitality(PhysicalCardImpl card)
    {
        return _game.getModifiersQuerying().getVitality(_game, card);
    }
    public int GetSiteNumber(PhysicalCardImpl card) { return _game.getModifiersQuerying().getMinionSiteNumber(_game, card); }

    public boolean HasKeyword(PhysicalCardImpl card, Keyword keyword)
    {
        return _game.getModifiersQuerying().hasKeyword(_game, card, keyword);
    }

    public int GetKeywordCount(PhysicalCardImpl card, Keyword keyword)
    {
        return _game.getModifiersQuerying().getKeywordCount(_game, card, keyword);
    }


    public void ApplyAdHocModifier(Modifier mod)
    {
        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(mod);
    }

    public void FreepsChoose(String choice) throws DecisionResultInvalidException { playerDecided(P1, choice); }
    public void FreepsChoose(String...choices) throws DecisionResultInvalidException { playerDecided(P1, String.join(",", choices)); }
    public void ShadowChoose(String choice) throws DecisionResultInvalidException { playerDecided(P2, choice); }
    public void ShadowChoose(String...choices) throws DecisionResultInvalidException { playerDecided(P2, String.join(",", choices)); }


    public void FreepsChooseToMove() throws DecisionResultInvalidException { playerDecided(P1, "0"); }
    public void FreepsChooseToStay() throws DecisionResultInvalidException { playerDecided(P1, "1"); }

    public boolean FreepsHasOptionalTriggerAvailable() throws DecisionResultInvalidException { return FreepsDecisionAvailable("Optional"); }
    public boolean ShadowHasOptionalTriggerAvailable() throws DecisionResultInvalidException { return ShadowDecisionAvailable("Optional"); }

    public void FreepsAcceptOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P1, "0"); }
    public void FreepsDeclineOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P1, ""); }
    public void ShadowAcceptOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P2, "0"); }
    public void ShadowDeclineOptionalTrigger() throws DecisionResultInvalidException { playerDecided(P2, ""); }

    public void FreepsChooseYes() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, "Yes"); }
    public void ShadowChooseYes() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, "Yes"); }
    public void FreepsChooseNo() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, "No"); }
    public void ShadowChooseNo() throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, "No"); }
    public void FreepsChooseMultipleChoiceOption(String option) throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P1, option); }
    public void ShadowChooseMultipleChoiceOption(String option) throws DecisionResultInvalidException { ChooseMultipleChoiceOption(P2, option); }
    public void ChooseMultipleChoiceOption(String playerID, String option) throws DecisionResultInvalidException { ChooseAction(playerID, "results", option); }
    public void FreepsChooseAction(String paramName, String option) throws DecisionResultInvalidException { ChooseAction(P1, paramName, option); }
    public void ShadowChooseAction(String paramName, String option) throws DecisionResultInvalidException { ChooseAction(P2, paramName, option); }
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

}
