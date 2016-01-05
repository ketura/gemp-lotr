package com.gempukku.mtg;

public interface MtgDataProvider {
    boolean shouldBeUpdated();

    void update(UpdateCallback updateCallback);

    String getDisplayName();

    interface UpdateCallback {
        void callback(TimestampedCardCollection result);
    }
}
