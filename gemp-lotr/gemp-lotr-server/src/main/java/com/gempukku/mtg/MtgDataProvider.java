package com.gempukku.mtg;

public interface MtgDataProvider {
    boolean shouldBeUpdated();

    void update(UpdateCallback updateCallback);

    interface UpdateCallback {
        void callback(TimestampedCardCollection result);
    }
}
