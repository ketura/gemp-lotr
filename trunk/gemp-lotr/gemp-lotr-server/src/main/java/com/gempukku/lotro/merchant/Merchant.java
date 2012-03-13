package com.gempukku.lotro.merchant;

import java.util.Date;

public interface Merchant {
    public Integer getCardSellPrice(String blueprintId, Date currentTime);

    public Integer getCardBuyPrice(String blueprintId, Date currentTime);

    /**
     * Called when card was sold by merchant.
     *
     * @param blueprintId
     * @param currentTime
     * @param price
     */
    public void cardSold(String blueprintId, Date currentTime, int price);

    /**
     * Called when card was bought by merchant.
     *
     * @param blueprintId
     * @param currentTime
     * @param price
     */
    public void cardBought(String blueprintId, Date currentTime, int price);
}
