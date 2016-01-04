package com.gempukku.mtg;

public class CardData {
    private final String _id;
    private final String _name;
    private final int _price;

    public CardData(String id, String name, int price) {
        _id = id;
        _name = name;
        _price = price;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getPrice() {
        return _price;
    }
}
