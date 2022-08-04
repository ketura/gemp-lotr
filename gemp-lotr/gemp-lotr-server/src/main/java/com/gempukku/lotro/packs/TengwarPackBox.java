package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.game.packs.SetDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TengwarPackBox implements PackBox {
    private final List<CardCollection.Item> _cards = new ArrayList<>();

    public TengwarPackBox(int[] sets, CardSets cardSets) {
        final Map<String,SetDefinition> setDefinitions = cardSets.getSetDefinitions();
        for (int set : sets)
            for (String tengwarCard : setDefinitions.get(String.valueOf(set)).getTengwarCards())
                _cards.add(CardCollection.Item.createItem(tengwarCard, 1));
    }

    @Override
    public List<CardCollection.Item> openPack() {
        return Collections.unmodifiableList(_cards);
    }
}
