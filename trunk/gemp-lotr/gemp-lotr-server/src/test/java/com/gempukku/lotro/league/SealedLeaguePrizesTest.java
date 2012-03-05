package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;
import org.junit.Test;

import java.util.Map;

public class SealedLeaguePrizesTest {
    @Test
    public void test() {
        SealedLeaguePrizes leaguePrizes = new SealedLeaguePrizes();
        CardCollection prize = leaguePrizes.getPrizeForLeagueMatchWinner(2, 2, "fotr_block");
        for (Map.Entry<String, Integer> stringIntegerEntry : prize.getAll().entrySet()) {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue());
        }
    }
}
