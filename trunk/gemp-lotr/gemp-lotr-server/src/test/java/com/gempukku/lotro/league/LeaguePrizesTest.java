package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;
import org.junit.Test;

import java.util.Map;

public class LeaguePrizesTest {
    @Test
    public void test() {
        LeaguePrizes leaguePrizes = new NewLeaguePrizes();
        CardCollection prize = leaguePrizes.getPrizeForLeagueMatchWinner(2, 2);
        for (Map.Entry<String, Integer> stringIntegerEntry : prize.getAll().entrySet()) {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue());
        }
    }

    @Test
    public void testLeaguePrize() {
        LeaguePrizes leaguePrizes = new NewLeaguePrizes();
        CardCollection prize = leaguePrizes.getPrizeForLeague(1, 100, 2f);
        for (Map.Entry<String, Integer> stringIntegerEntry : prize.getAll().entrySet()) {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue());
        }
    }
}
