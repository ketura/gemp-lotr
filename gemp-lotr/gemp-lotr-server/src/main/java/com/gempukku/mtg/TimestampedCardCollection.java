package com.gempukku.mtg;

import java.util.List;

public class TimestampedCardCollection {
    private final String _updateMarker;
    private final List<SetCardData> _setCardDataList;

    public TimestampedCardCollection(String updateMarker, List<SetCardData> setCardDataList) {
        _updateMarker = updateMarker;
        _setCardDataList = setCardDataList;
    }

    public List<SetCardData> getSetCardDataList() {
        return _setCardDataList;
    }

    public String getUpdateMarker() {
        return _updateMarker;
    }
}
