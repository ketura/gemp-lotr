package com.gempukku.lotro.merchant;

import com.gempukku.lotro.db.MerchantDAO;
import org.junit.Test;

import java.util.Date;

public class ParametrizedMerchantTest {
    @Test
    public void checkBuying() {
        Date setupDate = new Date(0);
        Date firstTrans = new Date(0);

        MerchantDAO merchantDao = new MockMerchantDAO();

        ParametrizedMerchant merchant = new ParametrizedMerchant();
        merchant.setMerchantDao(merchantDao);
        merchant.setMerchantSetupDate(setupDate);

        merchant.cardSold("1_1", firstTrans, 1000);

        Date buyTime = new Date(1000 * 60 * 60 * 12);
        System.out.println(merchant.getCardBuyPrice("1_1", buyTime));
        System.out.println(merchant.getCardSellPrice("1_1", buyTime));
    }
}
