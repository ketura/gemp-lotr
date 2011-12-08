package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LeagueStarterBox implements PackBox {
    private Random _random = new Random();

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        if (_random.nextBoolean()) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "FotR - Gandalf Starter"));
        } else {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "FotR - Aragorn Starter"));
        }
        result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 2, "FotR - Booster"));
        return result;
    }
}
