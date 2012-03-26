package com.gempukku.lotro.logic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GameUtilsTest {
    @Test
    public void testRegion() {
        assertEquals(1, GameUtils.getRegion(1));
        assertEquals(1, GameUtils.getRegion(2));
        assertEquals(1, GameUtils.getRegion(3));
        assertEquals(2, GameUtils.getRegion(4));
        assertEquals(2, GameUtils.getRegion(5));
        assertEquals(2, GameUtils.getRegion(6));
        assertEquals(3, GameUtils.getRegion(7));
        assertEquals(3, GameUtils.getRegion(8));
        assertEquals(3, GameUtils.getRegion(9));
    }
}
