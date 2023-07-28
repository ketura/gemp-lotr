package com.gempukku.lotro.game.state.tribbles;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.game.decisions.AwaitingDecision;
import com.gempukku.lotro.game.modifiers.ModifierFlag;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.GameStats;
import com.gempukku.lotro.game.timing.PlayerOrder;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TribblesGameState extends GameState {
    private static final Logger _log = Logger.getLogger(TribblesGameState.class);
    private static final int LAST_MESSAGE_STORED_COUNT = 30;
    private PlayerOrder _playerOrder;
    private LotroFormat _format;
    private final Map<String, List<LotroPhysicalCardImpl>> _playPiles = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _adventureDecks = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _decks = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _hands = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _discards = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _stacked = new HashMap<>();
    private final Map<String, List<LotroPhysicalCardImpl>> _removed = new HashMap<>();
    private final List<LotroPhysicalCardImpl> _inPlay = new LinkedList<>();
    private final Map<Integer, LotroPhysicalCardImpl> _allCards = new HashMap<>();
    private String _currentPlayerId;
    private Phase _currentPhase = Phase.PUT_RING_BEARER;
    private boolean _consecutiveAction;
    private final Map<String, Boolean> _playerDecked = new HashMap<>();
    private final Map<String, AwaitingDecision> _playerDecisions = new HashMap<>();

    private final Set<GameStateListener> _gameStateListeners = new HashSet<>();
    private final LinkedList<String> _lastMessages = new LinkedList<>();

    private int _nextCardId = 0;
    private int _nextTribble;
    private boolean _chainBroken;
    private int _currentRound = 0;
    private int nextCardId() {
        return _nextCardId++;
    }

    public void init(PlayerOrder playerOrder, String firstPlayer, Map<String, List<String>> cards,
                     CardBlueprintLibrary library, LotroFormat format) {
        _playerOrder = playerOrder;
        _currentPlayerId = firstPlayer;
        _format = format;

        _nextTribble = 1;
        _chainBroken = false;

        for (String player : playerOrder.getAllPlayers()) {
            _playerDecked.put(player, false);
        }

        for (Map.Entry<String, List<String>> stringListEntry : cards.entrySet()) {
            String playerId = stringListEntry.getKey();
            List<String> decks = stringListEntry.getValue();

            _adventureDecks.put(playerId, new LinkedList<>());
            _decks.put(playerId, new LinkedList<>());
            _hands.put(playerId, new LinkedList<>());
            _removed.put(playerId, new LinkedList<>());
            _discards.put(playerId, new LinkedList<>());
            _stacked.put(playerId, new LinkedList<>());
            _playPiles.put(playerId, new LinkedList<>());

            addPlayerCards(playerId, decks, library);
        }

        for (GameStateListener listener : getAllGameStateListeners()) {
            listener.initializeBoard(playerOrder.getAllPlayers(), format.discardPileIsPublic());
        }

        //This needs done after the Player Order initialization has been issued, or else the player
        // adventure deck areas don't exist.
        for (String playerId : playerOrder.getAllPlayers()) {
            for(var site : getAdventureDeck(playerId)) {
                for (GameStateListener listener : getAllGameStateListeners()) {
                    listener.cardCreated(site);
                }
            }
        }
    }

    public void finish() {
        for (GameStateListener listener : getAllGameStateListeners()) {
            listener.endGame();
        }

        if(_playerOrder == null || _playerOrder.getAllPlayers() == null)
            return;

        for (String playerId : _playerOrder.getAllPlayers()) {
            for(var card : getDeck(playerId)) {
                for (GameStateListener listener : getAllGameStateListeners()) {
                    listener.cardCreated(card, true);
                }
            }
        }
    }

    private void addPlayerCards(String playerId, List<String> cards, CardBlueprintLibrary library) {
        for (String blueprintId : cards) {
            try {
                LotroPhysicalCardImpl physicalCard = createPhysicalCardImpl(playerId, library, blueprintId);
                if (physicalCard.getBlueprint().getCardType() == CardType.SITE) {
                    physicalCard.setZone(Zone.ADVENTURE_DECK);
                    _adventureDecks.get(playerId).add(physicalCard);
                } else {
                    physicalCard.setZone(Zone.DECK);
                    _decks.get(playerId).add(physicalCard);
                }
            } catch (CardNotFoundException exp) {
                // Ignore the card
            }
        }
    }

    public LotroPhysicalCard createPhysicalCard(String ownerPlayerId, CardBlueprintLibrary library, String blueprintId) throws CardNotFoundException {
        return createPhysicalCardImpl(ownerPlayerId, library, blueprintId);
    }

    private LotroPhysicalCardImpl createPhysicalCardImpl(String playerId, CardBlueprintLibrary library, String blueprintId) throws CardNotFoundException {
        LotroCardBlueprint card = library.getLotroCardBlueprint(blueprintId);

        int cardId = nextCardId();
        LotroPhysicalCardImpl result = new LotroPhysicalCardImpl(cardId, blueprintId, playerId, card);

        _allCards.put(cardId, result);

        return result;
    }

    public boolean isConsecutiveAction() {
        return _consecutiveAction;
    }

    public void setConsecutiveAction(boolean consecutiveAction) {
        _consecutiveAction = consecutiveAction;
    }

    public PlayerOrder getPlayerOrder() {
        return _playerOrder;
    }

    public void addGameStateListener(String playerId, GameStateListener gameStateListener, GameStats gameStats) {
        _gameStateListeners.add(gameStateListener);
        sendStateToPlayer(playerId, gameStateListener, gameStats);
    }

    public void removeGameStateListener(GameStateListener gameStateListener) {
        _gameStateListeners.remove(gameStateListener);
    }

    private Collection<GameStateListener> getAllGameStateListeners() {
        return Collections.unmodifiableSet(_gameStateListeners);
    }

    private String getPhaseString() {
        return _currentPhase.getHumanReadable();
    }

    private void sendStateToPlayer(String playerId, GameStateListener listener, GameStats gameStats) {
        if (_playerOrder != null) {
            listener.initializeBoard(_playerOrder.getAllPlayers(), _format.discardPileIsPublic());
            if (_currentPlayerId != null)
                listener.setCurrentPlayerId(_currentPlayerId);
            if (_currentPhase != null)
                listener.setCurrentPhase(getPhaseString());
            for (Map.Entry<String, Boolean> stringBooleanEntry : _playerDecked.entrySet())
                listener.setPlayerDecked(stringBooleanEntry.getKey(), stringBooleanEntry.getValue());

            Set<LotroPhysicalCard> cardsLeftToSent = new LinkedHashSet<>(_inPlay);
            Set<LotroPhysicalCard> sentCardsFromPlay = new HashSet<>();

            int cardsToSendAtLoopStart;
            do {
                cardsToSendAtLoopStart = cardsLeftToSent.size();
                Iterator<LotroPhysicalCard> cardIterator = cardsLeftToSent.iterator();
                while (cardIterator.hasNext()) {
                    LotroPhysicalCard physicalCard = cardIterator.next();
                    LotroPhysicalCard attachedTo = physicalCard.getAttachedTo();
                    if (attachedTo == null || sentCardsFromPlay.contains(attachedTo)) {
                        listener.cardCreated(physicalCard);
                        sentCardsFromPlay.add(physicalCard);

                        cardIterator.remove();
                    }
                }
            } while (cardsToSendAtLoopStart != cardsLeftToSent.size() && cardsLeftToSent.size() > 0);

            // Finally the stacked ones
            for (List<LotroPhysicalCardImpl> physicalCards : _stacked.values())
                for (LotroPhysicalCardImpl physicalCard : physicalCards)
                    listener.cardCreated(physicalCard);

            List<LotroPhysicalCardImpl> hand = _hands.get(playerId);
            if (hand != null) {
                for (LotroPhysicalCardImpl physicalCard : hand)
                    listener.cardCreated(physicalCard);
            }

            List<LotroPhysicalCardImpl> discard = _discards.get(playerId);
            if (discard != null) {
                for (LotroPhysicalCardImpl physicalCard : discard)
                    listener.cardCreated(physicalCard);
            }

            List<LotroPhysicalCardImpl> adventureDeck = _adventureDecks.get(playerId);
            if (adventureDeck != null) {
                for (LotroPhysicalCardImpl physicalCard : adventureDeck)
                    listener.cardCreated(physicalCard);
            }

            List<LotroPhysicalCardImpl> playPile = _playPiles.get(playerId);
            if (playPile != null) {
                for (LotroPhysicalCardImpl physicalCard : playPile)
                    listener.cardCreated(physicalCard);
            }

            listener.sendGameStats(gameStats);
        }

        for (String lastMessage : _lastMessages)
            listener.sendMessage(lastMessage);

        final AwaitingDecision awaitingDecision = _playerDecisions.get(playerId);
        if (awaitingDecision != null)
            listener.decisionRequired(playerId, awaitingDecision);
    }

    public void sendMessage(String message) {
        _lastMessages.add(message);
        if (_lastMessages.size() > LAST_MESSAGE_STORED_COUNT)
            _lastMessages.removeFirst();
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendMessage(message);
    }

    public void playerDecisionStarted(String playerId, AwaitingDecision awaitingDecision) {
        _playerDecisions.put(playerId, awaitingDecision);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.decisionRequired(playerId, awaitingDecision);
    }

    public void playerDecisionFinished(String playerId) {
        _playerDecisions.remove(playerId);
    }

    public void transferCard(LotroPhysicalCard card, LotroPhysicalCard transferTo) {
        if (card.getZone() != Zone.ATTACHED)
            ((LotroPhysicalCardImpl) card).setZone(Zone.ATTACHED);

        ((LotroPhysicalCardImpl) card).attachTo((LotroPhysicalCardImpl) transferTo);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void takeControlOfCard(String playerId, DefaultGame game, LotroPhysicalCard card, Zone zone) {
        ((LotroPhysicalCardImpl) card).setCardController(playerId);
        ((LotroPhysicalCardImpl) card).setZone(zone);
        if (card.getBlueprint().getCardType() == CardType.SITE)
            startAffectingControlledSite(game, card);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void loseControlOfCard(LotroPhysicalCard card, Zone zone) {
        ((LotroPhysicalCardImpl) card).setCardController(null);
        ((LotroPhysicalCardImpl) card).setZone(zone);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardMoved(card);
    }

    public void attachCard(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard attachTo) throws InvalidParameterException {
        if(card == attachTo)
            throw new InvalidParameterException("Cannot attach card to itself!");

        ((LotroPhysicalCardImpl) card).attachTo((LotroPhysicalCardImpl) attachTo);
        addCardToZone(game, card, Zone.ATTACHED);
    }

    public void stackCard(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard stackOn) throws InvalidParameterException {
        if(card == stackOn)
            throw new InvalidParameterException("Cannot stack card on itself!");

        ((LotroPhysicalCardImpl) card).stackOn((LotroPhysicalCardImpl) stackOn);
        addCardToZone(game, card, Zone.STACKED);
    }

    public void cardAffectsCard(String playerPerforming, LotroPhysicalCard card, Collection<LotroPhysicalCard> affectedCards) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardAffectedByCard(playerPerforming, card, affectedCards);
    }

    public void eventPlayed(LotroPhysicalCard card) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.eventPlayed(card);
    }

    public void activatedCard(String playerPerforming, LotroPhysicalCard card) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardActivated(playerPerforming, card);
    }

    private List<LotroPhysicalCardImpl> getZoneCards(String playerId, Zone zone) {
        if (zone == Zone.DECK)
            return _decks.get(playerId);
        else if (zone == Zone.PLAY_PILE)
            return _playPiles.get(playerId);
        else if (zone == Zone.ADVENTURE_DECK)
            return _adventureDecks.get(playerId);
        else if (zone == Zone.DISCARD)
            return _discards.get(playerId);
        else if (zone == Zone.HAND)
            return _hands.get(playerId);
        else if (zone == Zone.REMOVED)
            return _removed.get(playerId);
        else if (zone == Zone.STACKED)
            return _stacked.get(playerId);
        else // This should never be accessed
            return _inPlay;
    }

    public void removeCardsFromZone(String playerPerforming, Collection<LotroPhysicalCard> cards) {
        for (LotroPhysicalCard card : cards) {
            List<LotroPhysicalCardImpl> zoneCards = getZoneCards(card.getOwner(), card.getZone());
            if (!zoneCards.contains(card))
                _log.error("Card was not found in the expected zone");
        }

        for (LotroPhysicalCard card : cards) {
            Zone zone = card.getZone();

            if (zone.isInPlay()) {
                if (card.getBlueprint().getCardType() != CardType.SITE || (getCurrentPhase() != Phase.PLAY_STARTING_FELLOWSHIP))
                    stopAffecting(card);
            }

            if (zone == Zone.STACKED)
                stopAffectingStacked(card);
            else if (zone == Zone.DISCARD)
                stopAffectingInDiscard(card);

            List<LotroPhysicalCardImpl> zoneCards = getZoneCards(card.getOwner(), zone);
            zoneCards.remove(card);
            if (zone.isInPlay())
                _inPlay.remove(card);

            if (zone == Zone.ATTACHED)
                ((LotroPhysicalCardImpl) card).attachTo(null);

            if (zone == Zone.STACKED)
                ((LotroPhysicalCardImpl) card).stackOn(null);

            //If this is reset, then there is no way for self-discounting effects (which are evaluated while in the void)
            // to have any sort of permanent effect once the card is in play.
            if(zone != Zone.VOID_FROM_HAND && zone != Zone.VOID)
                card.setWhileInZoneData(null);
        }

        for (GameStateListener listener : getAllGameStateListeners())
            listener.cardsRemoved(playerPerforming, cards);

        for (LotroPhysicalCard card : cards) {
            ((LotroPhysicalCardImpl) card).setZone(null);
        }
    }

    public void addCardToZone(DefaultGame game, LotroPhysicalCard card, Zone zone) {
        addCardToZone(game, card, zone, true);
    }

    private void addCardToZone(DefaultGame game, LotroPhysicalCard card, Zone zone, boolean end) {
        if (zone == Zone.DISCARD && game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.REMOVE_CARDS_GOING_TO_DISCARD))
            zone = Zone.REMOVED;

        if (zone.isInPlay()) {
            assignNewCardId(card);
            _inPlay.add((LotroPhysicalCardImpl) card);
        }

        List<LotroPhysicalCardImpl> zoneCards = getZoneCards(card.getOwner(), zone);
        if (end)
            zoneCards.add((LotroPhysicalCardImpl) card);
        else
            zoneCards.add(0, (LotroPhysicalCardImpl) card);

        if (card.getZone() != null)
            _log.error("Card was in " + card.getZone() + " when tried to add to zone: " + zone);

        ((LotroPhysicalCardImpl) card).setZone(zone);

        if (zone == Zone.ADVENTURE_PATH) {
            for (GameStateListener listener : getAllGameStateListeners())
                listener.setSite(card);
        } else {
            for (GameStateListener listener : getAllGameStateListeners())
                listener.cardCreated(card);
        }

        if (_currentPhase.isCardsAffectGame()) {
            if (zone.isInPlay())
                if (card.getBlueprint().getCardType() != CardType.SITE || (getCurrentPhase() != Phase.PLAY_STARTING_FELLOWSHIP))
                    startAffecting(game, card);

            if (zone == Zone.STACKED)
                startAffectingStacked(game, card);
            else if (zone == Zone.DISCARD)
                startAffectingInDiscard(game, card);
        }
    }

    private void assignNewCardId(LotroPhysicalCard card) {
        _allCards.remove(card.getCardId());
        int newCardId = nextCardId();
        ((LotroPhysicalCardImpl) card).setCardId(newCardId);
        _allCards.put(newCardId, ((LotroPhysicalCardImpl) card));
    }

    public void shuffleCardsIntoDeck(Collection<? extends LotroPhysicalCard> cards, String playerId) {
        List<LotroPhysicalCardImpl> zoneCards = _decks.get(playerId);

        for (LotroPhysicalCard card : cards) {
            zoneCards.add((LotroPhysicalCardImpl) card);

            ((LotroPhysicalCardImpl) card).setZone(Zone.DECK);
        }

        shuffleDeck(playerId);
    }

    public void shufflePlayPileIntoDeck(TribblesGame game, String playerId) {
        List<LotroPhysicalCard> playPile = new LinkedList<>(getPlayPile(playerId));
        removeCardsFromZone(playerId, playPile);
        for (LotroPhysicalCard card : playPile) {
            addCardToZone(game, card, Zone.DECK);
        }
        shuffleDeck(playerId);
    }

    public void discardHand(TribblesGame game, String playerId) {
        List<LotroPhysicalCard> hand = new LinkedList<>(getHand(playerId));
        removeCardsFromZone(playerId, hand);
        for (LotroPhysicalCard card : hand) {
            addCardToZone(game, card, Zone.DISCARD);
        }
    }

    public void putCardOnBottomOfDeck(LotroPhysicalCard card) {
        addCardToZone(null, card, Zone.DECK, true);
    }

    public void putCardOnTopOfDeck(LotroPhysicalCard card) {
        addCardToZone(null, card, Zone.DECK, false);
    }

    public boolean iterateActiveCards(PhysicalCardVisitor physicalCardVisitor) {
        for (LotroPhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard))
                if (physicalCardVisitor.visitPhysicalCard(physicalCard))
                    return true;
        }

        return false;
    }

    public LotroPhysicalCard findCardById(int cardId) {
        return _allCards.get(cardId);
    }

    public Iterable<? extends LotroPhysicalCard> getAllCards() {
        return Collections.unmodifiableCollection(_allCards.values());
    }

    public List<? extends LotroPhysicalCard> getHand(String playerId) {
        return Collections.unmodifiableList(_hands.get(playerId));
    }

    public List<? extends LotroPhysicalCard> getRemoved(String playerId) {
        return Collections.unmodifiableList(_removed.get(playerId));
    }

    public List<? extends LotroPhysicalCard> getDeck(String playerId) {
        return Collections.unmodifiableList(_decks.get(playerId));
    }

    public List<? extends LotroPhysicalCard> getDiscard(String playerId) {
        return Collections.unmodifiableList(_discards.get(playerId));
    }

    public List<? extends LotroPhysicalCard> getAdventureDeck(String playerId) {
        return Collections.unmodifiableList(_adventureDecks.get(playerId));
    }

    public List<LotroPhysicalCard> getPlayPile(String playerId) {
        return Collections.unmodifiableList(_playPiles.get(playerId));
    }
    public List<? extends LotroPhysicalCard> getInPlay() {
        return Collections.unmodifiableList(_inPlay);
    }

    public List<? extends LotroPhysicalCard> getStacked(String playerId) {
        return Collections.unmodifiableList(_stacked.get(playerId));
    }

    public String getCurrentPlayerId() {
        return _currentPlayerId;
    }

    public void setCurrentPlayerId(String playerId) {
        _currentPlayerId = playerId;
    }

    public void setPlayerDecked(String playerId, boolean bool) {
        _playerDecked.put(playerId, bool);
        for (GameStateListener listener : getAllGameStateListeners())
            listener.setPlayerDecked(playerId, bool);
    }

    public boolean getPlayerDecked(String playerId) {
        return _playerDecked.get(playerId);
    }

    public List<LotroPhysicalCard> getAttachedCards(LotroPhysicalCard card) {
        List<LotroPhysicalCard> result = new LinkedList<>();
        for (LotroPhysicalCardImpl physicalCard : _inPlay) {
            if (physicalCard.getAttachedTo() != null && physicalCard.getAttachedTo() == card)
                result.add(physicalCard);
        }
        return result;
    }

    public List<LotroPhysicalCard> getStackedCards(LotroPhysicalCard card) {
        List<LotroPhysicalCard> result = new LinkedList<>();
        for (List<LotroPhysicalCardImpl> physicalCardList : _stacked.values()) {
            for (LotroPhysicalCardImpl physicalCard : physicalCardList) {
                if (physicalCard.getStackedOn() == card)
                    result.add(physicalCard);
            }
        }
        return result;
    }

    public void startPlayerTurn(String playerId) {
        _currentPlayerId = playerId;

        for (GameStateListener listener : getAllGameStateListeners())
            listener.setCurrentPlayerId(_currentPlayerId);
    }

    public boolean isCardInPlayActive(LotroPhysicalCard card) {
        Side side = card.getBlueprint().getSide();
        // Either it's not attached or attached to active card
        // AND is a site or fp/ring of current player or shadow of any other player
        if (card.getBlueprint().getCardType() == CardType.SITE)
            return _currentPhase != Phase.PUT_RING_BEARER && _currentPhase != Phase.PLAY_STARTING_FELLOWSHIP;

        if (card.getBlueprint().getCardType() == CardType.THE_ONE_RING)
            return card.getOwner().equals(_currentPlayerId);

        if (card.getOwner().equals(_currentPlayerId) && side == Side.SHADOW)
            return false;

        if (!card.getOwner().equals(_currentPlayerId) && side == Side.FREE_PEOPLE)
            return false;

        if (card.getAttachedTo() != null)
            return isCardInPlayActive(card.getAttachedTo());

        return true;
    }

    public void startAffectingCardsForCurrentPlayer(DefaultGame game) {
        // Active non-sites are affecting
        for (LotroPhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard) && physicalCard.getBlueprint().getCardType() != CardType.SITE)
                startAffecting(game, physicalCard);
            else if (physicalCard.getBlueprint().getCardType() == CardType.SITE &&
                    physicalCard.getCardController() != null) {
                startAffectingControlledSite(game, physicalCard);
            }
        }

        // Stacked cards on active cards are stack-affecting
        for (List<LotroPhysicalCardImpl> stackedCards : _stacked.values())
            for (LotroPhysicalCardImpl stackedCard : stackedCards)
                if (isCardInPlayActive(stackedCard.getStackedOn()))
                    startAffectingStacked(game, stackedCard);

        for (List<LotroPhysicalCardImpl> discardedCards : _discards.values())
            for (LotroPhysicalCardImpl discardedCard : discardedCards)
                startAffectingInDiscard(game, discardedCard);
    }

    private void startAffectingControlledSite(DefaultGame game, LotroPhysicalCard physicalCard) {
        ((LotroPhysicalCardImpl) physicalCard).startAffectingGameControlledSite(game);
    }

    public void stopAffectingCardsForCurrentPlayer() {
        for (LotroPhysicalCardImpl physicalCard : _inPlay) {
            if (isCardInPlayActive(physicalCard) && physicalCard.getBlueprint().getCardType() != CardType.SITE)
                stopAffecting(physicalCard);
        }

        for (List<LotroPhysicalCardImpl> stackedCards : _stacked.values())
            for (LotroPhysicalCardImpl stackedCard : stackedCards)
                if (isCardInPlayActive(stackedCard.getStackedOn()))
                    stopAffectingStacked(stackedCard);

        for (List<LotroPhysicalCardImpl> discardedCards : _discards.values())
            for (LotroPhysicalCardImpl discardedCard : discardedCards)
                stopAffectingInDiscard(discardedCard);
    }

    private void startAffecting(DefaultGame game, LotroPhysicalCard card) {
        ((LotroPhysicalCardImpl) card).startAffectingGame(game);
    }

    private void stopAffecting(LotroPhysicalCard card) {
        ((LotroPhysicalCardImpl) card).stopAffectingGame();
    }

    private void startAffectingStacked(DefaultGame game, LotroPhysicalCard card) {
        if (isCardAffectingGame(card))
            ((LotroPhysicalCardImpl) card).startAffectingGameStacked(game);
    }

    private void stopAffectingStacked(LotroPhysicalCard card) {
        if (isCardAffectingGame(card))
            ((LotroPhysicalCardImpl) card).stopAffectingGameStacked();
    }

    private void startAffectingInDiscard(DefaultGame game, LotroPhysicalCard card) {
        if (isCardAffectingGame(card))
            ((LotroPhysicalCardImpl) card).startAffectingGameInDiscard(game);
    }

    private void stopAffectingInDiscard(LotroPhysicalCard card) {
        if (isCardAffectingGame(card))
            ((LotroPhysicalCardImpl) card).stopAffectingGameInDiscard();
    }

    private boolean isCardAffectingGame(LotroPhysicalCard card) {
        final Side side = card.getBlueprint().getSide();
        if (side == Side.SHADOW)
            return !getCurrentPlayerId().equals(card.getOwner());
        else if (side == Side.FREE_PEOPLE)
            return getCurrentPlayerId().equals(card.getOwner());
        else
            return false;
    }

    public void setCurrentPhase(Phase phase) {
        _currentPhase = phase;
        for (GameStateListener listener : getAllGameStateListeners())
            listener.setCurrentPhase(getPhaseString());
    }

    public Phase getCurrentPhase() {
        return _currentPhase;
    }

    public LotroPhysicalCard removeTopDeckCard(String player) {
        List<LotroPhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            final LotroPhysicalCard topDeckCard = deck.get(0);
            removeCardsFromZone(null, Collections.singleton(topDeckCard));
            return topDeckCard;
        } else {
            return null;
        }
    }

    public LotroPhysicalCard removeBottomDeckCard(String player) {
        List<LotroPhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            final LotroPhysicalCard topDeckCard = deck.get(deck.size() - 1);
            removeCardsFromZone(null, Collections.singleton(topDeckCard));
            return topDeckCard;
        } else {
            return null;
        }
    }

    public void playerDrawsCard(String player) {
        List<LotroPhysicalCardImpl> deck = _decks.get(player);
        if (deck.size() > 0) {
            LotroPhysicalCard card = deck.get(0);
            removeCardsFromZone(null, Collections.singleton(card));
            addCardToZone(null, card, Zone.HAND);
        }
    }

    public void shuffleDeck(String player) {
        List<LotroPhysicalCardImpl> deck = _decks.get(player);
        Collections.shuffle(deck, ThreadLocalRandom.current());
    }

    public void sendGameStats(GameStats gameStats) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendGameStats(gameStats);
    }

    public void sendWarning(String player, String warning) {
        for (GameStateListener listener : getAllGameStateListeners())
            listener.sendWarning(player, warning);
    }

    public void setNextTribble(int num) {
        _nextTribble = num;
    }

    public int getNextTribble() { return _nextTribble; }

    public void setChainBroken(boolean chainBroken) {
        _chainBroken = chainBroken;
        if (chainBroken) {
            sendMessage("The chain has been broken.");
            for (GameStateListener listener : getAllGameStateListeners()) {
                listener.setTribbleSequence("1 or " + _nextTribble);
            }
        }
    }

    public boolean getChainBroken() {
        return _chainBroken;
    }

    @Override
    public void playEffectReturningResult(LotroPhysicalCard cardPlayed) {
        int tribblePlayed = cardPlayed.getBlueprint().getTribbleValue();
        int currentTribble = _nextTribble;
        if (tribblePlayed == 100000) {
            _nextTribble = 1;
        } else {
            _nextTribble = tribblePlayed * 10;
        }
        _chainBroken = false;
        sendMessage("Tribble chain advanced from " + currentTribble + " to " + _nextTribble);
        for (GameStateListener listener : getAllGameStateListeners()) {
            listener.setTribbleSequence(String.valueOf(_nextTribble));
        }
    }

    @Override
    public void playerPassEffect() {
        this.setChainBroken(true);
    }

    public void playerWentOut(String player) {
        // TODO
    }

    public void playerScored(String player, int points) {
        // TODO
    }

    public void advanceRoundNum() {
        _currentRound++;
    }

    public int getRoundNum() {
        return _currentRound;
    }
    public boolean isLastRound() {
        return (_currentRound == 5);
    }
}