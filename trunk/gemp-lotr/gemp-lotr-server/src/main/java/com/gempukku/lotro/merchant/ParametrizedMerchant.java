package com.gempukku.lotro.merchant;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.db.MerchantDAO;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParametrizedMerchant implements Merchant {
    private static final int BOOSTER_PRICE = 1000;
    private static final long DAY = BOOSTER_PRICE * 60 * 60 * 24;

    private Date _merchantSetupDate;

    private final float _fluctuationValue = 0.1f;
    private final long _priceRevertTimeMs = 5 * DAY;
    private final long _easingTimeMs = 30 * DAY;
    private final float _profitMargin = 0.7f;
    private final double _returnPrizeSlope = 0.3;
    private final double _decreasePrizeSlope = 0.8;

    private MerchantDAO _merchantDao;
    private Map<Integer, SetRarity> _rarity = new HashMap<Integer, SetRarity>();
    private LotroCardBlueprintLibrary _library;

    public ParametrizedMerchant(LotroCardBlueprintLibrary library) {
        _library = library;
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
        boolean foil = false;
        if (blueprintId.endsWith("*"))
            foil = true;

        blueprintId = _library.getBaseBlueprintId(blueprintId);

        Double normalPrice = getNormalPrice(blueprintId, currentTime);
        if (normalPrice == null)
            return null;

        int price = (int) Math.floor(_profitMargin * normalPrice / getSetupComponent(currentTime));

        if (foil)
            return 2 * price;
        return price;
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
            lastTrans = new MerchantDAO.Transaction(_merchantSetupDate, basePrice, MerchantDAO.TransactionType.SELL);
        }
        if (lastTrans.getDate().getTime() + _priceRevertTimeMs > currentTime.getTime())
            if (lastTrans.getTransactionType() == MerchantDAO.TransactionType.SELL) {
                return (1 + _fluctuationValue) * lastTrans.getPrice() / (1 + (_fluctuationValue * Math.pow(1f * (currentTime.getTime() - lastTrans.getDate().getTime()) / _priceRevertTimeMs, _returnPrizeSlope)));
            } else {
                return (1 - _fluctuationValue) * lastTrans.getPrice() / (1 - (_fluctuationValue * Math.pow(1f * (currentTime.getTime() - lastTrans.getDate().getTime()) / _priceRevertTimeMs, _returnPrizeSlope)));
            }
        //  (stored price)/(1+(fluctuation value * ms since last transaction)/(price revert time in ms))
        return lastTrans.getPrice() / (1 + (_fluctuationValue * Math.pow(1f * (currentTime.getTime() - lastTrans.getDate().getTime() - _priceRevertTimeMs) / _priceRevertTimeMs, _decreasePrizeSlope)));
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
        if (blueprintId.endsWith("*"))
            price = price / 2;
        blueprintId = _library.getBaseBlueprintId(blueprintId);
        _merchantDao.addTransaction(blueprintId, (price / _profitMargin), currentTime, MerchantDAO.TransactionType.BUY);
    }

    @Override
    public void cardSold(String blueprintId, Date currentTime, int price) {
        _merchantDao.addTransaction(blueprintId, price, currentTime, MerchantDAO.TransactionType.SELL);
    }
}
