package com.gempukku.mtg;

import java.util.List;

public class SetCardData {
    private String _setName;
    private List<CardData> _allCards;

    public SetCardData(String setName, List<CardData> allCards) {
        _setName = setName;
        _allCards = allCards;
    }

    public List<CardData> getAllCards() {
        return _allCards;
    }

    public String getSetName() {
        return _setName;
    }
}
