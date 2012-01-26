package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.packs.SetRarity;

import java.util.List;
import java.util.Map;

public interface CardCollection extends OwnershipCheck {
    public Map<String, Integer> getAll();

    public int getItemCount(String blueprintId);

    public List<Item> getItems(String filter, LotroCardBlueprintLibrary library, Map<String, SetRarity> rarities);

    public static class Item {
        public enum Type {
            PACK, CARD, SELECTION
        }

        private Type _type;
        private int _count;
        private String _blueprintId;

        public Item(Type type, int count, String blueprintId) {
            _type = type;
            _count = count;
            _blueprintId = blueprintId;
        }

        public Type getType() {
            return _type;
        }

        public int getCount() {
            return _count;
        }

        public String getBlueprintId() {
            return _blueprintId;
        }
    }
}
