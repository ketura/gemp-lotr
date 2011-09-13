package com.gempukku.lotro.game.state;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.logic.PlayerOrder;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;

import java.util.*;

public class GameState {
    private PlayerOrder _playerOrder;

    private Map<String, List<PhysicalCardImpl>> _adventureDecks = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _decks = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _hands = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _discards = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _deadPiles = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _stacked = new HashMap<String, List<PhysicalCardImpl>>();

    private Map<String, List<PhysicalCardImpl>> _inPlay = new HashMap<String, List<PhysicalCardImpl>>();

    private String _currentPlayerId;
    private Phase _currentPhase = Phase.GAME_SETUP;
    private int _twilightPool;

    private int _moveCount;
    private boolean _fierceSkirmishes;
    private boolean _wearingRing;
    private boolean _cancelRingText;

    private Map<String, Integer> _playerPosition = new HashMap<String, Integer>();
    private Map<PhysicalCard, Map<Token, Integer>> _cardTokens = new HashMap<PhysicalCard, Map<Token, Integer>>();

    private Map<String, PhysicalCard> _ringBearers = new HashMap<String, PhysicalCard>();

    private List<Skirmish> _assignments = new LinkedList<Skirmish>();
    private Skirmish _skirmish = null;

    private Map<String, GameStateListener> _gameStateListeners = new HashMap<String, GameStateListener>();

    private String _winnerPlayerId;
    private String _winReason;
    private Map<String, String> _losers = new HashMap<String, String>();

    private int _nextCardId = 0;

    private int nextCardId() {
        return _nextCardId++;
    }

    public GameState(PlayerOrder playerOrder, String firstPlayer, Map<String, List<String>> cards, LotroCardBlueprintLibrary library) {
        _playerOrder = playerOrder;
        _currentPlayerId = firstPlayer;
        for (Map.Entry<String, List<String>> stringListEntry : cards.entrySet()) {
            _adventureDecks.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _decks.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _hands.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _discards.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _deadPiles.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _inPlay.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _stacked.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());

            addPlayerCards(stringListEntry.getKey(), stringListEntry.getValue(), library);
        }
    }

    public void setWinnerPlayerId(String winnerPlayerId, String reason) {
        _winnerPlayerId = winnerPlayerId;
        _winReason = reason;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendMessage(winnerPlayerId + " is the winner due to: " + reason);
    }

    public String setLoserPlayerId(String loserPlayerId, String reason) {
        _losers.put(loserPlayerId, reason);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendMessage(loserPlayerId + " lost due to: " + reason);

        if (_losers.size() + 1 == _playerOrder.getAllPlayers().size()) {
            List<String> allPlayers = new LinkedList<String>(_playerOrder.getAllPlayers());
            allPlayers.removeAll(_losers.keySet());
            setWinnerPlayerId(allPlayers.get(0), "Last remaining player in game");
            return allPlayers.get(0);
        }
        return null;
    }

    public String getWinnerPlayerId() {
        return _winnerPlayerId;
    }

    private void addPlayerCards(String playerId, List<String> cards, LotroCardBlueprintLibrary library) {
        for (String blueprintId : cards) {
            LotroCardBlueprint card = library.getLotroCardBlueprint(blueprintId);
            if (card.getCardType() == CardType.SITE)
                _adventureDecks.get(playerId).add(new PhysicalCardImpl(nextCardId(), blueprintId, playerId, Zone.DECK, card));
            else
                _decks.get(playerId).add(new PhysicalCardImpl(nextCardId(), blueprintId, playerId, Zone.DECK, card));
        }
    }

    public void setWearingRing(boolean wearingRing) {
        _wearingRing = wearingRing;
    }

    public boolean isWearingRing() {
        return _wearingRing;
    }

    public void setCancelRingText(boolean cancelRingText) {
        _cancelRingText = cancelRingText;
    }

    public boolean isCancelRingText() {
        return _cancelRingText;
    }

    public PlayerOrder getPlayerOrder() {
        return _playerOrder;
    }

    public void addGameStateListener(String playerId, GameStateListener gameStateListener) {
        _gameStateListeners.put(playerId, gameStateListener);
        sendStateToPlayer(playerId);
    }

    public void removeGameStateListener(String playerId, GameStateListener gameStateListener) {
        _gameStateListeners.remove(playerId);
    }

    private Collection<GameStateListener> getPrivateGameStateListeners(PhysicalCard physicalCard) {
        String owner = physicalCard.getOwner();
        GameStateListener result = _gameStateListeners.get(owner);
        if (result != null)
            return Collections.singletonList(result);
        else
            return Collections.emptyList();
    }

    private Collection<GameStateListener> getAllGameStateListeners() {
        return _gameStateListeners.values();
    }

    private void sendStateToPlayer(String playerId) {
        GameStateListener listener = _gameStateListeners.get(playerId);
        listener.setPlayerOrder(_playerOrder.getAllPlayers());
        if (_currentPlayerId != null)
            listener.setCurrentPlayerId(_currentPlayerId);
        if (_currentPhase != null)
            listener.setCurrentPhase(_currentPhase);
        listener.setTwilight(_twilightPool);
        for (Map.Entry<String, Integer> stringIntegerEntry : _playerPosition.entrySet())
            listener.setPlayerPosition(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());

        for (List<PhysicalCardImpl> physicalCards : _inPlay.values())
            for (PhysicalCardImpl physicalCard : physicalCards)
                listener.cardCreated(physicalCard);

        List<PhysicalCardImpl> hand = _hands.get(playerId);
        if (hand != null) {
            for (PhysicalCardImpl physicalCard : hand)
                listener.cardCreated(physicalCard);
        }

        for (Skirmish assignment : _assignments)
            listener.addAssignment(assignment.getFellowshipCharacter(), assignment.getShadowCharacters());

        if (_skirmish != null)
            listener.startSkirmish(_skirmish.getFellowshipCharacter(), _skirmish.getShadowCharacters());

        for (Map.Entry<PhysicalCard, Map<Token, Integer>> physicalCardMapEntry : _cardTokens.entrySet()) {
            PhysicalCard card = physicalCardMapEntry.getKey();
            for (Map.Entry<Token, Integer> tokenIntegerEntry : physicalCardMapEntry.getValue().entrySet())
                listener.addTokens(card, tokenIntegerEntry.getKey(), tokenIntegerEntry.getValue());
        }

        for (Map.Entry<String, String> playerLoseReason : _losers.entrySet())
            listener.sendMessage(playerLoseReason.getKey() + " lost due to: " + playerLoseReason.getValue());

        if (_winnerPlayerId != null)
            listener.sendMessage(_winnerPlayerId + " is the winner due to: " + _winReason);
    }

    private boolean isZonePublic(Zone zone) {
        return zone == Zone.FREE_CHARACTERS || zone == Zone.FREE_SUPPORT || zone == Zone.SHADOW_CHARACTERS || zone == Zone.SHADOW_SUPPORT
                || zone == Zone.ADVENTURE_PATH || zone == Zone.ATTACHED || zone == Zone.STACKED || zone == Zone.DEAD;
    }

    private boolean isZonePrivate(Zone zone) {
        return zone == Zone.DISCARD || zone == Zone.HAND;
    }

    public void transferCard(PhysicalCard card, PhysicalCard transferTo) {
        if (card.getZone() != Zone.ATTACHED) {
            removeCardFromZone(card);
            addCardToZone(card, Zone.ATTACHED);
        }
        ((PhysicalCardImpl) card).attachTo((PhysicalCardImpl) transferTo);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void attachCard(PhysicalCard card, PhysicalCard attachTo) {
        ((PhysicalCardImpl) card).attachTo((PhysicalCardImpl) attachTo);
        addCardToZone(card, Zone.ATTACHED);
    }

    public void stackCard(PhysicalCard card, PhysicalCard stackOn) {
        ((PhysicalCardImpl) card).stackOn((PhysicalCardImpl) stackOn);
        addCardToZone(card, Zone.STACKED);
    }

    public void setRingBearer(PhysicalCard card) {
        _ringBearers.put(card.getOwner(), card);
    }

    public PhysicalCard getRingBearer(String playerId) {
        return _ringBearers.get(playerId);
    }

    private List<PhysicalCardImpl> getZoneCards(String playerId, CardType type, Zone zone) {
        if (zone == Zone.DECK && type != CardType.SITE)
            return _decks.get(playerId);
        else if (zone == Zone.DECK)
            return _adventureDecks.get(playerId);
        else if (zone == Zone.DISCARD)
            return _discards.get(playerId);
        else if (zone == Zone.DEAD)
            return _deadPiles.get(playerId);
        else if (zone == Zone.HAND)
            return _hands.get(playerId);
        else if (zone == Zone.STACKED)
            return _stacked.get(playerId);
        else
            return _inPlay.get(playerId);
    }

    public void removeCardFromZone(PhysicalCard card) {
        Zone zone = card.getZone();

        boolean b = getZoneCards(card.getOwner(), card.getBlueprint().getCardType(), zone).remove(card);
        if (!b)
            throw new RuntimeException("Card was not found in the expected zone");

        if (zone == Zone.ATTACHED)
            ((PhysicalCardImpl) card).attachTo(null);

        if (zone == Zone.STACKED)
            ((PhysicalCardImpl) card).stackOn(null);

        for (Skirmish assignment : new LinkedList<Skirmish>(_assignments)) {
            if (assignment.getFellowshipCharacter() == card)
                removeAssignment(assignment);
            if (assignment.getShadowCharacters().remove(card))
                removeAssignment(assignment);
        }

        if (_skirmish != null) {
            if (_skirmish.getFellowshipCharacter() == card)
                _skirmish.setFellowshipCharacter(card);
            _skirmish.getShadowCharacters().remove(card);
        }

        removeAllTokens(card);

        if (isZonePublic(zone))
            for (GameStateListener listener : getAllGameStateListeners())
                listener.cardRemoved(card);
        else if (isZonePrivate(zone))
            for (GameStateListener listener : getPrivateGameStateListeners(card))
                listener.cardRemoved(card);
    }

    public void addCardToZone(PhysicalCard card, Zone zone) {
        getZoneCards(card.getOwner(), card.getBlueprint().getCardType(), zone).add((PhysicalCardImpl) card);
        ((PhysicalCardImpl) card).setZone(zone);
        if (isZonePublic(zone))
            for (GameStateListener listener : getAllGameStateListeners())
                listener.cardCreated(card);
        else if (isZonePrivate(zone))
            for (GameStateListener listener : getPrivateGameStateListeners(card))
                listener.cardCreated(card);
    }

    private void removeAllTokens(PhysicalCard card) {
        Map<Token, Integer> map = _cardTokens.get(card);
        if (map != null) {
            for (Map.Entry<Token, Integer> tokenIntegerEntry : map.entrySet())
                if (tokenIntegerEntry.getValue() > 0)
                    for (GameStateListener listener : getAllGameStateListeners())
                        listener.removeTokens(card, tokenIntegerEntry.getKey(), tokenIntegerEntry.getValue());

            map.clear();
        }
    }

    public void putCardOnBottomOfDeck(PhysicalCard card) {
        addCardToZone(card, Zone.DECK);
    }

    public void putCardOnTopOfDeck(PhysicalCard card) {
        _decks.get(card.getOwner()).add(0, (PhysicalCardImpl) card);
    }

    public boolean iterateActiveCards(PhysicalCardVisitor physicalCardVisitor) {
        for (List<PhysicalCardImpl> physicalCards : _inPlay.values()) {
            for (PhysicalCardImpl physicalCard : physicalCards) {
                if (isCardInPlayActive(physicalCard))
                    if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                        return true;
            }
        }

        return false;
    }

    public boolean iterateActivableCards(String player, PhysicalCardVisitor physicalCardVisitor) {
        if (physicalCardVisitor.visitPhysicalCard(getCurrentSite()))
            return true;
        for (PhysicalCardImpl physicalCard : _inPlay.get(player)) {
            if (physicalCard.getBlueprint().getSide() != Side.SITE && isCardInPlayActive(physicalCard))
                if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                    return true;
        }
        for (PhysicalCardImpl physicalCard : _hands.get(player)) {
            if (_currentPlayerId.equals(player)) {
                if (physicalCard.getBlueprint().getSide() == Side.FREE_PEOPLE)
                    if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                        return true;
            } else {
                if (physicalCard.getBlueprint().getSide() == Side.SHADOW)
                    if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                        return true;
            }
        }
        return false;
    }

    public boolean iterateActiveCards(String player, PhysicalCardVisitor physicalCardVisitor) {
        for (int i = 1; i <= 9; i++) {
            PhysicalCard site = getSite(i);
            if (site != null)
                physicalCardVisitor.visitPhysicalCard(site);
        }
        for (PhysicalCardImpl physicalCard : _inPlay.get(player)) {
            if (isCardInPlayActive(physicalCard))
                if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                    return true;
        }
        return false;
    }

    private boolean iterateCards(List<PhysicalCardImpl> cards, PhysicalCardVisitor physicalCardVisitor) {
        for (PhysicalCard card : cards)
            if (physicalCardVisitor.visitPhysicalCard(card))
                return true;
        return false;
    }

    private boolean iterateCards(Map<String, List<PhysicalCardImpl>> cards, PhysicalCardVisitor physicalCardVisitor) {
        for (List<PhysicalCardImpl> physicalCardList : cards.values())
            if (iterateCards(physicalCardList, physicalCardVisitor))
                return true;
        return false;
    }

    public boolean iterateAllCards(PhysicalCardVisitor physicalCardVisitor) {
        if (iterateCards(_adventureDecks, physicalCardVisitor))
            return true;
        if (iterateCards(_deadPiles, physicalCardVisitor))
            return true;
        if (iterateCards(_decks, physicalCardVisitor))
            return true;
        if (iterateCards(_discards, physicalCardVisitor))
            return true;
        if (iterateCards(_hands, physicalCardVisitor))
            return true;
        if (iterateCards(_inPlay, physicalCardVisitor))
            return true;
        return false;
    }

    private PhysicalCard findCardIn(int cardId, Map<String, List<PhysicalCardImpl>> cards) {
        for (List<PhysicalCardImpl> physicalCards : cards.values()) {
            for (PhysicalCardImpl physicalCard : physicalCards) {
                if (physicalCard.getCardId() == cardId)
                    return physicalCard;
            }
        }
        return null;
    }

    public PhysicalCard findCardById(int cardId) {
        PhysicalCard card = findCardIn(cardId, _adventureDecks);
        if (card != null)
            return card;
        card = findCardIn(cardId, _decks);
        if (card != null)
            return card;
        card = findCardIn(cardId, _hands);
        if (card != null)
            return card;
        card = findCardIn(cardId, _discards);
        if (card != null)
            return card;
        card = findCardIn(cardId, _deadPiles);
        if (card != null)
            return card;
        card = findCardIn(cardId, _inPlay);
        return card;
    }

    public List<? extends PhysicalCard> getHand(String playerId) {
        return Collections.unmodifiableList(_hands.get(playerId));
    }

    public List<? extends PhysicalCard> getDeck(String playerId) {
        return Collections.unmodifiableList(_decks.get(playerId));
    }

    public List<? extends PhysicalCard> getDiscard(String playerId) {
        return Collections.unmodifiableList(_discards.get(playerId));
    }

    public List<? extends PhysicalCard> getDeadPile(String playerId) {
        return Collections.unmodifiableList(_deadPiles.get(playerId));
    }

    public List<? extends PhysicalCard> getAdventureDeck(String playerId) {
        return Collections.unmodifiableList(_adventureDecks.get(playerId));
    }

    public String getCurrentPlayerId() {
        return _currentPlayerId;
    }

    public int getCurrentSiteNumber() {
        return _playerPosition.get(_currentPlayerId);
    }

    public void setPlayerPosition(String playerId, int i) {
        _playerPosition.put(playerId, i);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.setPlayerPosition(playerId, i);
    }

    private int getTokenCount(PhysicalCard physicalCard, Token token) {
        Map<Token, Integer> tokens = _cardTokens.get(physicalCard);
        if (tokens == null)
            return 0;
        Integer count = tokens.get(token);
        if (count == null)
            return 0;
        return count;
    }

    public List<PhysicalCard> getAttachedCards(PhysicalCard card) {
        List<PhysicalCard> result = new LinkedList<PhysicalCard>();
        for (List<PhysicalCardImpl> physicalCardList : _inPlay.values()) {
            for (PhysicalCardImpl physicalCard : physicalCardList) {
                if (physicalCard.getAttachedTo() != null && physicalCard.getAttachedTo() == card)
                    result.add(physicalCard);
            }
        }
        return result;
    }

    public List<PhysicalCard> getStackedCards(PhysicalCard card) {
        List<PhysicalCard> result = new LinkedList<PhysicalCard>();
        for (List<PhysicalCardImpl> physicalCardList : _stacked.values()) {
            for (PhysicalCardImpl physicalCard : physicalCardList) {
                if (physicalCard.getStackedOn() == card)
                    result.add(physicalCard);
            }
        }
        return result;
    }

    public int getWounds(PhysicalCard physicalCard) {
        return getTokenCount(physicalCard, Token.WOUND);
    }

    public void addBurdens(String playerId, int burdens) {
        addTokens(_ringBearers.get(playerId), Token.BURDEN, Math.max(0, burdens));
    }

    public int getBurdens() {
        return getTokenCount(_ringBearers.get(getCurrentPlayerId()), Token.BURDEN);
    }

    public void removeBurdens(int burdens) {
        removeTokens(_ringBearers.get(getCurrentPlayerId()), Token.BURDEN, Math.max(0, burdens));
    }

    public void addWound(PhysicalCard card) {
        addTokens(card, Token.WOUND, 1);
    }

    public void removeWound(PhysicalCard card) {
        removeTokens(card, Token.WOUND, 1);
    }

    public void addTokens(PhysicalCard card, Token token, int count) {
        Map<Token, Integer> tokens = _cardTokens.get(card);
        if (tokens == null) {
            tokens = new HashMap<Token, Integer>();
            _cardTokens.put(card, tokens);
        }
        Integer currentCount = tokens.get(token);
        if (currentCount == null)
            tokens.put(token, count);
        else
            tokens.put(token, currentCount + count);

        for (GameStateListener listener : getAllGameStateListeners())
            listener.addTokens(card, token, count);
    }

    public void removeTokens(PhysicalCard card, Token token, int count) {
        Map<Token, Integer> tokens = _cardTokens.get(card);
        if (tokens == null) {
            tokens = new HashMap<Token, Integer>();
            _cardTokens.put(card, tokens);
        }
        Integer currentCount = tokens.get(token);
        if (currentCount == null)
            tokens.put(token, count);
        else {
            if (currentCount <= count)
                tokens.remove(token);
            else
                tokens.put(token, currentCount - count);
        }

        for (GameStateListener listener : getAllGameStateListeners())
            listener.removeTokens(card, token, count);
    }

    public void setTwilight(int twilight) {
        _twilightPool = twilight;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.setTwilight(_twilightPool);
    }

    public int getTwilightPool() {
        return _twilightPool;
    }

    public void startPlayerTurn(String playerId) {
        _currentPlayerId = playerId;
        setTwilight(0);
        _moveCount = 0;
        _fierceSkirmishes = false;

        for (GameStateListener listener : getAllGameStateListeners())
            listener.setCurrentPlayerId(_currentPlayerId);
    }

    public void setFierceSkirmishes(boolean value) {
        _fierceSkirmishes = value;
    }

    public boolean isFierceSkirmishes() {
        return _fierceSkirmishes;
    }

    private boolean isCardInPlayActive(PhysicalCard card) {
        Side side = card.getBlueprint().getSide();
        // Either it's not attached or attached to active card
        // AND is a site or fp/ring of current player or shadow of any other player
        return (
                (card.getAttachedTo() == null || isCardInPlayActive(card.getAttachedTo()))
                        && (side == Side.SITE
                        || (card.getOwner().equals(_currentPlayerId) && (side == Side.FREE_PEOPLE || side == Side.RING))
                        || (!card.getOwner().equals(_currentPlayerId) && (side == Side.SHADOW))));
    }

    public void startAffectingCardsForCurrentPlayer(ModifiersEnvironment modifiersEnvironment) {
        for (List<PhysicalCardImpl> physicalCards : _inPlay.values()) {
            for (PhysicalCardImpl physicalCard : physicalCards) {
                if (isCardInPlayActive(physicalCard))
                    startAffecting(physicalCard, modifiersEnvironment);
            }
        }
    }

    public void stopAffectingCardsForCurrentPlayer() {
        for (List<PhysicalCardImpl> physicalCards : _inPlay.values()) {
            for (PhysicalCardImpl physicalCard : physicalCards) {
                if (isCardInPlayActive(physicalCard))
                    stopAffecting(physicalCard);
            }
        }
    }

    public void startAffecting(PhysicalCard card, ModifiersEnvironment modifiersEnvironment) {
        ((PhysicalCardImpl) card).startAffectingGame(modifiersEnvironment);
    }

    public void stopAffecting(PhysicalCard card) {
        card.removeData();
        ((PhysicalCardImpl) card).stopAffectingGame();
    }

    public void setCurrentPhase(Phase phase) {
        _currentPhase = phase;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.setCurrentPhase(_currentPhase);
    }

    public Phase getCurrentPhase() {
        return _currentPhase;
    }

    public PhysicalCard getSite(int siteNumber) {
        for (List<PhysicalCardImpl> physicalCards : _inPlay.values()) {
            for (PhysicalCardImpl physicalCard : physicalCards) {
                LotroCardBlueprint blueprint = physicalCard.getBlueprint();
                if (blueprint.getCardType() == CardType.SITE && blueprint.getSiteNumber() == siteNumber)
                    return physicalCard;
            }
        }
        return null;
    }

    public PhysicalCard getCurrentSite() {
        return getSite(getCurrentSiteNumber());
    }

    public void increaseMoveCount() {
        _moveCount++;
    }

    public int getMoveCount() {
        return _moveCount;
    }

    public void addTwilight(int twilight) {
        setTwilight(_twilightPool + Math.max(0, twilight));
    }

    public void removeTwilight(int twilight) {
        setTwilight(_twilightPool - Math.max(0, twilight));
    }

    public void assignToSkirmishes(PhysicalCard fp, List<PhysicalCard> minions) {
        Skirmish assignment = findAssignment(fp);
        if (assignment != null)
            assignment.getShadowCharacters().addAll(minions);
        else
            _assignments.add(new Skirmish(fp, new LinkedList<PhysicalCard>(minions)));

        for (GameStateListener listener : getAllGameStateListeners())
            listener.addAssignment(fp, minions);
    }

    public void removeAssignment(Skirmish skirmish) {
        _assignments.remove(skirmish);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.removeAssignment(skirmish.getFellowshipCharacter());
    }

    public List<Skirmish> getAssignments() {
        return _assignments;
    }

    private Skirmish findAssignment(PhysicalCard fp) {
        for (Skirmish assignment : _assignments)
            if (assignment.getFellowshipCharacter() == fp)
                return assignment;
        return null;
    }

    public void startSkirmish(PhysicalCard fp) {
        Skirmish skirmish = findAssignment(fp);
        removeAssignment(skirmish);
        _skirmish = skirmish;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.startSkirmish(_skirmish.getFellowshipCharacter(), _skirmish.getShadowCharacters());
    }

    public Skirmish getSkirmish() {
        return _skirmish;
    }

    public void finishSkirmish() {
        _skirmish = null;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.finishSkirmish();
    }

    public void playerShufflesDeck(String player) {
        Collections.shuffle(_decks.get(player));
    }

    public PhysicalCard removeTopDeckCard(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            return deck.remove(0);
        } else {
            return null;
        }
    }

    public void playerDrawsCard(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            PhysicalCard card = deck.get(0);
            removeCardFromZone(card);
            addCardToZone(card, Zone.HAND);
        }
    }

    public void shuffleDeck(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        Collections.shuffle(deck);
    }
}
