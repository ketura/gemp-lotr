package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class ReplaceSiteResult extends EffectResult {
    private int _siteNumber;
    private String _playerId;

    public ReplaceSiteResult(String playerId, int siteNumber) {
        super(Type.REPLACE_SITE);
        _playerId = playerId;
        _siteNumber = siteNumber;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public int getSiteNumber() {
        return _siteNumber;
    }
}
