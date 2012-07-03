package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.competitive.StandingsProducer;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractTournament implements Tournament {
    protected TournamentService _tournamentService;
    private String _tournamentId;
    private String _lotroFormat;
    private CollectionType _collectionType;
    private String _tournamentName;

    private int _currentRound;
    private int _ongoingGamesCount;

    private TournamentTask _nextTask;

    private HashSet<String> _allPlayers;
    private HashMap<String, LotroDeck> _playerDecks;
    protected HashSet<String> _playersInContention;
    private Set<String> _droppedPlayers = new HashSet<String>();

    private Map<String, Integer> _playerByes = new HashMap<String, Integer>();
    private List<TournamentMatch> _tournamentMatches = new ArrayList<TournamentMatch>();

    private List<PlayerStanding> _playerStandings;

    protected ReadWriteLock _lock = new ReentrantReadWriteLock();

    public AbstractTournament(TournamentService tournamentService, String tournamentId, String format,
                              CollectionType collectionType, String tournamentName) {
        _tournamentService = tournamentService;
        _tournamentId = tournamentId;
        _lotroFormat = format;
        _collectionType = collectionType;
        _tournamentName = tournamentName;

        _playerDecks = new HashMap<String, LotroDeck>(_tournamentService.getPlayers(tournamentId));
        _allPlayers = new HashSet<String>(_playerDecks.keySet());

        _playersInContention = new HashSet<String>(_playerDecks.keySet());
        _playersInContention.removeAll(_tournamentService.getDroppedPlayers(tournamentId));

        int round = 1;
        while (!isFinished()) {
            List<TournamentMatch> matches = _tournamentService.getMatches(tournamentId, round);
            if (matches.size() > 0) {
                Map<String, String> gamesToCreate = new HashMap<String, String>();
                for (TournamentMatch match : matches) {
                    String winner = match.getWinner();
                    if (winner != null) {
                        if (match.isBye()) {
                            addPlayerBye(winner);
                        } else {
                            _tournamentMatches.add(match);
                        }
                    } else {
                        gamesToCreate.put(match.getPlayerOne(), match.getPlayerTwo());
                    }
                }
                _currentRound++;
                if (gamesToCreate.size() > 0) {
                    _nextTask = new CreateMissingGames(gamesToCreate);
                    break;
                }
            } else {
                _nextTask = new PairPlayers();
                break;
            }
        }
    }

    @Override
    public String getTournamentId() {
        return _tournamentId;
    }

    @Override
    public CollectionType getCollectionType() {
        return _collectionType;
    }

    @Override
    public String getFormat() {
        return _lotroFormat;
    }

    @Override
    public String getTournamentName() {
        return _tournamentName;
    }

    @Override
    public void advanceTournament(TournamentCallback tournamentCallback) {
        _lock.writeLock().lock();
        try {
            if (_nextTask == null) {
                if (_ongoingGamesCount == 0) {
                    if (isFinished())
                        finishTournament(tournamentCallback);
                    else
                        _nextTask = new PairPlayers();
                }
            }
            if (_nextTask != null && _nextTask.getExecuteAfter() < System.currentTimeMillis()) {
                TournamentTask task = _nextTask;
                _nextTask = null;
                task.executeTask(tournamentCallback);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    private void addPlayerBye(String player) {
        Integer byes = _playerByes.get(player);
        if (byes == null)
            byes = 0;
        _playerByes.put(player, byes + 1);
    }

    @Override
    public void dropPlayer(String player) {
        _lock.writeLock().lock();
        try {
            if (_playersInContention.contains(player)) {
                if (_droppedPlayers.add(player))
                    _tournamentService.dropPlayer(_tournamentId, player);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public int getCurrentRound() {
        return _currentRound;
    }

    @Override
    public void reportGameFinished(TournamentCallback tournamentCallback, String winner, String loser) {
        _lock.writeLock().lock();
        try {
            _ongoingGamesCount--;

            _tournamentService.setMatchResult(_tournamentId, _currentRound, winner);

            _tournamentMatches.add(new TournamentMatch(winner, loser, winner, _currentRound));
            _playerStandings = null;
            if (_ongoingGamesCount == 0)
                processToNextTournamentRoundOrFinish(tournamentCallback);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    private void processToNextTournamentRoundOrFinish(TournamentCallback tournamentCallback) {
        if (isFinished()) {
            finishTournament(tournamentCallback);
        } else {
            tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " round has finished, next round will start in 2 minutes");
            _nextTask = new PairPlayers();
        }
    }

    private void finishTournament(TournamentCallback tournamentCallback) {
        tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " is finished");
        awardPrizes();
    }

    private void awardPrizes() {

    }

    @Override
    public boolean isPlayerCompeting(String player) {
        _lock.readLock().lock();
        try {
            return _playersInContention.contains(player);
        } finally {
            _lock.readLock().unlock();
        }
    }

    @Override
    public List<PlayerStanding> getCurrentStandings() {
        List<PlayerStanding> standings = _playerStandings;
        if (standings != null)
            return standings;

        _lock.readLock().lock();
        try {
            _playerStandings = StandingsProducer.produceStandings(_allPlayers, _tournamentMatches, 1, 0, _playerByes);
            return _playerStandings;
        } finally {
            _lock.readLock().unlock();
        }
    }

    private void createNewGame(TournamentCallback tournamentCallback, String playerOne, String playerTwo) {
        tournamentCallback.createGame(new LotroGameParticipant(playerOne, _playerDecks.get(playerOne)),
                new LotroGameParticipant(playerTwo, _playerDecks.get(playerTwo)));
    }

    private class PairPlayers implements TournamentTask {
        private long _taskStart = System.currentTimeMillis() + 1000 * 60 * 2;

        @Override
        public void executeTask(TournamentCallback tournamentCallback) {
            _playersInContention.removeAll(_droppedPlayers);
            _droppedPlayers.clear();

            if (isFinished()) {
                finishTournament(tournamentCallback);
            } else {
                _currentRound++;
                pairPlayers(tournamentCallback);
            }
        }

        @Override
        public long getExecuteAfter() {
            return _taskStart;
        }
    }

    protected abstract void pairPlayers(TournamentCallback tournamentCallback);

    protected void pairNewGame(TournamentCallback tournamentCallback, String playerOne, String playerTwo) {
        _ongoingGamesCount++;
        _tournamentService.addMatch(_tournamentId, _currentRound, playerOne, playerTwo);
        createNewGame(tournamentCallback, playerOne, playerTwo);
    }

    protected void awardNewBye(TournamentCallback tournamentCallback, String player) {
        addPlayerBye(player);
        _tournamentService.addMatch(_tournamentId, _currentRound, player, "Bye");
        _tournamentService.setMatchResult(_tournamentId, _currentRound, player);
        tournamentCallback.broadcastMessage("Player " + player + " has received a bye");
    }

    private class CreateMissingGames implements TournamentTask {
        private Map<String, String> _gamesToCreate;

        public CreateMissingGames(Map<String, String> gamesToCreate) {
            _gamesToCreate = gamesToCreate;
        }

        @Override
        public void executeTask(TournamentCallback tournamentCallback) {
            for (Map.Entry<String, String> pairings : _gamesToCreate.entrySet()) {
                String playerOne = pairings.getKey();
                String playerTwo = pairings.getValue();
                _ongoingGamesCount++;
                createNewGame(tournamentCallback, playerOne, playerTwo);
            }
        }

        @Override
        public long getExecuteAfter() {
            return 0;
        }
    }
}
