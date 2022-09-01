package com.gempukku.lotro.league;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import org.junit.Test;

public class LeaguePrizesTest extends AbstractAtTest {
    @Test
    public void test() {
        LeaguePrizes leaguePrizes = new FixedLeaguePrizes(_library);
        CardCollection prize = leaguePrizes.getPrizeForLeagueMatchWinner(2, 2);
        for (CardCollection.Item stringIntegerEntry : prize.getAll()) {
            System.out.println(stringIntegerEntry.getBlueprintId() + ": " + stringIntegerEntry.getCount());
        }
    }

    @Test
    public void testLeaguePrize() {
        LeaguePrizes leaguePrizes = new FixedLeaguePrizes(_library);
        for (int i = 1; i <= 32; i++) {
            System.out.println("Place "+i);
            CardCollection prize = leaguePrizes.getPrizeForLeague(i, 60, 1, 2, CollectionType.ALL_CARDS);
            if (prize != null)
                for (CardCollection.Item stringIntegerEntry : prize.getAll()) {
                    System.out.println(stringIntegerEntry.getBlueprintId() + ": " + stringIntegerEntry.getCount());
            }
        }
    }
}
