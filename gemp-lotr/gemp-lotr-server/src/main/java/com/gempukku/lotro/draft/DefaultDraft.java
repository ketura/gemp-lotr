package com.gempukku.lotro.draft;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.tournament.SingleEliminationTournament;
import com.gempukku.lotro.tournament.Tournament;
import com.gempukku.lotro.tournament.TournamentService;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultDraft implements Draft {
    // 35 seconds
    public static final int PICK_TIME = 35 * 1000;
    // 10 minutes
    public static final int DECK_BUILD_TIME = 10 * 60 * 1000;

    private String _tournamentIdPrefix;
    private String _tournamentNamePrefix;
    private String _tournamentId;
    private String _tournamentName;
    private LotroFormatLibrary _formatLibrary;
    private String _format;
    private CollectionsManager _collectionsManager;
    private TournamentService _tournamentService;
    private CollectionType _collectionType;
    private PacksStorage _packsStorage;
    private DraftPack _draftPack;
    private List<Player> _players;

    private List<MutableCardCollection> _cardChoices = new ArrayList<MutableCardCollection>();
    private Map<String, MutableCardCollection> _cardChoice = new HashMap<String, MutableCardCollection>();

    private int _playerCount;
    private boolean _deckBuildingStarted;
    private boolean _tournamentStarted;

    private long _lastPickStart;
    private long _deckBuildStart;

    private int _nextPickNumber = 0;
    private int _nextPackIndex = 0;
    private Map<String, LotroDeck> _playerDecks = new HashMap<String, LotroDeck>();

    private ReadWriteLock _lock = new ReentrantReadWriteLock();

    public DefaultDraft(String tournamentId, String tournamentName, LotroFormatLibrary formatLibrary, String format, CollectionsManager collectionsManager, TournamentService tournamentService, CollectionType collectionType, PacksStorage packsStorage, DraftPack draftPack, List<Player> players) {
        _tournamentId = tournamentId;
        _tournamentName = tournamentName;
        _formatLibrary = formatLibrary;
        _format = format;
        _collectionsManager = collectionsManager;
        _tournamentService = tournamentService;
        _collectionType = collectionType;
        _packsStorage = packsStorage;
        _draftPack = draftPack;
        _players = players;
        _playerCount = _players.size();

        CardCollection fixedCollection = _draftPack.getFixedCollection();
        for (Player player : _players)
            _collectionsManager.addPlayerCollection(player, _collectionType, fixedCollection);
    }

    @Override
    public void advanceDraft(DraftCallback draftCallback) {
        _lock.writeLock().lock();
        try {
            if (_deckBuildingStarted) {
                if (haveAllPlayersSubmittedDecks() || deckBuildTimePassed()) {
                    startTournament(draftCallback);
                }
            } else {
                if (haveAllPlayersPicked()) {
                    if (haveAllCardsBeenChosen()) {
                        if (haveMorePacks()) {
                            openNextPacks();
                        } else {
                            _deckBuildStart = System.currentTimeMillis();
                            _deckBuildingStarted = true;
                        }
                    } else {
                        presentNewCardChoices();
                    }
                } else {
                    if (choiceTimePassed()) {
                        forceRandomCardChoice();
                    }
                }
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public void playerChosenCard(String playerName, String cardId) {
        _lock.writeLock().lock();
        try {
            playerChosen(playerName, cardId);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public DraftCardChoice getCardChoice(String playerName) {
        _lock.readLock().lock();
        try {
            if (_deckBuildingStarted)
                return null;
            MutableCardCollection cardChoice = _cardChoice.get(playerName);

            return new DefaultDraftCardChoice(cardChoice, _lastPickStart + PICK_TIME);
        } finally {
            _lock.readLock().unlock();
        }
    }

    @Override
    public void playerSummittedDeck(Player player, LotroDeck deck) throws DeckInvalidException {
        _lock.writeLock().lock();
        try {
            LotroFormat format = _formatLibrary.getFormat(_format);
            format.validateDeck(deck);

            CardCollection collection = _collectionsManager.getPlayerCollection(player, _collectionType.getCode());
            if (collection == null)
                throw new DeckInvalidException("You don't have cards in the required collection to play in this format");

            Map<String, Integer> deckCardCounts = CollectionUtils.getTotalCardCountForDeck(deck);

            for (Map.Entry<String, Integer> cardCount : deckCardCounts.entrySet()) {
                final int collectionCount = collection.getItemCount(cardCount.getKey());
                if (collectionCount < cardCount.getValue())
                    throw new DeckInvalidException("You don't have the required cards in collection");
            }

            _playerDecks.put(player.getName(), deck);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isPlayerInDraft(String player) {
        for (Player playerInDraft : _players) {
            if (playerInDraft.getName().equals(player))
                return true;
        }

        return false;
    }

    @Override
    public boolean isFinished() {
        return _tournamentStarted;
    }

    private void startTournament(DraftCallback draftCallback) {
        String parameters = _format + "," + _collectionType.getCode() + "," + _collectionType.getFullName() + "," + _tournamentName;

        for (Map.Entry<String, LotroDeck> playerDeck : _playerDecks.entrySet())
            _tournamentService.addPlayer(_tournamentId, playerDeck.getKey(), playerDeck.getValue());

        Tournament tournament = _tournamentService.addTournament(_tournamentId, SingleEliminationTournament.class.getName(), parameters, new Date());

        draftCallback.createTournament(tournament);

        _tournamentStarted = true;
    }

    private boolean haveAllPlayersSubmittedDecks() {
        return _playerDecks.size() == _playerCount;
    }

    private void forceRandomCardChoice() {
        Map<String, MutableCardCollection> cardChoiceCopy = new HashMap<String, MutableCardCollection>(_cardChoice);

        for (Map.Entry<String, MutableCardCollection> playerChoices : cardChoiceCopy.entrySet()) {
            String playerName = playerChoices.getKey();
            MutableCardCollection collection = playerChoices.getValue();
            String cardId = collection.getAll().entrySet().iterator().next().getKey();
            playerChosen(playerName, cardId);
        }
    }

    private void playerChosen(String playerName, String cardId) {
        MutableCardCollection cardChoice = _cardChoice.get(playerName);
        if (cardChoice != null) {
            if (cardChoice.removeItem(cardId, 1)) {
                _collectionsManager.addItemsToPlayerCollection(playerName, _collectionType, Arrays.asList(CardCollection.Item.createItem(cardId, 1)));
                _cardChoice.remove(playerName);
            }
        }
    }

    private void presentNewCardChoices() {
        for (int i = 0; i < _players.size(); i++) {
            Player player = _players.get(i);
            _cardChoice.put(player.getName(), _cardChoices.get((i + _nextPickNumber) % _playerCount));
        }
        _nextPickNumber++;
        _lastPickStart = System.currentTimeMillis();
    }

    private void openNextPacks() {
        _cardChoices.clear();
        String packId = _draftPack.getPacks().get(_nextPackIndex);
        for (int i = 0; i < _playerCount; i++) {
            MutableCardCollection cardCollection = new DefaultCardCollection();
            cardCollection.addItem(packId, 1);
            cardCollection.openPack(packId, null, _packsStorage);
            _cardChoices.add(cardCollection);
        }
        _nextPackIndex++;
        _nextPickNumber = 0;
    }

    private boolean deckBuildTimePassed() {
        return System.currentTimeMillis() > DECK_BUILD_TIME + _deckBuildStart;
    }

    private boolean choiceTimePassed() {
        return System.currentTimeMillis() > PICK_TIME + _lastPickStart;
    }

    private boolean haveMorePacks() {
        return _nextPackIndex < _draftPack.getPacks().size();
    }

    private boolean haveAllCardsBeenChosen() {
        return _cardChoices.size() == 0 || _cardChoices.get(0).getAll().size() == 0;
    }

    private boolean haveAllPlayersPicked() {
        return _cardChoice.isEmpty();
    }
}
