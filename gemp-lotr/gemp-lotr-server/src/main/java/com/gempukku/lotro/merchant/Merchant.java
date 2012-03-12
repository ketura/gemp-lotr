package com.gempukku.lotro.merchant;

import java.util.Date;

public interface Merchant {
    public Integer getCardSellPrice(String blueprintId, Date currentTime);

    public Integer getCardBuyPrice(String blueprintId, Date currentTime);

    public void cardSold(String blueprintId, Date currentTime, int price);

    public void cardBought(String blueprintId, Date currentTime, int price);
}
