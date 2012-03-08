package com.gempukku.lotro.league;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConstructedLeagueDataTest {
    @Test
    public void testParameters() {
        ConstructedLeagueData leagueData = new ConstructedLeagueData("20120312,fotr_block,0.7,default,All cards,7,10,3,fotr1_block,fotr_block,fotr2_block,fotr_block,fotr_block,fotr_block");
        final List<LeagueSerieData> series = leagueData.getSeries();
        assertTrue(series.size() == 3);
        assertTrue(series.get(0).getStart() == 20120312);
        assertTrue(series.get(0).getEnd() == 20120318);
        assertEquals("fotr1_block", series.get(0).getFormat());
        assertTrue(series.get(1).getStart() == 20120319);
        assertTrue(series.get(1).getEnd() == 20120325);
        assertEquals("fotr2_block", series.get(1).getFormat());
        assertTrue(series.get(2).getStart() == 20120326);
        assertTrue(series.get(2).getEnd() == 20120401);
        assertEquals("fotr_block", series.get(2).getFormat());
    }
}
