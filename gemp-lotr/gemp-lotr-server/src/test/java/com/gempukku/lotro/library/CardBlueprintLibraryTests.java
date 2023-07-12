package com.gempukku.lotro.library;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.game.rules.GameUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CardBlueprintLibraryTests extends AbstractAtTest {
    @Test
    public void LibraryLoadsWithNoDuplicates() throws CardNotFoundException {
        Map<String, String> cardNames = new HashMap<>();
        for (int i = 0; i <= 19; i++) {
            for (int j = 1; j <= 365; j++) {
                String blueprintId = i + "_" + j;
                try {
                    if (blueprintId.equals(_cardLibrary.getBaseBlueprintId(blueprintId))) {
                        try {
                            LotroCardBlueprint cardBlueprint = _cardLibrary.getLotroCardBlueprint(blueprintId);
                            String cardName = GameUtils.getFullName(cardBlueprint);
                            if (cardNames.containsKey(cardName) && cardBlueprint.getCardType() != CardType.SITE)
                                System.out.println("Multiple detected - " + cardName + ": " + cardNames.get(cardName) + " and " + blueprintId);
                            else
                                cardNames.put(cardName, blueprintId);
                        }
                        catch(CardNotFoundException ex) {
                            break;
                        }
                    }
                } catch (IllegalArgumentException exp) {
                    //exp.printStackTrace();
                }
            }
        }

    }
}
