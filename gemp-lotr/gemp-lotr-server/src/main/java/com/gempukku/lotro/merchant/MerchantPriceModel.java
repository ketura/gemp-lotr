package com.gempukku.lotro.merchant;

import java.util.Date;

public interface MerchantPriceModel {
    public int getCardSellPrice(int lastTransactionValue, Date lastTransactionTime, Date openingTime, Date currentTime);

    public int getCardBuyPrice(int lastTransactionValue, Date lastTransactionTime, Date openingTime, Date currentTime);
}
