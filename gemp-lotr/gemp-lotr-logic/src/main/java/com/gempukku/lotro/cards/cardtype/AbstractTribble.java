package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.common.CardType;

public abstract class AbstractTribble extends AbstractLotroCardBlueprint {
    private final int _tribbleValue;
    private final String _tribblePower;

    public AbstractTribble(String name, int tribbleValue, String tribblePower) {
        super(0, null, CardType.TRIBBLE, null, name);
        _tribbleValue = tribbleValue;
        _tribblePower = tribblePower;
    }

    @Override
    public int getTribbleValue() {
        return _tribbleValue;
    }

    public String getTribblePower() {
        return _tribblePower;
    }
}
