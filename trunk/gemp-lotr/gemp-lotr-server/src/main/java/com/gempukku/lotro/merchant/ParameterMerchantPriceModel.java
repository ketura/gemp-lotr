package com.gempukku.lotro.merchant;

import java.util.Date;

public class ParameterMerchantPriceModel implements MerchantPriceModel {
    private static final int DAY = 24 * 60 * 60 * 1000;
    private float _profitMargin = 0.7f;
    private long _priceRevertTime = 5 * DAY;
    private long _easingTime = 30 * DAY;

    @Override
    public int getCardBuyPrice(int lastTransactionValue, Date lastTransactionTime, Date openingTime, Date currentTime) {
        return (int) Math.floor(_profitMargin
                * getCardSellPrice(lastTransactionValue, lastTransactionTime, openingTime, currentTime));
    }

    @Override
    public int getCardSellPrice(int lastTransactionValue, Date lastTransactionTime, Date openingTime, Date currentTime) {
        return lastTransactionValue / (1 + ());
    }
}
