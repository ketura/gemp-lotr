package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.competitive.StandingsProducer;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SingleEliminationTournament implements Tournament {
    private int _cost;
    private CollectionType _collectionType;
    private String _lotroFormat;
    private String _tournamentName;
    private Map<String, LotroDeck> _playerDecks;
    private TournamentService _tournamentService;
    private String _tournamentId;

    private Set<String> _allPlayers;
    private Set<String> _playersInContention;
    private Set<String> _droppedPlayers = new HashSet<String>();
    private int _currentRound;

    private int _ongoingGamesCount;

    private TournamentTask _nextTask;

    private ReadWriteLock _lock = new ReentrantReadWriteLock();

    private Map<String, Integer> _playerByes = new HashMap<String, Integer>();

    private List<PlayerStanding> _playerStandings;

    private List<TournamentMatch> _tournamentMatches = new ArrayList<TournamentMatch>();

    public SingleEliminationTournament(TournamentService tournamentService, String tournamentId, String parameters) {
        _tournamentService = tournamentService;

        String[] params = parameters.split(",");
        _cost = Integer.parseInt(params[0]);
        _lotroFormat = params[1];
        _collectionType = new CollectionType(params[2], params[3]);
        _tournamentName = params[4];

        _tournamentId = tournamentId;
        _playerDecks = new HashMap<String, LotroDeck>(_tournamentService.getPlayers(tournamentId));

        _allPlayers = new HashSet<String>(_playerDecks.keySet());

        _playersInContention = new HashSet<String>(_playerDecks.keySet());
        _playersInContention.removeAll(_tournamentService.getDroppedPlayers(tournamentId));

        int round = 1;
        while (true) {
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

    @Override
    public int getCurrentRound() {
        return _currentRound;
    }

    @Override
    public void advanceTournament(TournamentCallback tournamentCallback) {
        _lock.writeLock().lock();
        try {
            if (_nextTask == null) {
                if (_ongoingGamesCount == 0) {
                    if (_playersInContention.size() > 1)
                        _nextTask = new PairPlayers();
                    else
                        finishTournament(tournamentCallback);
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

    @Override
    public void dropPlayer(String player) {
        _lock.writeLock().lock();
        try {
            if (_playersInContention.contains(player))
                _droppedPlayers.add(player);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public void reportGameFinished(TournamentCallback tournamentCallback, String winner, String loser) {
        _lock.writeLock().lock();
        try {
            _ongoingGamesCount--;

            _tournamentService.dropPlayer(_tournamentId, loser);
            _tournamentService.setMatchResult(_tournamentId, _currentRound, winner);

            _playersInContention.remove(loser);
            _tournamentMatches.add(new TournamentMatch(winner, loser, winner, _currentRound));
            _playerStandings = null;
            if (_ongoingGamesCount == 0)
                processToNextTournamentRoundOrFinish(tournamentCallback);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    private void processToNextTournamentRoundOrFinish(TournamentCallback tournamentCallback) {
        if (_playersInContention.size() > 1) {
            tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " round has finished, next round will start in 2 minutes");
            _nextTask = new PairPlayers();
        } else {
            finishTournament(tournamentCallback);
        }
    }

    private void finishTournament(TournamentCallback tournamentCallback) {
        tournamentCallback.broadcastMessage("Tournament " + _tournamentName + " is finished");
        awardPrizes();
    }

    private void awardPrizes() {

    }

    @Override
    public boolean isFinished() {
        _lock.readLock().lock();
        try {
            return _playersInContention.size() < 2;
        } finally {
            _lock.readLock().unlock();
        }
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

    private class PairPlayers implements TournamentTask {
        private long _taskStart = System.currentTimeMillis() + 1000 * 60 * 2;

        @Override
        public void executeTask(TournamentCallback tournamentCallback) {
            for (String droppedPlayer : _droppedPlayers)
                _playersInContention.remove(droppedPlayer);
            _droppedPlayers.clear();

            List<String> playersRandomized = new ArrayList<String>(_playersInContention);
            Collections.shuffle(playersRandomized);
            if (playersRandomized.size() > 1) {
                _currentRound++;
                Iterator<String> playerIterator = playersRandomized.iterator();
                while (playerIterator.hasNext()) {
                    String playerOne = playerIterator.next();
                    if (playerIterator.hasNext()) {
                        String playerTwo = playerIterator.next();
                        _ongoingGamesCount++;
                        _tournamentService.addMatch(_tournamentId, _currentRound, playerOne, playerTwo);
                        createNewGame(tournamentCallback, playerOne, playerTwo);
                    } else {
                        Integer byes = addPlayerBye(playerOne);
                        _playerByes.put(playerOne, byes + 1);
                        _tournamentService.addMatch(_tournamentId, _currentRound, playerOne, "Bye");
                        _tournamentService.setMatchResult(_tournamentId, _currentRound, playerOne);
                        tournamentCallback.broadcastMessage("Player " + playerOne + " has received a bye");
                    }
                }
            } else {
                finishTournament(tournamentCallback);
            }
        }

        @Override
        public long getExecuteAfter() {
            return _taskStart;
        }
    }

    private void createNewGame(TournamentCallback tournamentCallback, String playerOne, String playerTwo) {
        tournamentCallback.createGame(new LotroGameParticipant(playerOne, _playerDecks.get(playerOne)),
                new LotroGameParticipant(playerTwo, _playerDecks.get(playerTwo)));
    }

    private Integer addPlayerBye(String player) {
        Integer byes = _playerByes.get(player);
        if (byes == null)
            byes = 0;
        return byes;
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
