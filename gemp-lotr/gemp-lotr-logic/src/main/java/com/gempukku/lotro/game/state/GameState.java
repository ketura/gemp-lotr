package com.gempukku.lotro.game.state;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.logic.PlayerOrder;
import com.gempukku.lotro.logic.timing.GameStats;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class GameState {
    private PlayerOrder _playerOrder;

    private Map<String, List<PhysicalCardImpl>> _adventureDecks = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _decks = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _hands = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _discards = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _deadPiles = new HashMap<String, List<PhysicalCardImpl>>();
    private Map<String, List<PhysicalCardImpl>> _stacked = new HashMap<String, List<PhysicalCardImpl>>();

    private List<PhysicalCardImpl> _inPlay = new LinkedList<PhysicalCardImpl>();

    private Map<Integer, PhysicalCardImpl> _allCards = new HashMap<Integer, PhysicalCardImpl>();

    private String _currentPlayerId;
    private Phase _currentPhase = Phase.GAME_SETUP;
    private int _twilightPool;

    private int _moveCount;
    private boolean _fierceSkirmishes;
    private boolean _wearingRing;
    private boolean _consecutiveAction;

    private Map<String, Integer> _playerPosition = new HashMap<String, Integer>();
    private Map<PhysicalCard, Map<Token, Integer>> _cardTokens = new HashMap<PhysicalCard, Map<Token, Integer>>();

    private Map<String, PhysicalCard> _ringBearers = new HashMap<String, PhysicalCard>();

    private List<Assignment> _assignments = new LinkedList<Assignment>();
    private Skirmish _skirmish = null;

    private Map<String, GatheringParticipantCommunicationChannel> _recordingListeners = new HashMap<String, GatheringParticipantCommunicationChannel>();
    private Map<String, GameStateListener> _gameStateListeners = new HashMap<String, GameStateListener>();

    private int _nextCardId = 0;

    private int nextCardId() {
        return _nextCardId++;
    }

    public void init(PlayerOrder playerOrder, String firstPlayer, Map<String, List<String>> cards, LotroCardBlueprintLibrary library) {
        _playerOrder = playerOrder;
        _currentPlayerId = firstPlayer;

        for (Map.Entry<String, List<String>> stringListEntry : cards.entrySet()) {
            _adventureDecks.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _decks.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _hands.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _discards.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _deadPiles.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());
            _stacked.put(stringListEntry.getKey(), new LinkedList<PhysicalCardImpl>());

            addPlayerCards(stringListEntry.getKey(), stringListEntry.getValue(), library);
        }

        for (String playerId : playerOrder.getAllPlayers()) {
            GatheringParticipantCommunicationChannel listener = new GatheringParticipantCommunicationChannel(playerId);
            listener.setPlayerOrder(playerOrder.getAllPlayers());
            _recordingListeners.put(playerId, listener);
        }

        for (String playerId : _gameStateListeners.keySet())
            sendStateToPlayer(playerId);
    }

    private String _possibleChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private int _charsCount = _possibleChars.length();

    private String randomUid() {
        int length = 16;
        char[] chars = new char[length];
        Random rnd = new Random();
        for (int i = 0; i < length; i++)
            chars[i] = _possibleChars.charAt(rnd.nextInt(_charsCount));

        return new String(chars);
    }

    public void gameFinished() {
        File gameReplayFolder = new File("i:\\gemp-lotr\\replay");
        gameReplayFolder.mkdirs();
        for (Map.Entry<String, GatheringParticipantCommunicationChannel> playerRecordings : _recordingListeners.entrySet()) {
            String playerId = playerRecordings.getKey();
            File playerReplayFolder = new File(gameReplayFolder, playerId);
            playerReplayFolder.mkdir();
            File replayFile;
            do {
                replayFile = new File(playerReplayFolder, randomUid() + ".xml");
            } while (replayFile.exists());

            final List<GameEvent> gameEvents = playerRecordings.getValue().consumeGameEvents();

            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.newDocument();
                Element gameReplay = doc.createElement("gameReplay");
                EventSerializer serializer = new EventSerializer();
                for (GameEvent gameEvent : gameEvents) {
                    gameReplay.appendChild(serializer.serializeEvent(doc, gameEvent));
                }

                doc.appendChild(gameReplay);

                // Prepare the DOM document for writing
                Source source = new DOMSource(doc);

                // Prepare the output file
                Result result = new StreamResult(replayFile);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (Exception exp) {

            }
        }
    }

    private void addPlayerCards(String playerId, List<String> cards, LotroCardBlueprintLibrary library) {
        for (String blueprintId : cards) {
            LotroCardBlueprint card = library.getLotroCardBlueprint(blueprintId);

            int cardId = nextCardId();
            PhysicalCardImpl physicalCard = new PhysicalCardImpl(cardId, blueprintId, playerId, Zone.DECK, card);

            if (card.getCardType() == CardType.SITE)
                _adventureDecks.get(playerId).add(physicalCard);
            else
                _decks.get(playerId).add(physicalCard);

            _allCards.put(physicalCard.getCardId(), physicalCard);
        }
    }

    public boolean isConsecutiveAction() {
        return _consecutiveAction;
    }

    public void setConsecutiveAction(boolean consecutiveAction) {
        _consecutiveAction = consecutiveAction;
    }

    public void setWearingRing(boolean wearingRing) {
        _wearingRing = wearingRing;
    }

    public boolean isWearingRing() {
        return _wearingRing;
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
        Set<GameStateListener> listeners = new HashSet<GameStateListener>();
        GameStateListener result = _gameStateListeners.get(owner);
        if (result != null)
            listeners.add(result);

        listeners.add(_recordingListeners.get(owner));
        return listeners;
    }

    private Collection<GameStateListener> getAllGameStateListeners() {
        Set<GameStateListener> allListeners = new HashSet<GameStateListener>(_gameStateListeners.values());
        allListeners.addAll(_recordingListeners.values());
        return allListeners;
    }

    private void sendStateToPlayer(String playerId) {
        if (_playerOrder != null) {
            GameStateListener listener = _gameStateListeners.get(playerId);
            listener.setPlayerOrder(_playerOrder.getAllPlayers());
            if (_currentPlayerId != null)
                listener.setCurrentPlayerId(_currentPlayerId);
            if (_currentPhase != null)
                listener.setCurrentPhase(_currentPhase);
            listener.setTwilight(_twilightPool);
            for (Map.Entry<String, Integer> stringIntegerEntry : _playerPosition.entrySet())
                listener.setPlayerPosition(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());

            // First non-attached cards
            for (PhysicalCardImpl physicalCard : _inPlay) {
                if (physicalCard.getZone() != Zone.ATTACHED)
                    listener.cardCreated(physicalCard);
            }

            // Now the attached ones
            for (PhysicalCardImpl physicalCard : _inPlay) {
                if (physicalCard.getZone() == Zone.ATTACHED)
                    listener.cardCreated(physicalCard);
            }

            // Finally the stacked ones
            for (List<PhysicalCardImpl> physicalCards : _stacked.values())
                for (PhysicalCardImpl physicalCard : physicalCards)
                    listener.cardCreated(physicalCard);

            List<PhysicalCardImpl> hand = _hands.get(playerId);
            if (hand != null) {
                for (PhysicalCardImpl physicalCard : hand)
                    listener.cardCreated(physicalCard);
            }

            for (List<PhysicalCardImpl> physicalCards : _deadPiles.values())
                for (PhysicalCardImpl physicalCard : physicalCards)
                    listener.cardCreated(physicalCard);

            List<PhysicalCardImpl> discard = _discards.get(playerId);
            if (discard != null) {
                for (PhysicalCardImpl physicalCard : discard)
                    listener.cardCreated(physicalCard);
            }

            for (Assignment assignment : _assignments)
                listener.addAssignment(assignment.getFellowshipCharacter(), assignment.getShadowCharacters());

            if (_skirmish != null)
                listener.startSkirmish(_skirmish.getFellowshipCharacter(), _skirmish.getShadowCharacters());

            for (Map.Entry<PhysicalCard, Map<Token, Integer>> physicalCardMapEntry : _cardTokens.entrySet()) {
                PhysicalCard card = physicalCardMapEntry.getKey();
                for (Map.Entry<Token, Integer> tokenIntegerEntry : physicalCardMapEntry.getValue().entrySet()) {
                    Integer count = tokenIntegerEntry.getValue();
                    if (count != null && count > 0)
                        listener.addTokens(card, tokenIntegerEntry.getKey(), count);
                }
            }
        }
    }

    private boolean isZonePublic(Zone zone) {
        return zone == Zone.FREE_CHARACTERS || zone == Zone.SUPPORT || zone == Zone.SHADOW_CHARACTERS || zone == Zone.SUPPORT
                || zone == Zone.ADVENTURE_PATH || zone == Zone.ATTACHED || zone == Zone.STACKED || zone == Zone.DEAD;
    }

    private boolean isZonePrivate(Zone zone) {
        return zone == Zone.DISCARD || zone == Zone.HAND;
    }

    public void sendMessage(String message) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendMessage(message);
    }

    public void transferCard(PhysicalCard card, PhysicalCard transferTo) {
        if (card.getZone() != Zone.ATTACHED)
            ((PhysicalCardImpl) card).setZone(Zone.ATTACHED);

        ((PhysicalCardImpl) card).attachTo((PhysicalCardImpl) transferTo);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void takeControlOfCard(String playerId, PhysicalCard card, Zone zone) {
        ((PhysicalCardImpl) card).setCardController(playerId);
        ((PhysicalCardImpl) card).setZone(zone);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void loseControlOfCard(PhysicalCard card, Zone zone) {
        ((PhysicalCardImpl) card).setCardController(null);
        ((PhysicalCardImpl) card).setZone(zone);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void attachCard(LotroGame game, PhysicalCard card, PhysicalCard attachTo) {
        ((PhysicalCardImpl) card).attachTo((PhysicalCardImpl) attachTo);
        addCardToZone(game, card, Zone.ATTACHED);
    }

    public void cardAffectsCard(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardAffectedByCard(playerPerforming, card, affectedCards);
    }

    public void eventPlayed(PhysicalCard card) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.eventPlayed(card);
    }

    public void stackCard(LotroGame game, PhysicalCard card, PhysicalCard stackOn) {
        ((PhysicalCardImpl) card).stackOn((PhysicalCardImpl) stackOn);
        addCardToZone(game, card, Zone.STACKED);
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
            return _inPlay;
    }

    public void removeFromSkirmish(PhysicalCard card) {
        if (_skirmish.getFellowshipCharacter() == card) {
            _skirmish.setFellowshipCharacter(null);
            for (GameStateListener listener : getAllGameStateListeners())
                listener.removeFromSkirmish(card);
        }
        if (_skirmish.getShadowCharacters().remove(card)) {
            for (GameStateListener listener : getAllGameStateListeners())
                listener.removeFromSkirmish(card);
        }
    }

    public void removeCardsFromZone(Collection<PhysicalCard> cards) {
        Map<GameStateListener, Set<PhysicalCard>> listenerCards = new HashMap<GameStateListener, Set<PhysicalCard>>();

        for (PhysicalCard card : cards) {
            Zone zone = card.getZone();

            if (zone.isInPlay())
                stopAffecting(card);
            else if (zone == Zone.STACKED)
                stopAffectingStacked(card);

            List<PhysicalCardImpl> zoneCards = getZoneCards(card.getOwner(), card.getBlueprint().getCardType(), zone);
            boolean b = zoneCards.remove(card);
            if (!b)
                throw new RuntimeException("Card was not found in the expected zone");

            if (zone == Zone.ATTACHED)
                ((PhysicalCardImpl) card).attachTo(null);

            if (zone == Zone.STACKED)
                ((PhysicalCardImpl) card).stackOn(null);

            for (Assignment assignment : new LinkedList<Assignment>(_assignments)) {
                if (assignment.getFellowshipCharacter() == card)
                    removeAssignment(assignment);
                if (assignment.getShadowCharacters().remove(card))
                    removeAssignment(assignment);
            }

            if (_skirmish != null)
                removeFromSkirmish(card);

            removeAllTokens(card);

            if (isZonePublic(zone))
                for (GameStateListener listener : getAllGameStateListeners()) {
                    getValue(listenerCards, listener).add(card);
                }
            else if (isZonePrivate(zone))
                for (GameStateListener listener : getPrivateGameStateListeners(card)) {
                    getValue(listenerCards, listener).add(card);
                }

            ((PhysicalCardImpl) card).setZone(null);
        }

        for (Map.Entry<GameStateListener, Set<PhysicalCard>> gameStateListenerSetEntry : listenerCards.entrySet())
            gameStateListenerSetEntry.getKey().cardsRemoved(gameStateListenerSetEntry.getValue());
    }

    private Set<PhysicalCard> getValue(Map<GameStateListener, Set<PhysicalCard>> map, GameStateListener listener) {
        Set<PhysicalCard> result = map.get(listener);
        if (result == null) {
            result = new HashSet<PhysicalCard>();
            map.put(listener, result);
        }
        return result;
    }

    public void addCardToZone(LotroGame game, PhysicalCard card, Zone zone) {
        addCardToZone(game, card, zone, true);
    }

    private void addCardToZone(LotroGame game, PhysicalCard card, Zone zone, boolean end) {
        List<PhysicalCardImpl> zoneCards = getZoneCards(card.getOwner(), card.getBlueprint().getCardType(), zone);
        if (end)
            zoneCards.add((PhysicalCardImpl) card);
        else
            zoneCards.add(0, (PhysicalCardImpl) card);

        if (card.getZone() != null)
            throw new RuntimeException("Card was in " + card.getZone() + " when tried to add to zone: " + zone);

        ((PhysicalCardImpl) card).setZone(zone);

        if (zone == Zone.ADVENTURE_PATH) {
            for (GameStateListener listener : getAllGameStateListeners())
                listener.setSite(card);
        } else {
            if (isZonePublic(zone))
                for (GameStateListener listener : getAllGameStateListeners())
                    listener.cardCreated(card);
            else if (isZonePrivate(zone))
                for (GameStateListener listener : getPrivateGameStateListeners(card))
                    listener.cardCreated(card);
        }

        if (zone.isInPlay()) {
            if (zone != Zone.ADVENTURE_PATH || _currentPhase != Phase.GAME_SETUP)
                startAffecting(game, card);
        } else if (zone == Zone.STACKED)
            startAffectingStacked(game, card);
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

    public void shuffleCardsIntoDeck(Collection<? extends PhysicalCard> cards, String playerId) {
        List<PhysicalCardImpl> zoneCards = _decks.get(playerId);

        for (PhysicalCard card : cards) {
            zoneCards.add((PhysicalCardImpl) card);

            ((PhysicalCardImpl) card).setZone(Zone.DECK);
        }

        shuffleDeck(playerId);
    }

    public void putCardOnBottomOfDeck(PhysicalCard card) {
        addCardToZone(null, card, Zone.DECK, true);
    }

    public void putCardOnTopOfDeck(PhysicalCard card) {
        addCardToZone(null, card, Zone.DECK, false);
    }

    public boolean iterateActiveCards(PhysicalCardVisitor physicalCardVisitor) {
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard))
                if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                    return true;
        }

        return false;
    }

    public boolean iterateActiveTextCards(PhysicalCardVisitor physicalCardVisitor) {
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (physicalCard.getBlueprint().getCardType() != CardType.SITE || getCurrentSite() == physicalCard)
                if (isCardInPlayActive(physicalCard))
                    if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                        return true;
        }

        return false;
    }

    public boolean iterateActiveTextCards(String player, PhysicalCardVisitor physicalCardVisitor) {
        physicalCardVisitor.visitPhysicalCard(getCurrentSite());

        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (physicalCard.getBlueprint().getCardType() != CardType.SITE && physicalCard.getOwner().equals(player) && isCardInPlayActive(physicalCard))
                if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                    return true;
        }
        return false;
    }

    public boolean iterateActivableCards(String player, PhysicalCardVisitor physicalCardVisitor) {
        if (physicalCardVisitor.visitPhysicalCard(getCurrentSite()))
            return true;
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (physicalCard.getOwner().equals(player)
                    && physicalCard.getBlueprint().getSide() != Side.SITE && isCardInPlayActive(physicalCard))
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

    public boolean iterateStackedActivableCards(String player, PhysicalCardVisitor physicalCardVisitor) {
        for (PhysicalCardImpl physicalCard : _stacked.get(player)) {
            if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                return true;
        }
        return false;
    }

    public PhysicalCard findCardById(int cardId) {
        return _allCards.get(cardId);
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

    public void movePlayerToNextSite(LotroGame game) {
        final String currentPlayerId = getCurrentPlayerId();
        final int oldPlayerPosition = getPlayerPosition(currentPlayerId);
        stopAffecting(getCurrentSite());
        setPlayerPosition(currentPlayerId, oldPlayerPosition + 1);
        increaseMoveCount();
        startAffecting(game, getCurrentSite());
    }

    public int getPlayerPosition(String playerId) {
        return _playerPosition.get(playerId);
    }

    public int getTokenCount(PhysicalCard physicalCard, Token token) {
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
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (physicalCard.getAttachedTo() != null && physicalCard.getAttachedTo() == card)
                result.add(physicalCard);
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

    public void addBurdens(int burdens) {
        addTokens(_ringBearers.get(getCurrentPlayerId()), Token.BURDEN, Math.max(0, burdens));
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
        if (currentCount != null) {
            if (currentCount < count)
                count = currentCount;

            tokens.put(token, currentCount - count);

            for (GameStateListener listener : getAllGameStateListeners())
                listener.removeTokens(card, token, count);
        }
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
        return side == Side.SITE
                || (
                card.getAttachedTo() == null &&
                        ((card.getOwner().equals(_currentPlayerId) && (side == Side.FREE_PEOPLE))
                                || (!card.getOwner().equals(_currentPlayerId) && (side == Side.SHADOW))))
                || (
                card.getAttachedTo() != null && isCardInPlayActive(card.getAttachedTo()));
    }

    public void startAffectingCardsForCurrentPlayer(LotroGame game) {
        // Active non-sites are affecting
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard) && physicalCard.getBlueprint().getSide() != Side.SITE)
                startAffecting(game, physicalCard);
        }

        // Current site is affecting
        startAffecting(game, getCurrentSite());

        // Stacked cards on active cards are stack-affecting
        for (List<PhysicalCardImpl> stackedCards : _stacked.values())
            for (PhysicalCardImpl stackedCard : stackedCards)
                if (isCardInPlayActive(stackedCard.getStackedOn()))
                    startAffectingStacked(game, stackedCard);
    }

    public void stopAffectingCardsForCurrentPlayer() {
        for (PhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard) && physicalCard.getBlueprint().getSide() != Side.SITE)
                stopAffecting(physicalCard);
        }

        stopAffecting(getCurrentSite());

        for (List<PhysicalCardImpl> stackedCards : _stacked.values())
            for (PhysicalCardImpl stackedCard : stackedCards)
                if (isCardInPlayActive(stackedCard.getStackedOn()))
                    stopAffectingStacked(stackedCard);
    }

    private void startAffecting(LotroGame game, PhysicalCard card) {
        ((PhysicalCardImpl) card).startAffectingGame(game);
    }

    private void startAffectingStacked(LotroGame game, PhysicalCard card) {
        ((PhysicalCardImpl) card).startAffectingGameStacked(game);
    }

    private void stopAffecting(PhysicalCard card) {
        card.removeData();
        ((PhysicalCardImpl) card).stopAffectingGame();
    }

    private void stopAffectingStacked(PhysicalCard card) {
        ((PhysicalCardImpl) card).stopAffectingGameStacked();
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
        for (PhysicalCardImpl physicalCard : _inPlay) {
            LotroCardBlueprint blueprint = physicalCard.getBlueprint();
            if (blueprint.getCardType() == CardType.SITE && blueprint.getSiteNumber() == siteNumber)
                return physicalCard;
        }
        return null;
    }

    public PhysicalCard getCurrentSite() {
        return getSite(getCurrentSiteNumber());
    }

    public Block getCurrentSiteBlock() {
        return getCurrentSite().getBlueprint().getSiteBlock();
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
        setTwilight(_twilightPool - Math.min(Math.max(0, twilight), _twilightPool));
    }

    public void assignToSkirmishes(PhysicalCard fp, List<PhysicalCard> minions) {
        Assignment assignment = findAssignment(fp);
        if (assignment != null)
            assignment.getShadowCharacters().addAll(minions);
        else
            _assignments.add(new Assignment(fp, new LinkedList<PhysicalCard>(minions)));

        for (GameStateListener listener : getAllGameStateListeners())
            listener.addAssignment(fp, minions);
    }

    public void removeAssignment(Assignment assignment) {
        _assignments.remove(assignment);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.removeAssignment(assignment.getFellowshipCharacter());
    }

    public List<Assignment> getAssignments() {
        return _assignments;
    }

    private Assignment findAssignment(PhysicalCard fp) {
        for (Assignment assignment : _assignments)
            if (assignment.getFellowshipCharacter() == fp)
                return assignment;
        return null;
    }

    public void startSkirmish(PhysicalCard fp) {
        Assignment assignment = findAssignment(fp);
        removeAssignment(assignment);
        _skirmish = new Skirmish(assignment.getFellowshipCharacter(), assignment.getShadowCharacters());
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

    public PhysicalCard removeTopDeckCard(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            final PhysicalCard topDeckCard = deck.get(0);
            removeCardsFromZone(Collections.singleton(topDeckCard));
            return topDeckCard;
        } else {
            return null;
        }
    }

    public PhysicalCard removeBottomDeckCard(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            final PhysicalCard topDeckCard = deck.get(deck.size() - 1);
            removeCardsFromZone(Collections.singleton(topDeckCard));
            return topDeckCard;
        } else {
            return null;
        }
    }

    public void playerDrawsCard(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            PhysicalCard card = deck.get(0);
            removeCardsFromZone(Collections.singleton(card));
            addCardToZone(null, card, Zone.HAND);
        }
    }

    public void shuffleDeck(String player) {
        List<PhysicalCardImpl> deck = _decks.get(player);
        Collections.shuffle(deck);
    }

    public void sendGameStats(GameStats gameStats) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendGameStats(gameStats);
    }
}
