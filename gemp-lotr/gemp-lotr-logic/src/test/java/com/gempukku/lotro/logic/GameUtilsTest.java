package com.gempukku.lotro.logic;

import static org.junit.Assert.assertEquals;

import com.gempukku.lotro.rules.lotronly.LotroGameUtils;
import org.junit.Test;

public class GameUtilsTest {
    @Test
    public void testRegion() {
        assertEquals(1, LotroGameUtils.getRegion(1));
        assertEquals(1, LotroGameUtils.getRegion(2));
        assertEquals(1, LotroGameUtils.getRegion(3));
        assertEquals(2, LotroGameUtils.getRegion(4));
        assertEquals(2, LotroGameUtils.getRegion(5));
        assertEquals(2, LotroGameUtils.getRegion(6));
        assertEquals(3, LotroGameUtils.getRegion(7));
        assertEquals(3, LotroGameUtils.getRegion(8));
        assertEquals(3, LotroGameUtils.getRegion(9));
    }
}
