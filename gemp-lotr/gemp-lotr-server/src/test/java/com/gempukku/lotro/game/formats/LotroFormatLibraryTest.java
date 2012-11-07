package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import org.junit.Test;

public class LotroFormatLibraryTest {
    @Test
    public void testLoad() {
        LotroFormatLibrary library = new LotroFormatLibrary(null);
    }
    
    @Test(expected = DeckInvalidException.class)
    public void legolasGreenleafNotLegalInStandard() throws DeckInvalidException {
        LotroFormatLibrary library = new LotroFormatLibrary(new LotroCardBlueprintLibrary());
        LotroFormat standard = library.getFormat("standard");
        standard.validateCard("1_50");
    }
}
