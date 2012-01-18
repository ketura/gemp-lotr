package com.gempukku.lotro.trade;

import com.gempukku.lotro.game.CardCollection;

public interface TradeStateVisitor {
    public void processTradeState(String otherParty, CardCollection offering, CardCollection getting,
                                  boolean selfAccepted, boolean otherAccepted, int tradeState,
                                  boolean selfConfirmed, boolean otherConfirmed);
}
