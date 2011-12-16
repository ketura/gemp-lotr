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
        int starter = _random.nextInt(6);
        if (starter == 0) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "FotR - Gandalf Starter"));
        } else if (starter == 1) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "FotR - Aragorn Starter"));
        } else if (starter == 2) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "MoM - Gandalf Starter"));
        } else if (starter == 3) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "MoM - Gimli Starter"));
        } else if (starter == 4) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "RotEL - Boromir Starter"));
        } else if (starter == 5) {
            result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 1, "RotEL - Legolas Starter"));
        }
        result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 2, "FotR - Booster"));
        result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 2, "MoM - Booster"));
        result.add(new CardCollection.Item(CardCollection.Item.Type.PACK, 2, "RotEL - Booster"));

        // Arwen
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "3_7"));
        // Boromir
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_97"));
        // Gimli
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_12"));
        // Legolas
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_51"));
        // Merry
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_303"));
        // Pippin
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_306"));
        // Sam
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "1_311"));

        // The Balrog
        result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, "0_10"));

        return result;
    }
}
