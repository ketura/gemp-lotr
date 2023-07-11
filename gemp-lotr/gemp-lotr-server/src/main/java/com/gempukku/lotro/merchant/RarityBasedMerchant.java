package com.gempukku.lotro.merchant;

import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;
import com.gempukku.lotro.cards.sets.SetDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RarityBasedMerchant implements Merchant {
    private final Map<String, SetDefinition> _rarity = new HashMap<>();

    public RarityBasedMerchant(LotroCardBlueprintLibrary library) {
        for (SetDefinition setDefinition : library.getSetDefinitions().values()) {
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
        return switch (cardRarity) {
            case "C" -> 50;
            case "U", "S" -> 100;
            case "R", "P", "A" -> 1000;
            case "X" -> 2000;
            default -> throw new RuntimeException("Unknown rarity for priced card: " + cardRarity);
        };
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
