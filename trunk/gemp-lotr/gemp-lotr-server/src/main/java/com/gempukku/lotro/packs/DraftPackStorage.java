package com.gempukku.lotro.packs;

import com.gempukku.lotro.draft.DraftPack;

import java.util.HashMap;
import java.util.Map;

public class DraftPackStorage {
    private Map<String, DraftPack> _draftPacksByType = new HashMap<String, DraftPack>();

    public void addDraftPack(String draftType, DraftPack draftPack) {
        _draftPacksByType.put(draftType, draftPack);
    }

    public DraftPack getDraftPack(String draftType) {
        return _draftPacksByType.get(draftType);
    }
}
