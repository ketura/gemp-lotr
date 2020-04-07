package com.gempukku.mtg;

import java.util.List;

public class TimestampedCardCollection {
    private final long _updateDate;
    private final List<SetCardData> _setCardDataList;

    public TimestampedCardCollection(long updateDate, List<SetCardData> setCardDataList) {
        _updateDate = updateDate;
        _setCardDataList = setCardDataList;
    }

    public List<SetCardData> getSetCardDataList() {
        return _setCardDataList;
    }

    public long getUpdateDate() {
        return _updateDate;
    }
}
