package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.game.adventure.DefaultAdventureLibrary;
import org.junit.Test;

public class LotroFormatLibraryTest {
    @Test
    public void testLoad() {
        LotroFormatLibrary library = new LotroFormatLibrary(new DefaultAdventureLibrary(), new CardBlueprintLibrary());
    }
}
