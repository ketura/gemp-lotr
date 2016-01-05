package com.gempukku.mtg;

import java.util.List;

public class SetCardData {
    private String _setName;
    private String _setLink;
    private List<CardData> _allCards;

    public SetCardData(String setName, String setLink, List<CardData> allCards) {
        _setName = setName;
        _setLink = setLink;
        _allCards = allCards;
    }

    public List<CardData> getAllCards() {
        return _allCards;
    }

    public String getSetName() {
        return _setName;
    }

    public String getSetLink() {
        return _setLink;
    }
}
