package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.game.CardCollection;

public interface CardCollectionProducer {
    CardCollection getCardCollection(long seed);
}
