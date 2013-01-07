package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;
import org.junit.Test;

import java.util.Map;

public class LeaguePrizesTest {
    @Test
    public void test() {
        LeaguePrizes leaguePrizes = new NewLeaguePrizes();
        CardCollection prize = leaguePrizes.getPrizeForLeagueMatchWinner(2, 2);
        for (Map.Entry<String, CardCollection.Item> stringIntegerEntry : prize.getAll().entrySet()) {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue().getCount());
        }
    }

    @Test
    public void testLeaguePrize() {
        LeaguePrizes leaguePrizes = new NewLeaguePrizes();
        for (int i = 1; i <= 32; i++) {
            System.out.println("Place "+i);
            CardCollection prize = leaguePrizes.getPrizeForLeague(i, 60, 1, 2, 2f);
            if (prize != null)
            for (Map.Entry<String, CardCollection.Item> stringIntegerEntry : prize.getAll().entrySet()) {
                System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue().getCount());
            }
        }
    }
}
