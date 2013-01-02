package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;

import java.util.*;

public class SwissPairingMechanism implements PairingMechanism {
    @Override
    public String getPlayOffSystem() {
        return "Swiss";
    }

    @Override
    public boolean shouldDropLoser() {
        return false;
    }

    @Override
    public boolean isFinished(int round, Set<String> players, Set<String> droppedPlayers) {
        return round >= getRoundCountBasedOnNumberOfPlayers(players.size());
    }

    @Override
    public boolean pairPlayers(int round, Set<String> players, Set<String> droppedPlayers, Map<String, Integer> playerByes, List<PlayerStanding> currentStandings,
                               Map<String, Set<String>> previouslyPaired, Map<String, String> pairingResults, Set<String> byeResults) {
        int maxNumberOfPoints = determineMaximumNumberOfPoints(droppedPlayers, currentStandings);

        List<List<String>> playersGroupedByBracket = groupPlayersByPointBracket(droppedPlayers, currentStandings, maxNumberOfPoints);

        shufflePlayersWithinBrackets(playersGroupedByBracket);

        Set<String> playersWithByes = getPlayersWithByes(playerByes);

        boolean success = tryPairBracketAndFurther(0, new HashSet<String>(), playersGroupedByBracket, playersWithByes, previouslyPaired, pairingResults, byeResults);
        // Managed to pair with this carry over count - proceed with the pairings
        if (success)
            return false;

        // We can't pair, just finish the tournament
        return true;
    }

    private boolean tryPairBracketAndFurther(int bracketIndex, Set<String> carryOverPlayers, List<List<String>> playersGroupedByBracket, Set<String> playersWithByes,
                                             Map<String, Set<String>> previouslyPaired, Map<String, String> pairingsResult, Set<String> byes) {
        for (int carryOverMax = 0; carryOverMax < 4; carryOverMax++) {
            boolean success = tryPairBracketWithCarryOverMax(bracketIndex, carryOverMax, playersGroupedByBracket, playersWithByes, previouslyPaired, pairingsResult, pairingsResult, byes);
            if (success)
                return true;
        }
        return false;
    }

    private boolean tryPairBracketWithCarryOverMax(int bracketIndex, int carryOverMax, List<List<String>> playersGroupedByPoints, Set<String> playersWithByes,
                                                   Map<String, Set<String>> previouslyPaired, Map<String, String> pairingsResult,
                                                   Map<String, String> pairingsResult1, Set<String> byes) {
        // TODO
        return true;
    }

    private Set<String> getPlayersWithByes(Map<String, Integer> playerByes) {
        Set<String> playersWithByes = new HashSet<String>();
        for (Map.Entry<String, Integer> playerByeCount : playerByes.entrySet()) {
            if (playerByeCount.getValue() != null && playerByeCount.getValue() > 0)
                playersWithByes.add(playerByeCount.getKey());
        }
        return playersWithByes;
    }

    private void shufflePlayersWithinBrackets(List<List<String>> playersGroupedByPoints) {
        for (List<String> playersByPoint : playersGroupedByPoints)
            Collections.shuffle(playersByPoint);
    }

    private List<List<String>> groupPlayersByPointBracket(Set<String> droppedPlayers, List<PlayerStanding> currentStandings, int maxNumberOfPoints) {
        List<String>[] playersByPoints = new List[maxNumberOfPoints + 1];
        for (PlayerStanding currentStanding : currentStandings) {
            String playerName = currentStanding.getPlayerName();
            if (!droppedPlayers.contains(playerName)) {
                int points = currentStanding.getPoints();
                List<String> playersByPoint = playersByPoints[maxNumberOfPoints - points];
                if (playersByPoint == null) {
                    playersByPoint = new ArrayList<String>();
                    playersByPoints[maxNumberOfPoints - points] = playersByPoint;
                }
                playersByPoint.add(playerName);
            }
        }

        List<List<String>> result = new ArrayList<List<String>>();
        for (List<String> playersByPoint : playersByPoints) {
            if (playersByPoint != null)
                result.add(playersByPoint);
        }

        return result;
    }

    private int determineMaximumNumberOfPoints(Set<String> droppedPlayers, List<PlayerStanding> currentStandings) {
        int maxNumberOfPoints = 0;
        for (PlayerStanding currentStanding : currentStandings) {
            if (!droppedPlayers.contains(currentStanding.getPlayerName()))
                maxNumberOfPoints = Math.max(currentStanding.getPoints(), maxNumberOfPoints);
        }
        return maxNumberOfPoints;
    }

    private static int getRoundCountBasedOnNumberOfPlayers(int numberOfPlayers) {
        return (int) (Math.ceil(Math.log(numberOfPlayers) / Math.log(2))) + 1;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 128; i++) {
            System.out.println("i=" + i + " - " + getRoundCountBasedOnNumberOfPlayers(i));
        }
    }
}
