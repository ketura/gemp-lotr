package com.gempukku.lotro.merchant;

import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.game.packs.SetDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RarityBasedMerchant implements Merchant {
    private Map<String, SetDefinition> _rarity = new HashMap<String, SetDefinition>();

    public RarityBasedMerchant(CardSets cardSets) {
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("merchantable"))
                _rarity.put(setDefinition.getSetId(), setDefinition);
        }
    }

    @Override
    public Integer getCardSellPrice(String blueprintId, Date currentTime) {
        boolean foil = false;
        if (blueprintId.endsWith("*")) {
            foil = true;
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        }
        boolean tengwar = false;
        if (blueprintId.endsWith("T")) {
            tengwar = true;
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        }

        if (foil || tengwar)
            return null;

        final Integer cardBasePrice = getCardBasePrice(blueprintId);
        if (cardBasePrice == null)
            return null;

        return cardBasePrice;
    }

    @Override
    public Integer getCardBuyPrice(String blueprintId, Date currentTime) {
        boolean foil = false;
        if (blueprintId.endsWith("*")) {
            foil = true;
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        }
        boolean tengwar = false;
        if (blueprintId.endsWith("T")) {
            tengwar = true;
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        }

        Integer cardBasePrice = getCardBasePrice(blueprintId);
        if (cardBasePrice == null)
            return null;

        if (tengwar)
            cardBasePrice *= 2;
        if (foil)
            cardBasePrice *= 4;

        return cardBasePrice / 4;
    }

    private Integer getCardBasePrice(String blueprintId) {
        final int underscoreIndex = blueprintId.indexOf("_");
        if (underscoreIndex<0)
            return null;
        SetDefinition rarity = _rarity.get(blueprintId.substring(0, underscoreIndex));
        if (rarity == null)
            return null;
        String cardRarity = rarity.getCardRarity(blueprintId);
        if (cardRarity.equals("C"))
            return 50;
        else if (cardRarity.equals("U") || cardRarity.equals("S"))
            return 100;
        else if (cardRarity.equals("R") || cardRarity.equals("P") || cardRarity.equals("A"))
            return 1000;
        else if (cardRarity.equals("X"))
            return 2000;
        else
            throw new RuntimeException("Unknown rarity for priced card: " + cardRarity);
    }

    @Override
    public void cardBought(String blueprintId, Date currentTime, int price) {
        // NO-OP
    }

    @Override
    public void cardSold(String blueprintId, Date currentTime, int price) {
        // NO-OP
    }
}
