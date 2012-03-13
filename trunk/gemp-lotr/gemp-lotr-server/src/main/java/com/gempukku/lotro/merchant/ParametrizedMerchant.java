package com.gempukku.lotro.merchant;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.db.MerchantDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParametrizedMerchant implements Merchant {
    private static final int BOOSTER_PRICE = 1000;
    private static final long DAY = BOOSTER_PRICE * 60 * 60 * 24;

    private Date _merchantSetupDate;

    private float _fluctuationValue = 0.1f;
    private long _priceRevertTimeMs = 5 * DAY;
    private long _easingTimeMs = 30 * DAY;
    private float _profitMargin = 0.7f;

    private MerchantDAO _merchantDao;
    private Map<Integer, SetRarity> _rarity = new HashMap<Integer, SetRarity>();

    public ParametrizedMerchant() {
        RarityReader rarityReader = new RarityReader();
        for (int i = 0; i <= 19; i++)
            _rarity.put(i, rarityReader.getSetRarity(String.valueOf(i)));
    }

    public void setMerchantSetupDate(Date merchantSetupDate) {
        _merchantSetupDate = merchantSetupDate;
    }

    public void setMerchantDao(MerchantDAO merchantDao) {
        _merchantDao = merchantDao;
    }

    @Override
    public Integer getCardBuyPrice(String blueprintId, Date currentTime) {
        Double normalPrice = getNormalPrice(blueprintId, currentTime);
        if (normalPrice == null)
            return null;
        return (int) Math.floor(_profitMargin * normalPrice / getSetupComponent(currentTime));
    }

    @Override
    public Integer getCardSellPrice(String blueprintId, Date currentTime) {
        Double normalPrice = getNormalPrice(blueprintId, currentTime);
        if (normalPrice == null)
            return null;
        double setupComponent = getSetupComponent(currentTime);
        return (int) Math.ceil(normalPrice * setupComponent);
    }

    private double getSetupComponent(Date currentTime) {
        if (currentTime.getTime() < _merchantSetupDate.getTime() + _easingTimeMs)
            return 1 + ((_easingTimeMs - (currentTime.getTime() - _merchantSetupDate.getTime())) / (5d * DAY));
        return 1;
    }

    private Double getNormalPrice(String blueprintId, Date currentTime) {
        MerchantDAO.Transaction lastTrans = _merchantDao.getLastTransaction(blueprintId);
        if (lastTrans == null) {
            Integer basePrice = getBasePrice(blueprintId);
            if (basePrice == null)
                return null;
            lastTrans = new MerchantDAO.Transaction(_merchantSetupDate, basePrice);
        }
        //  (stored price)/(1+(fluctuation value * ms since last transaction)/(price revert time in ms))
        return lastTrans.getPrice() / (1 + (_fluctuationValue * Math.pow(1f * (currentTime.getTime() - lastTrans.getDate().getTime()) / _priceRevertTimeMs, 0.9)));
    }

    private Integer getBasePrice(String blueprintId) {
        int underscoreIndex = blueprintId.indexOf("_");
        if (underscoreIndex == -1)
            return null;
        SetRarity rarity = _rarity.get(Integer.parseInt(blueprintId.substring(0, blueprintId.indexOf("_"))));
        String cardRarity = rarity.getCardRarity(blueprintId);
        if (cardRarity.equals("X"))
            return 3 * BOOSTER_PRICE;
        if (cardRarity.equals("R") || cardRarity.equals("P"))
            return BOOSTER_PRICE;
        if (cardRarity.equals("U") || cardRarity.equals("S"))
            return BOOSTER_PRICE / 3;
        if (cardRarity.equals("C"))
            return BOOSTER_PRICE / 7;
        throw new RuntimeException("Unkown rarity for priced card: " + cardRarity);
    }

    @Override
    public void cardBought(String blueprintId, Date currentTime, int price) {
        _merchantDao.addTransaction(blueprintId, (price / _profitMargin) * (1 + _fluctuationValue), currentTime);
    }

    @Override
    public void cardSold(String blueprintId, Date currentTime, int price) {
        _merchantDao.addTransaction(blueprintId, price * (1 + _fluctuationValue), currentTime);
    }
}
