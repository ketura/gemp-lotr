package com.gempukku.lotro.tournament;

import com.gempukku.lotro.PlayerStanding;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.util.DescComparator;
import com.gempukku.util.MultipleComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Tournament {
    private Comparator<PlayerStanding> STANDING_COMPARATOR =
            new MultipleComparator<PlayerStanding>(
                    new DescComparator<PlayerStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<PlayerStanding>(new OpponentsWinComparator()));

    private TournamentMatchDao _tournamentMatchDao;

    private Map<String, LotroDeck> _players;
    private Set<String>[] _droppedPlayers;

    private int _roundCount;
    // Current round
    private int _currentRound;
    private String _id;
    private LotroFormat _format;

    private Set<TournamentMatch>[] _finishedMatches;

    private Map<String, String> _currentRoundPairings;
    private Set<String> _playersWithByes;

    private TournamentTask _nextTournamentTask;

    private List<PlayerStanding> _currentStandings;

    public Tournament(TournamentMatchDao tournamentMatchDao, Map<String, LotroDeck> players, Set<String>[] droppedPlayers, int roundCount, int status, String id, LotroFormat format) {
        _tournamentMatchDao = tournamentMatchDao;
        _droppedPlayers = droppedPlayers;

        _roundCount = roundCount;
        _currentRound = status;
        _id = id;
        _format = format;

        _players = new ConcurrentHashMap<String, LotroDeck>(players);
        for (int i = 0; i < _currentRound; i++) {
            Set<String> allPlayers = new HashSet<String>(players.keySet());
            // Remove all players dropped so far
            for (int j = 0; j <= i; j++)
                allPlayers.removeAll(droppedPlayers[j]);

            // Fill the information about all the matches finished or scheduled so far
            for (TournamentMatch tournamentMatch : _tournamentMatchDao.getMatches(_id, i + 1)) {
                if (tournamentMatch.getWinner() != null)
                    _finishedMatches[i].add(tournamentMatch);
                else
                    _currentRoundPairings.put(tournamentMatch.getPlayerOne(), tournamentMatch.getPlayerTwo());
                // Removed playing (or played) players
                allPlayers.remove(tournamentMatch.getPlayerOne());
                allPlayers.remove(tournamentMatch.getPlayerTwo());
            }
            // Remaining players had a bye
            _playersWithByes.addAll(allPlayers);
        }

        if (_currentRoundPairings.isEmpty()) {
            _nextTournamentTask = new DelayedTournamentTask() {
                @Override
                public int executeTask(TournamentCallback tournamentCallback) {
                    return roundFinished(tournamentCallback);
                }
            };
        } else {
            _nextTournamentTask = new DelayedTournamentTask() {
                @Override
                public int executeTask(TournamentCallback tournamentCallback) {
                    tournamentCallback.createGames(_currentRoundPairings, _players);
                    return _currentRound;
                }
            };
        }
    }

    private int roundFinished(TournamentCallback tournamentCallback) {
        if (_currentRound < _roundCount) {
            _currentRound++;
            boolean success = tryCreatingNewPairings();
            if (success) {
                Map<String, String> currentRoundPairings = _currentRoundPairings;
                for (Map.Entry<String, String> pairing : currentRoundPairings.entrySet())
                    _tournamentMatchDao.addMatch(_id, _currentRound, pairing.getKey(), pairing.getValue());

                tournamentCallback.createGames(currentRoundPairings, _players);
                return _currentRound;
            } else {
                return distributePrizes(tournamentCallback);
            }
        } else if (_currentRound == _roundCount) {
            return distributePrizes(tournamentCallback);
        } else {
            return _currentRound;
        }
    }

    private boolean tryCreatingNewPairings() {
        return false;
    }

    private int distributePrizes(TournamentCallback tournamentCallback) {
        _currentRound = _roundCount + 1;

        // Distribute prizes

        return _currentRound;
    }

    public synchronized void dropPlayerBeforeRound(int round, String playerName) {
        if (_droppedPlayers[round] == null)
            _droppedPlayers[round] = new CopyOnWriteArraySet<String>();
        _droppedPlayers[round].add(playerName);
    }

    public synchronized void executePendingTasks(TournamentCallback tournamentCallback) {
        if (_nextTournamentTask != null && _nextTournamentTask.getExecuteAfter() < System.currentTimeMillis()) {
            TournamentTask task = _nextTournamentTask;
            _nextTournamentTask = null;
            task.executeTask(tournamentCallback);
        }
    }

    public List<PlayerStanding> getCurrentStandings() {
        List<PlayerStanding> result = _currentStandings;
        if (result != null)
            return result;

        synchronized (this) {
            if (_currentStandings == null)
                calculateCurrentStandings();

            return _currentStandings;
        }
    }

    private void calculateCurrentStandings() {
        Map<String, PlayerStanding> playersStandings = new HashMap<String, PlayerStanding>();
        for (String player : _players.keySet()) {
            int points = 0;
            int gamesPlayed = 0;
            for (int i = 0; i < _currentRound; i++) {
                if (_droppedPlayers[i] != null && _droppedPlayers[i].contains(player))
                    break;

                TournamentMatch match = getPlayerMatch(i, player);
                if (match != null) {
                    gamesPlayed++;
                    if (match.getWinner().equals(player))
                        points++;
                } else if (_currentRoundPairings.containsKey(player) || _currentRoundPairings.containsValue(player)) {
                    // Do nothing
                } else {
                    // Bye
                    points++;
                    gamesPlayed++;
                }
            }

            playersStandings.put(player, new PlayerStanding(player, points, gamesPlayed));
        }

        for (String player : _players.keySet()) {
            int opponentPoints = 0;
            int opponentGamesPlayed = 0;
            for (int i = 0; i < _currentRound; i++) {
                if (_droppedPlayers[i] != null && _droppedPlayers[i].contains(player))
                    break;
                TournamentMatch match = getPlayerMatch(i, player);
                if (match != null) {
                    String opponent;
                    if (match.getPlayerOne().equals(player))
                        opponent = match.getPlayerTwo();
                    else
                        opponent = match.getPlayerOne();
                    PlayerStanding opponentStanding = playersStandings.get(opponent);
                    opponentPoints += opponentStanding.getPoints();
                    opponentGamesPlayed += opponentStanding.getGamesPlayed();
                }
            }
            float opponentWinPerc = opponentPoints * 1f / opponentGamesPlayed;
            playersStandings.get(player).setOpponentWin(opponentWinPerc);
        }

        List<PlayerStanding> result = new ArrayList<PlayerStanding>(playersStandings.values());

        Collections.sort(result, STANDING_COMPARATOR);

        int standing = 0;
        int position = 1;
        PlayerStanding lastStanding = null;
        for (PlayerStanding playerStanding : result) {
            if (lastStanding == null || STANDING_COMPARATOR.compare(playerStanding, lastStanding) != 0)
                standing = position;
            playerStanding.setStanding(standing);
            position++;
            lastStanding = playerStanding;
        }

        _currentStandings = Collections.unmodifiableList(result);
    }

    private TournamentMatch getPlayerMatch(int roundIndex, String player) {
        for (TournamentMatch tournamentMatch : _finishedMatches[roundIndex]) {
            if (tournamentMatch.getPlayerOne().equals(player)
                    || tournamentMatch.getPlayerTwo().equals(player))
                return tournamentMatch;
        }
        return null;
    }

    private interface TournamentTask {
        public int executeTask(TournamentCallback tournamentCallback);

        public long getExecuteAfter();
    }

    private abstract class DelayedTournamentTask implements TournamentTask {
        private final long _time;

        private DelayedTournamentTask() {
            _time = System.currentTimeMillis();
        }

        @Override
        public final long getExecuteAfter() {
            return _time + 1000 * 60 * 2;
        }
    }


    private class PointsComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getPoints() - o2.getPoints();
        }
    }

    private class GamesPlayedComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getGamesPlayed() - o2.getGamesPlayed();
        }
    }

    private class OpponentsWinComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            final float diff = o1.getOpponentWin() - o2.getOpponentWin();
            if (diff < 0)
                return -1;
            if (diff > 0)
                return 1;
            return 0;
        }
    }
}
