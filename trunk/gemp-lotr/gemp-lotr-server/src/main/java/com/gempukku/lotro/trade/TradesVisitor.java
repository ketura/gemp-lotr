package com.gempukku.lotro.trade;

public interface TradesVisitor {
    public void processPendingTradeRequestSent(String playerTo);

    public void processPendingTradeRequestIncoming(String playerFrom);

    public void processOngoingTrade(TradeState tradeState, String chatRoomName);
}
