package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;
import com.gempukku.lotro.cards.sets.SetDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TengwarPackBox implements PackBox {
    private final List<CardCollection.Item> _cards = new ArrayList<>();

    public TengwarPackBox(String[] sets, LotroCardBlueprintLibrary library) {
        final Map<String,SetDefinition> setDefinitions = library.getSetDefinitions();
        for (String set : sets)
            for (String tengwarCard : setDefinitions.get(set).getTengwarCards())
                _cards.add(CardCollection.Item.createItem(tengwarCard, 1, true));
    }

    @Override
    public List<CardCollection.Item> openPack() {
        return Collections.unmodifiableList(_cards);
    }

    //Not used in non-random packs
    @Override
    public List<CardCollection.Item> openPack(int selection) { return openPack(); }

    @Override
    public List<String> GetAllOptions() {
        return _cards.stream().map(CardCollection.Item::getBlueprintId).toList();
    }
}
