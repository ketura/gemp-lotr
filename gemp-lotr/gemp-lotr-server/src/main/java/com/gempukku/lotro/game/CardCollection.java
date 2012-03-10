package com.gempukku.lotro.game;

import java.util.List;
import java.util.Map;

public interface CardCollection extends OwnershipCheck {
    public Map<String, Integer> getAll();

    public int getItemCount(String blueprintId);

    public List<CardCollection.Item> getAllItems();

    public static class Item implements CardItem {
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

        @Override
        public String getBlueprintId() {
            return _blueprintId;
        }
    }
}
