package com.gempukku.lotro.game;

import java.util.Map;

public interface CardCollection {
    public Map<String, Integer> getAll();

    public Map<String, Item> getItems(String filter);

    public static class Item {
        public enum Type {PACK, CARD}

        private Type _type;
        private int _count;

        public Item(Type type, int count) {
            _type = type;
            _count = count;
        }

        public Type getType() {
            return _type;
        }

        public int getCount() {
            return _count;
        }
    }
}
