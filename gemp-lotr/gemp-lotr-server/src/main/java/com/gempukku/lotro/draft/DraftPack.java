package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;

import java.util.List;

public class DraftPack {
    public CardCollection _fixedCollection;
    public List<String> _packs;

    public DraftPack(CardCollection fixedCollection, List<String> packs) {
        _fixedCollection = fixedCollection;
        _packs = packs;
    }

    public CardCollection getFixedCollection() {
        return _fixedCollection;
    }

    public List<String> getPacks() {
        return _packs;
    }
}
