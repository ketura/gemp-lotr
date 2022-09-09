package com.gempukku.lotro.league;

import com.gempukku.lotro.at.AbstractAtTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConstructedLeagueDataTest extends AbstractAtTest {
    @Test
    public void testParameters() {
        ConstructedLeagueData leagueData = new ConstructedLeagueData(_cardLibrary, _formatLibrary, "20120312,fotr_block,0.7,default,All cards,7,10,3,fotr1_block,fotr_block,fotr2_block,fotr_block,fotr_block,fotr_block");
        final List<LeagueSerieData> series = leagueData.getSeries();
        assertEquals(3, series.size());
        assertEquals(20120312, series.get(0).getStart());
        assertEquals(20120318, series.get(0).getEnd());
        assertEquals("fotr1_block", series.get(0).getFormat().getCode());
        assertEquals(20120319, series.get(1).getStart());
        assertEquals(20120325, series.get(1).getEnd());
        assertEquals("fotr2_block", series.get(1).getFormat().getCode());
        assertEquals(20120326, series.get(2).getStart());
        assertEquals(20120401, series.get(2).getEnd());
        assertEquals("fotr_block", series.get(2).getFormat().getCode());
    }
}
