package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CompleteCardCollection implements CardCollection {

    private final LotroCardBlueprintLibrary _library;

    public CompleteCardCollection(LotroCardBlueprintLibrary library) {
        _library = library;
    }
    @Override
    public int getCurrency() {
        return 0;
    }

    @Override
    public Iterable<Item> getAll() {
        return _library.getBaseCards().entrySet().stream().map(cardBlueprintEntry -> {
            String blueprintId = cardBlueprintEntry.getKey();
            int count = getCount(cardBlueprintEntry.getValue());
            return Item.createItem(blueprintId, count);
        }).collect(Collectors.toList());
    }

    @Override
    public int getItemCount(String blueprintId) {
        final String baseBlueprintId = _library.getBaseBlueprintId(blueprintId);
        if (baseBlueprintId.equals(blueprintId)) {
            try {
                return getCount(_library.getLotroCardBlueprint(blueprintId));
            } catch (CardNotFoundException exp) {
                return 0;
            }
        }
        return 0;
    }

    private int getCount(LotroCardBlueprint blueprint) {
        final CardType cardType = blueprint.getCardType();
        if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
            return 1;
        return 4;
    }

    @Override
    public Map<String, Object> getExtraInformation() {
        return Collections.emptyMap();
    }
}
