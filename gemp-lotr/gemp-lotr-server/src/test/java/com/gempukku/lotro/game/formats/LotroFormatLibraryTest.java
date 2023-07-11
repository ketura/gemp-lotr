package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.adventure.DefaultAdventureLibrary;
import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;
import org.junit.Test;

public class LotroFormatLibraryTest {
    @Test
    public void testLoad() {
        LotroFormatLibrary library = new LotroFormatLibrary(new DefaultAdventureLibrary(), new LotroCardBlueprintLibrary());
    }
}
