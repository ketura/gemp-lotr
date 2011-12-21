package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class TTTBlockFormat extends DefaultLotroFormat {
    public TTTBlockFormat(LotroCardBlueprintLibrary library, boolean mulliganRule) {
        super(library, (mulliganRule ? "Community " : "") + "Two Towers block", Block.TWO_TOWERS, true, 60, 4, mulliganRule, true);
        addValidSet(4);
        addValidSet(5);
        addValidSet(6);
    }
}
