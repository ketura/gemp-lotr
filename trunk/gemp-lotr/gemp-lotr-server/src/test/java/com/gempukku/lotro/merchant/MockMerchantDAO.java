package com.gempukku.lotro.merchant;

import com.gempukku.lotro.db.MerchantDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockMerchantDAO implements MerchantDAO {
    private Map<String, Float> _prices = new HashMap<String, Float>();
    private Map<String, Date> _dates = new HashMap<String, Date>();
    private Map<String, TransactionType> _transactionTypes = new HashMap<String, TransactionType>();

    @Override
    public void addTransaction(String blueprintId, float price, Date date, TransactionType transactionType) {
        _prices.put(blueprintId, price);
        _dates.put(blueprintId, date);
        _transactionTypes.put(blueprintId, transactionType);
    }

    @Override
    public Transaction getLastTransaction(String blueprintId) {
        if (_dates.containsKey(blueprintId))
            return new Transaction(_dates.get(blueprintId), _prices.get(blueprintId), _transactionTypes.get(blueprintId));
        return null;
    }

    @Override
    public void clearCache() {
    }
}
