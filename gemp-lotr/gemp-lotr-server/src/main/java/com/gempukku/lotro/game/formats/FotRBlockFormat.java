package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class FotRBlockFormat extends DefaultLotroFormat {
    public FotRBlockFormat(LotroCardBlueprintLibrary library) {
        super(library, "Fellowship block", Block.FELLOWSHIP, true, 60, 4, true, true);
        addRestrictedCard("1_248");
        addValidSet(1);
        addValidSet(2);
        addValidSet(3);
    }
}
