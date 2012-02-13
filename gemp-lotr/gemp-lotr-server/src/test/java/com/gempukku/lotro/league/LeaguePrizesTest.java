package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.CardCollection;
import org.junit.Test;

import java.util.Map;

public class LeaguePrizesTest {
    @Test
    public void test() {
        LeaguePrizes leaguePrizes = new LeaguePrizes();
        League league = new League(1, "a", "bla", 1, 1);
        LeagueSerie leagueSerie = new LeagueSerie("a", "b", "fotr_block", null, 0, 0, 0, 0);
        CardCollection prize = leaguePrizes.getPrizeForLeagueMatchWinner(2, 2, league, leagueSerie);
        for (Map.Entry<String, Integer> stringIntegerEntry : prize.getAll().entrySet()) {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue());
        }
    }
}
