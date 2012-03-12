package com.gempukku.lotro.merchant;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.db.MerchantDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParametrizedMerchant implements Merchant {
    private static final long DAY = 1000 * 60 * 60 * 24;

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
        double normalPrice = getNormalPrice(blueprintId, currentTime);
        return (int) Math.floor(_profitMargin * normalPrice / getSetupComponent(currentTime));
    }

    @Override
    public Integer getCardSellPrice(String blueprintId, Date currentTime) {
        double normalPrice = getNormalPrice(blueprintId, currentTime);
        double setupComponent = getSetupComponent(currentTime);
        System.out.println(setupComponent);
        return (int) Math.ceil(normalPrice * setupComponent);
    }

    private double getSetupComponent(Date currentTime) {
        if (currentTime.getTime() < _merchantSetupDate.getTime() + _easingTimeMs)
            return 1 + ((_easingTimeMs - (currentTime.getTime() - _merchantSetupDate.getTime())) / (5d * DAY));
        return 1;
    }

    private double getNormalPrice(String blueprintId, Date currentTime) {
        MerchantDAO.Transaction lastTrans = _merchantDao.getLastTransaction(blueprintId);
        //  (stored price)/(1+(fluctuation value * ms since last transaction)/(price revert time in ms))
        return lastTrans.getPrice() / (1 + (_fluctuationValue * Math.pow(1f * (currentTime.getTime() - lastTrans.getDate().getTime()) / _priceRevertTimeMs, 0.9)));
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
