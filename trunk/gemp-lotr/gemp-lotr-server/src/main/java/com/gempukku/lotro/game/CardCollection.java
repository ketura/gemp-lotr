package com.gempukku.lotro.game;

import java.util.List;
import java.util.Map;

public interface CardCollection {
    public Map<String, Integer> getAll();

    public List<Item> getItems(String filter);

    public static class Item {
        public enum Type {
            PACK, CARD
        }

        private Type _type;
        private int _count;
        private String _blueprintId;
        private LotroCardBlueprint _cardBlueprint;

        public Item(Type type, int count, String blueprintId) {
            _type = type;
            _count = count;
            _blueprintId = blueprintId;
        }

        public Item(Type type, int count, String blueprintId, LotroCardBlueprint cardBlueprint) {
            _type = type;
            _count = count;
            _blueprintId = blueprintId;
            _cardBlueprint = cardBlueprint;
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

        public LotroCardBlueprint getCardBlueprint() {
            return _cardBlueprint;
        }
    }
}
