package com.gempukku.lotro.common;

import org.junit.Test;

import static junit.framework.Assert.assertSame;

public class CultureTest {
    @Test
    public void urukHaiEnum() {
        Culture urukHai = Enum.valueOf(Culture.class, "URUK_HAI");
        assertSame(Culture.URUK_HAI, urukHai);
    }
}
