package com.gempukku.lotro.packs;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.game.CardCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TengwarPackBox implements PackBox {
    private List<CardCollection.Item> _cards = new ArrayList<CardCollection.Item>();

    public TengwarPackBox(int[] sets) {
        RarityReader rarityReader = new RarityReader();
        for (int set : sets)
            for (String tengwarCard : rarityReader.getSetRarity(String.valueOf(set)).getTengwarCards())
                _cards.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, tengwarCard));
    }

    @Override
    public List<CardCollection.Item> openPack() {
        return Collections.unmodifiableList(_cards);
    }
}
