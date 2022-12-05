package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.DefaultAdventureLibrary;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import org.junit.Test;

public class LotroFormatLibraryTest {
    @Test
    public void testLoad() {
        LotroFormatLibrary library = new LotroFormatLibrary(new DefaultAdventureLibrary(), new LotroCardBlueprintLibrary());
    }
}
