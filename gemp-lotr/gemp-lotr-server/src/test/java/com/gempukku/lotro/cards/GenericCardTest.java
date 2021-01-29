package com.gempukku.lotro.cards;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class GenericCardTest extends AbstractAtTest {

    private int LastCardID = 100;

    //TODO: refactor this to not have different freeps/shadows collections
    public Map<String, PhysicalCardImpl> freepsCards = new HashMap<>();
    public Map<String, PhysicalCardImpl> shadowCards = new HashMap<>();

    public GenericCardTest(Map<String, String> freepsIDs, Map<String, String> shadowIDs) throws CardNotFoundException, DecisionResultInvalidException {
        super();

        initializeSimplestGame();

        if(freepsIDs != null) {
            for(String name : freepsIDs.keySet()) {
                String id = freepsIDs.get(name);
                PhysicalCardImpl card = CreateCard(P1, id);
                freepsCards.put(name, card);
                FreepsMoveCardToDeck(card);
                ShadowMoveCardToDeck(CreateCard(P2, id));
            }
        }

        if(shadowIDs != null) {
            for(String name : shadowIDs.keySet()) {
                String id = shadowIDs.get(name);
                PhysicalCardImpl card = CreateCard(P2, id);
                shadowCards.put(name, card);
                ShadowMoveCardToDeck(card);
                FreepsMoveCardToDeck(CreateCard(P1, id));
            }
        }
    }

    public PhysicalCardImpl CreateCard(String playerID, String cardID) throws CardNotFoundException {
        return new PhysicalCardImpl(LastCardID++, cardID, playerID, _library.getLotroCardBlueprint(cardID));
    }

    public void StartGame() throws DecisionResultInvalidException {
        skipMulligans();
    }

    public PhysicalCardImpl GetFreepsCard(String cardName) { return freepsCards.get(cardName); }
    public PhysicalCardImpl GetShadowCard(String cardName) { return shadowCards.get(cardName); }

    public List<String> FreepsGetAvailableActions() { return GetAvailableActions(P1); }
    public List<String> ShadowGetAvailableActions() { return GetAvailableActions(P2); }
    public List<String> GetAvailableActions(String playerID) {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(playerID);
        return Arrays.asList((String[])decision.getDecisionParameters().get("actionText"));
    }

    public AwaitingDecision FreepsGetAwaitingDecision() { return GetAwaitingDecision(P1); }
    public AwaitingDecision ShadowGetAwaitingDecision() { return GetAwaitingDecision(P2); }
    public AwaitingDecision GetAwaitingDecision(String playerID) { return _userFeedback.getAwaitingDecision(playerID); }

    public void FreepsUseAction(String name) throws DecisionResultInvalidException { playerDecided(P1, getCardActionId(P1, name)); }
    public void ShadowUseAction(String name) throws DecisionResultInvalidException { playerDecided(P2, getCardActionId(P2, name)); }

    public int FreepsGetWoundsOn(String cardName) { return GetWoundsOn(GetFreepsCard(cardName)); }
    public int ShadowGetWoundsOn(String cardName) { return GetWoundsOn(GetShadowCard(cardName)); }
    public int GetWoundsOn(PhysicalCardImpl card) { return _game.getGameState().getWounds(card); }

    public int GetBurdens() { return _game.getGameState().getBurdens(); }

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

    public Phase GetCurrentPhase() { return _game.getGameState().getCurrentPhase(); }

    public Boolean FreepsActionAvailable(String action) { return ActionAvailable(P1, action); }
    public Boolean ShadowActionAvailable(String action) { return ActionAvailable(P2, action); }
    public Boolean ActionAvailable(String player, String action) {
        List<String> actions = GetAvailableActions(player);
        String lowerAction = action.toLowerCase();
        return actions.stream().anyMatch(x -> x.toLowerCase().startsWith(lowerAction));
    }

    public void FreepsMoveCardToHand(String cardName) { MoveCardToZone(P1, GetFreepsCard(cardName), Zone.HAND); }
    public void FreepsMoveCardToHand(PhysicalCardImpl card) { MoveCardToZone(P1, card, Zone.HAND); }
    public void ShadowMoveCardToHand(String cardName) { MoveCardToZone(P2, GetShadowCard(cardName), Zone.HAND); }
    public void ShadowMoveCardToHand(PhysicalCardImpl card) { MoveCardToZone(P2, card, Zone.HAND); }

    public void FreepsMoveCardToDeck(String cardName) { MoveCardToZone(P1, GetFreepsCard(cardName), Zone.DECK); }
    public void FreepsMoveCardToDeck(PhysicalCardImpl card) { MoveCardToZone(P1, card, Zone.DECK); }
    public void ShadowMoveCardToDeck(String cardName) { MoveCardToZone(P2, GetShadowCard(cardName), Zone.DECK); }
    public void ShadowMoveCardToDeck(PhysicalCardImpl card) { MoveCardToZone(P2, card, Zone.DECK); }

    public void FreepsMoveCharToTable(String cardName) { MoveCardToZone(P1, GetFreepsCard(cardName), Zone.FREE_CHARACTERS); }
    public void FreepsMoveCharToTable(PhysicalCardImpl card)
    {
        MoveCardToZone(P1, card, Zone.FREE_CHARACTERS);
    }
    public void ShadowMoveCharToTable(String cardName) { MoveCardToZone(P2, GetShadowCard(cardName), Zone.SHADOW_CHARACTERS); }
    public void ShadowMoveCharToTable(PhysicalCardImpl card)
    {
        MoveCardToZone(P2, card, Zone.SHADOW_CHARACTERS);
    }

    public void FreepsMoveCardToZone(String cardID, Zone zone) { MoveCardToZone(P1, GetFreepsCard(cardID), zone); }
    public void FreepsMoveCardToZone(PhysicalCardImpl card, Zone zone) { MoveCardToZone(P1, card, zone); }
    public void ShadowMoveCardToZone(String cardID, Zone zone) { MoveCardToZone(P2, GetShadowCard(cardID), zone); }
    public void ShadowMoveCardToZone(PhysicalCardImpl card, Zone zone) { MoveCardToZone(P2, card, zone); }
    public void MoveCardToZone(String player, PhysicalCardImpl card, Zone zone) {
        if(card.getZone() != null)
        {
            _game.getGameState().removeCardsFromZone(player, new ArrayList<PhysicalCard>() {{ add(card); }});
        }
        _game.getGameState().addCardToZone(_game, card, zone);
    }



    public List<String> FreepsGetADParamAsList(String paramName) { return Arrays.asList((String[])GetAwaitingDecisionParam(P1, paramName)); }
    public List<String> ShadowGetADParamAsList(String paramName) { return Arrays.asList((String[])GetAwaitingDecisionParam(P2, paramName)); }
    public Object FreepsGetADParam(String paramName) { return GetAwaitingDecisionParam(P1, paramName); }
    public Object ShadowGetADParam(String paramName) { return GetAwaitingDecisionParam(P2, paramName); }
    public Object GetAwaitingDecisionParam(String playerID, String paramName) {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(playerID);
        return decision.getDecisionParameters().get(paramName);
    }

    public Map<String, Object> GetAwaitingDecisionParams(String playerID) {
        AwaitingDecision decision = _userFeedback.getAwaitingDecision(playerID);
        return decision.getDecisionParameters();
    }


    public void FreepsPlayCharFromHand(String cardName) throws DecisionResultInvalidException {
        PhysicalCardImpl card = GetFreepsCard(cardName);
        List<String> availableIds = FreepsGetADParamAsList("cardId");

        Integer index = availableIds.indexOf(String.valueOf(card.getCardId()));
        playerDecided(P1, index.toString());
    }

    public void FreepsAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetFreepsCard(cardName), count); }
    public void ShadowAddWoundsToChar(String cardName, int count) { AddWoundsToChar(GetShadowCard(cardName), count); }
    public void AddWoundsToChar(PhysicalCardImpl card, int count) {
        for(int i = 0; i < count; i++)
        {
            AddWoundToChar(card);
        }
    }
    public void AddWoundToChar(PhysicalCardImpl card) { _game.getGameState().addWound(card); }

    public int GetTwilight() { return _game.getGameState().getTwilightPool(); }
    public void SetTwilight(int amount) { _game.getGameState().setTwilight(amount); }

    public PhysicalCardImpl GetRingBearer()
    {
        return (PhysicalCardImpl)_game.getGameState().getRingBearer(P1);
    }

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
        Phase current = _game.getGameState().getCurrentPhase();

        if(_userFeedback.getAwaitingDecision(P1) != null) {
            playerDecided(P1, "");
        }

        if(_userFeedback.getAwaitingDecision(P2) != null) {
            playerDecided(P2, "");
        }
    }


    public void FreepsAssignToMinion(String name, String target) throws DecisionResultInvalidException { FreepsAssignToMinion(GetFreepsCard(name), GetShadowCard(target)); }
    public void FreepsAssignToMinion(PhysicalCardImpl comp, PhysicalCardImpl minion) throws DecisionResultInvalidException {
        playerDecided(P1, comp.getCardId() + " " + minion.getCardId());
    }

    public List<PhysicalCardImpl> FreepsGetAttachedCards(String name) { return GetAttachedCards(GetFreepsCard(name)); }
    public List<PhysicalCardImpl> GetAttachedCards(PhysicalCardImpl card)
    {
        return (List<PhysicalCardImpl>)(List<?>)_game.getGameState().getAttachedCards(card);
    }

    public void FreepsResolveSkirmish(String name) throws DecisionResultInvalidException { FreepsResolveSkirmish(GetFreepsCard(name)); }
    public void FreepsResolveSkirmish(PhysicalCardImpl comp) throws DecisionResultInvalidException {
        playerDecided(P1, String.valueOf(comp.getCardId()));
    }

    public int FreepsGetStrength(String name) { return GetStrength(GetFreepsCard(name)); }
    public int ShadowGetStrength(String name) { return GetStrength(GetShadowCard(name)); }
    public int GetStrength(PhysicalCardImpl card)
    {
        return _game.getModifiersQuerying().getStrength(_game, card);
    }

    public boolean HasKeyword(PhysicalCardImpl card, Keyword keyword)
    {
        return _game.getModifiersQuerying().hasKeyword(_game, card, keyword);
    }
}
