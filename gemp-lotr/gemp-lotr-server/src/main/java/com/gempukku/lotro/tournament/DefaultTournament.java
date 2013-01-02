package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.competitive.StandingsProducer;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft.DefaultDraft;
import com.gempukku.lotro.draft.Draft;
import com.gempukku.lotro.draft.DraftCardChoice;
import com.gempukku.lotro.draft.DraftPack;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultTournament implements Tournament {
    // 10 minutes
    private int _deckBuildTime = 10 * 60 * 1000;
    private long _waitForPairingsTime = 1000 * 60 * 2;

    private PairingMechanism _pairingMechanism;
    private TournamentPrizes _tournamentPrizes;
    private String _tournamentId;
    private String _tournamentName;
    private String _format;
    private CollectionType _collectionType;
    private Stage _tournamentStage;
    private int _tournamentRound;

    private Set<String> _players;
    private Map<String, LotroDeck> _playerDecks;
    private Set<String> _droppedPlayers;
    private Map<String, Integer> _playerByes;

    private Set<String> _currentlyPlayingPlayers;
    private Set<TournamentMatch> _finishedTournamentMatches = new HashSet<TournamentMatch>();

    private TournamentService _tournamentService;

    private ReadWriteLock _lock = new ReentrantReadWriteLock();
    private TournamentTask _nextTask;

    private long _deckBuildStartTime;
    private Draft _draft;

    private List<PlayerStanding> _currentStandings;

    public DefaultTournament(CollectionsManager collectionsManager, TournamentService tournamentService,
                             PacksStorage packsStorage, DraftPack draftPack, String tournamentId, String tournamentName, String format, CollectionType collectionType,
                             int tournamentRound, Stage tournamentStage, PairingMechanism pairingMechanism, TournamentPrizes tournamentPrizes) {
        _tournamentService = tournamentService;
        _tournamentId = tournamentId;
        _tournamentName = tournamentName;
        _format = format;
        _collectionType = collectionType;
        _tournamentRound = tournamentRound;
        _tournamentStage = tournamentStage;
        _pairingMechanism = pairingMechanism;
        _tournamentPrizes = tournamentPrizes;

        _currentlyPlayingPlayers = new HashSet<String>();

        _players = new HashSet<String>(_tournamentService.getPlayers(_tournamentId));
        _playerDecks = new HashMap<String, LotroDeck>(_tournamentService.getPlayerDecks(_tournamentId));
        _droppedPlayers = new HashSet<String>(_tournamentService.getDroppedPlayers(_tournamentId));
        _playerByes = new HashMap<String, Integer>(_tournamentService.getPlayerByes(_tournamentId));

        if (_tournamentStage == Stage.PLAYING_GAMES) {
            Map<String, String> matchesToCreate = new HashMap<String, String>();
            for (TournamentMatch tournamentMatch : _tournamentService.getMatches(_tournamentId)) {
                if (tournamentMatch.isFinished())
                    _finishedTournamentMatches.add(tournamentMatch);
                else {
                    _currentlyPlayingPlayers.add(tournamentMatch.getPlayerOne());
                    _currentlyPlayingPlayers.add(tournamentMatch.getPlayerTwo());
                    matchesToCreate.put(tournamentMatch.getPlayerOne(), tournamentMatch.getPlayerTwo());
                }
            }

            if (matchesToCreate.size() > 0)
                _nextTask = new CreateMissingGames(matchesToCreate);
        } else if (_tournamentStage == Stage.DRAFT) {
            _draft = new DefaultDraft(collectionsManager, _collectionType, packsStorage, draftPack,
                    _players);
        } else if (_tournamentStage == Stage.DECK_BUILDING) {
            _deckBuildStartTime = System.currentTimeMillis();
        } else if (_tournamentStage == Stage.FINISHED) {
            _finishedTournamentMatches.addAll(_tournamentService.getMatches(_tournamentId));
        }
    }

    public void setWaitForPairingsTime(long waitForPairingsTime) {
        _waitForPairingsTime = waitForPairingsTime;
    }

    @Override
    public String getTournamentId() {
        return _tournamentId;
    }

    @Override
    public String getTournamentName() {
        return _tournamentName;
    }

    @Override
    public Stage getTournamentStage() {
        return _tournamentStage;
    }

    @Override
    public CollectionType getCollectionType() {
        return _collectionType;
    }

    @Override
    public int getCurrentRound() {
        return _tournamentRound;
    }

    @Override
    public String getFormat() {
        return _format;
    }

    @Override
    public boolean isPlayerInCompetition(String player) {
        _lock.readLock().lock();
        try {
            return _tournamentStage != Stage.FINISHED && _players.contains(player) && !_droppedPlayers.contains(player);
        } finally {
            _lock.readLock().unlock();
        }
    }

    @Override
    public void reportGameFinished(String winner, String loser) {
        _lock.writeLock().lock();
        try {
            if (_tournamentStage == Stage.PLAYING_GAMES && _currentlyPlayingPlayers.contains(winner)
                    && _currentlyPlayingPlayers.contains(loser)) {
                _tournamentService.setMatchResult(_tournamentId, _tournamentRound, winner);
                _currentlyPlayingPlayers.remove(winner);
                _currentlyPlayingPlayers.remove(loser);
                _finishedTournamentMatches.add(
                        new TournamentMatch(winner, loser, winner, _tournamentRound));
                if (_pairingMechanism.shouldDropLoser())
                    _droppedPlayers.add(loser);
                _currentStandings = null;
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public void playerSummittedDeck(String player, LotroDeck deck) throws DeckInvalidException {
        _lock.writeLock().lock();
        try {
            if (_tournamentStage == Stage.DECK_BUILDING && _players.contains(player)) {
                _tournamentService.setPlayerDeck(_tournamentId, player, deck);
                _playerDecks.put(player, deck);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public void playerChosenCard(String playerName, String cardId) {
        _lock.writeLock().lock();
        try {
            if (_tournamentStage == Stage.DRAFT) {
                _draft.playerChosenCard(playerName, cardId);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public DraftCardChoice getCardChoice(String playerName) {
        _lock.readLock().lock();
        try {
            return _draft.getCardChoice(playerName);
        } finally {
            _lock.readLock().unlock();
        }
    }

    @Override
    public boolean dropPlayer(String player) {
        _lock.writeLock().lock();
        try {
            if (_currentlyPlayingPlayers.contains(player))
                return false;
            if (_tournamentStage == Stage.FINISHED)
                return false;
            if (_droppedPlayers.contains(player))
                return false;
            if (!_players.contains(player))
                return false;

            _tournamentService.dropPlayer(_tournamentId, player);
            _droppedPlayers.add(player);
            return true;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public void advanceTournament(TournamentCallback tournamentCallback, CollectionsManager collectionsManager) {
        _lock.writeLock().lock();
        try {
            if (_nextTask == null) {
                if (_tournamentStage == Stage.DRAFT) {
                    _draft.advanceDraft(tournamentCallback);
                    if (_draft.isFinished()) {
                        tournamentCallback.broadcastMessage("Drafting in tournament " + _tournamentName + " is finished, starting deck building");
                        _tournamentStage = Stage.DECK_BUILDING;
                        _tournamentService.updateTournamentStage(_tournamentId, _tournamentStage);
                        _deckBuildStartTime = System.currentTimeMillis();
                    }
                }
                if (_tournamentStage == Stage.DECK_BUILDING) {
                    if (_deckBuildStartTime + _deckBuildTime < System.currentTimeMillis()
                            || _playerDecks.size() == _players.size()) {
                        _tournamentStage = Stage.PLAYING_GAMES;
                        _tournamentService.updateTournamentStage(_tournamentId, _tournamentStage);
                    }
                }
                if (_tournamentStage == Stage.PLAYING_GAMES) {
                    if (_currentlyPlayingPlayers.size() == 0) {
                        if (_pairingMechanism.isFinished(_tournamentRound, _players, _droppedPlayers)) {
                            finishTournament(tournamentCallback, collectionsManager);
                        } else {
                            tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " will start round "+(_tournamentRound+1)+" in 2 minutes");
                            _nextTask = new PairPlayers();
                        }
                    }
                }
            }
            if (_nextTask != null && _nextTask.getExecuteAfter() <= System.currentTimeMillis()) {
                TournamentTask task = _nextTask;
                _nextTask = null;
                task.executeTask(tournamentCallback, collectionsManager);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public List<PlayerStanding> getCurrentStandings() {
        List<PlayerStanding> result = _currentStandings;
        if (result != null)
            return result;

        _lock.readLock().lock();
        try {
            _currentStandings = StandingsProducer.produceStandings(_players, _finishedTournamentMatches, 1, 0, _playerByes);
            return _currentStandings;
        } finally {
            _lock.readLock().unlock();
        }
    }

    private void finishTournament(TournamentCallback tournamentCallback, CollectionsManager collectionsManager) {
        _tournamentStage = Stage.FINISHED;
        _tournamentService.updateTournamentStage(_tournamentId, _tournamentStage);
        tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " is finished");
        awardPrizes(collectionsManager);
    }

    private void awardPrizes(CollectionsManager collectionsManager) {
        List<PlayerStanding> list = getCurrentStandings();
        for (PlayerStanding playerStanding : list) {
            CardCollection prizes = _tournamentPrizes.getPrizeForTournament(playerStanding, list.size());
            if (prizes != null)
                collectionsManager.addItemsToPlayerCollection(true, "Tournament " +getTournamentName()+" prize", playerStanding.getPlayerName(), CollectionType.MY_CARDS, prizes.getAll().values());
        }
    }


    private void createNewGame(TournamentCallback tournamentCallback, String playerOne, String playerTwo, boolean allowSpectators) {
        tournamentCallback.createGame(playerOne, _playerDecks.get(playerOne),
                playerTwo, _playerDecks.get(playerTwo), allowSpectators);
    }

    private void doPairing(TournamentCallback tournamentCallback, CollectionsManager collectionsManager) {
        _tournamentRound++;
        _tournamentService.updateTournamentRound(_tournamentId, _tournamentRound);
        Map<String, String> pairingResults = new HashMap<String, String>();
        Set<String> byeResults = new HashSet<String>();
        boolean finished = _pairingMechanism.pairPlayers(_tournamentRound, _players, _droppedPlayers, _playerByes, getCurrentStandings(), pairingResults, byeResults);
        if (finished) {
            finishTournament(tournamentCallback, collectionsManager);
        } else {
            for (Map.Entry<String, String> pairing : pairingResults.entrySet()) {
                String playerOne = pairing.getKey();
                String playerTwo = pairing.getValue();
                _tournamentService.addMatch(_tournamentId, _tournamentRound, playerOne, playerTwo);
                _currentlyPlayingPlayers.add(playerOne);
                _currentlyPlayingPlayers.add(playerTwo);
                createNewGame(tournamentCallback, playerOne, playerTwo, false);
            }

            for (String bye : byeResults) {
                _tournamentService.addRoundBye(_tournamentId, bye, _tournamentRound);
                addPlayerBye(bye);
            }
        }
    }

    private void addPlayerBye(String player) {
        Integer byes = _playerByes.get(player);
        if (byes == null)
            byes = 0;
        _playerByes.put(player, byes + 1);
    }

    private class PairPlayers implements TournamentTask {
        private long _taskStart = System.currentTimeMillis() + _waitForPairingsTime;

        @Override
        public void executeTask(TournamentCallback tournamentCallback, CollectionsManager collectionsManager) {
            doPairing(tournamentCallback, collectionsManager);
        }

        @Override
        public long getExecuteAfter() {
            return _taskStart;
        }
    }

    private class CreateMissingGames implements TournamentTask {
        private Map<String, String> _gamesToCreate;

        public CreateMissingGames(Map<String, String> gamesToCreate) {
            _gamesToCreate = gamesToCreate;
        }

        @Override
        public void executeTask(TournamentCallback tournamentCallback, CollectionsManager collectionsManager) {
            for (Map.Entry<String, String> pairings : _gamesToCreate.entrySet()) {
                String playerOne = pairings.getKey();
                String playerTwo = pairings.getValue();
                createNewGame(tournamentCallback, playerOne, playerTwo, false);
            }
        }

        @Override
        public long getExecuteAfter() {
            return 0;
        }
    }
}
