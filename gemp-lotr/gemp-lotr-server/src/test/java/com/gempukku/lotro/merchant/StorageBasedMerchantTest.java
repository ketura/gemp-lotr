package com.gempukku.lotro.merchant;

import com.gempukku.lotro.db.MerchantDAO;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class StorageBasedMerchantTest {
    private StorageBasedMerchant _merchant;
    private static final long DAY = 1000 * 60 * 60 * 24;

    @Before
    public void setup() {
        Date setupDate = new Date(0);
        MerchantDAO merchantDao = new MockMerchantDAO();

        _merchant = new StorageBasedMerchant(new LotroCardBlueprintLibrary(), merchantDao, setupDate);
    }

    @Test
    public void cardTradingWithNoStock() {
        assertNull(_merchant.getCardSellPrice("1_1", new Date(0)));
        assertNotNull(_merchant.getCardBuyPrice("1_1", new Date(0)));
    }

    @Test
    public void cardTradingWithSomeStock() {
        _merchant.cardBought("1_1", new Date(0), 1000);
        assertNotNull(_merchant.getCardSellPrice("1_1", new Date(0)));
        assertNotNull(_merchant.getCardBuyPrice("1_1", new Date(0)));
    }

    @Test
    public void cardTradingWithFullStock() {
        for (int i=0; i<100; i++)
            _merchant.cardBought("1_1", new Date(0), 1000);
        assertNotNull(_merchant.getCardSellPrice("1_1", new Date(0)));
        assertNull(_merchant.getCardBuyPrice("1_1", new Date(0)));
    }

    @Test
    public void cardPriceLowersAfterMerchantBuys() {
        _merchant.cardBought("1_1", new Date(0), 700);
        int cardInitialPrice = _merchant.getCardSellPrice("1_1", new Date(0));
        assertEqualsMoreOrLess((int) (1000 / 1.1f), cardInitialPrice);
    }

    @Test
    public void cardPriceRisesAfterMerchantSells() {
        // Setup some stock
        _merchant.cardBought("1_1", new Date(0), 700);
        _merchant.cardBought("1_1", new Date(0), 700);

        _merchant.cardSold("1_1", new Date(0), 1000);
        int cardInitialPrice = _merchant.getCardSellPrice("1_1", new Date(0));
        assertEqualsMoreOrLess((int) (1000*1.1f), cardInitialPrice);
    }

    @Test
    public void cardPriceRisesOverTimeWhenNoStock() {
        int initialPrice = _merchant.getCardBuyPrice("1_1", new Date(0));
        assertTrue(initialPrice < _merchant.getCardBuyPrice("1_1", new Date(DAY)));
    }

    @Test
    public void cardPriceLowersOverTimeWhenInStock() {
        for (int i=0; i<11; i++)
            _merchant.cardBought("1_1", new Date(0), 700);
        int initialPrice = _merchant.getCardBuyPrice("1_1", new Date(0));
        assertTrue(initialPrice > _merchant.getCardBuyPrice("1_1", new Date(DAY)));
    }

    @Test
    public void plotPricesAfterTransactions() {
        Date setupDate = new Date(-1000 * 60 * 60 * 24 * 50L);
        Date firstTrans = new Date(0);

        MerchantDAO merchantDao = new MockMerchantDAO();

        StorageBasedMerchant merchant = new StorageBasedMerchant(new LotroCardBlueprintLibrary(), merchantDao, setupDate);

        merchant.cardSold("1_1", firstTrans, 1000);
        merchant.cardBought("1_2", firstTrans, 700);

        long hour = 1000 * 60 * 60;

        System.out.println("-2,1000,700,1000,700");
        for (long time = 0; time < hour * 24 * 35L; time += hour * 2) {
            System.out.println("After "+formatTimeInHours(hour, time) + "," + merchant.getCardSellPrice("1_1", new Date(time)) + "," + merchant.getCardBuyPrice("1_1", new Date(time))
                    + "," + merchant.getCardSellPrice("1_2", new Date(time)) + "," + merchant.getCardBuyPrice("1_2", new Date(time)));
        }
    }

    private String formatTimeInHours(long hour, long time) {
        return (time / (24*hour))+" days, "+((time/hour)%24)+" hours";
    }


    // @Test
    public void plotPriceEasingFromSetup() {
        Date setupDate = new Date(0);

        MerchantDAO merchantDao = new MockMerchantDAO();

        StorageBasedMerchant merchant = new StorageBasedMerchant(new LotroCardBlueprintLibrary(), merchantDao, setupDate);

        long hour = 1000 * 60 * 60;

        for (long time = 0; time < hour * 24 * 35L; time += hour * 2)
            System.out.println("After "+formatTimeInHours(hour, time) + "," + merchant.getCardSellPrice("1_1", new Date(time)) + "," + merchant.getCardBuyPrice("1_1", new Date(time)));
    }

    private void assertEqualsMoreOrLess(int expected, int given) {
        assertTrue("Expected " + expected + " more or less, but received " + given, given <= expected + 1 && given >= expected - 1);
    }
    
}
