package com.gempukku.lotro.merchant;

import com.gempukku.lotro.db.MerchantDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockMerchantDAO implements MerchantDAO {
    private Map<String, Float> _prices = new HashMap<String, Float>();
    private Map<String, Date> _dates = new HashMap<String, Date>();

    @Override
    public void addTransaction(String blueprintId, float price, Date date) {
        _prices.put(blueprintId, price);
        _dates.put(blueprintId, date);
    }

    @Override
    public Transaction getLastTransaction(String blueprintId) {
        if (_dates.containsKey(blueprintId))
            return new Transaction(_dates.get(blueprintId), _prices.get(blueprintId));
        return null;
    }
}
