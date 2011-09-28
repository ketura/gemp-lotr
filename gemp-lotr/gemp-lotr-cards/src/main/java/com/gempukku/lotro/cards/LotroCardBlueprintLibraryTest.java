package com.gempukku.lotro.cards;

import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

import java.util.HashMap;
import java.util.Map;

public class LotroCardBlueprintLibraryTest {
    public static void main(String[] args) {
        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        Map<String, String> cardNames = new HashMap<String, String>();
        for (int i = 1; i <= 1; i++) {
            for (int j = 1; j <= 365; j++) {
                String blueprintId = i + "_" + j;
                try {
                    LotroCardBlueprint cardBlueprint = library.getLotroCardBlueprint(blueprintId);
                    String cardName = cardBlueprint.getName();
                    if (cardNames.containsKey(cardName))
                        System.out.println("Multiple detected - " + cardName + ": " + cardNames.get(cardName) + " and " + blueprintId);
                    else
                        cardNames.put(cardName, blueprintId);
                } catch (IllegalArgumentException exp) {
                    exp.printStackTrace();
                }
            }
        }

    }
}
