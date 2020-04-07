package com.gempukku.mtg;

public class CardData {
    private final String _id;
    private final String _name;
    private final int _price;
    private final String _link;

    public CardData(String id, String name, int price, String link) {
        _name = name;
        _id = id;
        _link = link;
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

    public String getLink() {
        return _link;
    }
}
