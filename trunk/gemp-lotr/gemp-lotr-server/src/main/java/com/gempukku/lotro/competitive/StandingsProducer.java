package com.gempukku.lotro.competitive;

import com.gempukku.util.DescComparator;
import com.gempukku.util.MultipleComparator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StandingsProducer {
    private static Comparator<PlayerStanding> LEAGUE_STANDING_COMPARATOR =
            new MultipleComparator<PlayerStanding>(
                    new DescComparator<PlayerStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<PlayerStanding>(new OpponentsWinComparator()));


    public static List<PlayerStanding> produceStandings(Collection<String> participants, Collection<? extends CompetitiveMatchResult> matches,
                                                        int pointsForWin, int pointsForLoss, Set<String> playersWithByes) {
        Map<String, List<String>> playerOpponents = new HashMap<String, List<String>>();
        Map<String, AtomicInteger> playerWinCounts = new HashMap<String, AtomicInteger>();
        Map<String, AtomicInteger> playerLossCounts = new HashMap<String, AtomicInteger>();

        // Initialize the list
        for (String playerName : participants) {
            playerOpponents.put(playerName, new ArrayList<String>());
            playerWinCounts.put(playerName, new AtomicInteger(0));
            playerLossCounts.put(playerName, new AtomicInteger(0));
        }

        for (CompetitiveMatchResult leagueMatch : matches) {
            playerOpponents.get(leagueMatch.getWinner()).add(leagueMatch.getLoser());
            playerOpponents.get(leagueMatch.getLoser()).add(leagueMatch.getWinner());
            playerWinCounts.get(leagueMatch.getWinner()).incrementAndGet();
            playerLossCounts.get(leagueMatch.getLoser()).incrementAndGet();
        }

        List<PlayerStanding> leagueStandings = new LinkedList<PlayerStanding>();
        for (String playerName : participants) {
            int points = playerWinCounts.get(playerName).intValue() * pointsForWin + playerLossCounts.get(playerName).intValue() * pointsForLoss;
            int gamesPlayed = playerWinCounts.get(playerName).intValue() + playerLossCounts.get(playerName).intValue();

            if (playersWithByes.contains(playerName))
                points += pointsForWin;

            PlayerStanding standing = new PlayerStanding(playerName, points, gamesPlayed);
            List<String> opponents = playerOpponents.get(playerName);
            int opponentWins = 0;
            int opponentGames = 0;
            for (String opponent : opponents) {
                opponentWins += playerWinCounts.get(opponent).intValue();
                opponentGames += playerWinCounts.get(opponent).intValue() + playerLossCounts.get(opponent).intValue();
            }
            if (opponentGames != 0)
                standing.setOpponentWin(opponentWins * 1f / opponentGames);
            else
                standing.setOpponentWin(0f);
            leagueStandings.add(standing);
        }

        Collections.sort(leagueStandings, LEAGUE_STANDING_COMPARATOR);

        int standing = 0;
        int position = 1;
        PlayerStanding lastStanding = null;
        for (PlayerStanding leagueStanding : leagueStandings) {
            if (lastStanding == null || LEAGUE_STANDING_COMPARATOR.compare(leagueStanding, lastStanding) != 0)
                standing = position;
            leagueStanding.setStanding(standing);
            position++;
            lastStanding = leagueStanding;
        }
        return leagueStandings;

    }

    private static class PointsComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getPoints() - o2.getPoints();
        }
    }

    private static class GamesPlayedComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getGamesPlayed() - o2.getGamesPlayed();
        }
    }

    private static class OpponentsWinComparator implements Comparator<PlayerStanding> {
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
