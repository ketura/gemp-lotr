package com.gempukku.lotro.db;

import org.apache.commons.collections.map.LRUMap;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class CachedMerchantDAO implements MerchantDAO {
    private MerchantDAO _delegate;
    private Map<String, Transaction> _blueprintIdLastTransaction = Collections.synchronizedMap(new LRUMap(500));

    public CachedMerchantDAO(MerchantDAO delegate) {
        _delegate = delegate;
    }

    @Override
    public void addTransaction(String blueprintId, float price, Date date, TransactionType transactionType) {
        _delegate.addTransaction(blueprintId, price, date, transactionType);
        _blueprintIdLastTransaction.put(blueprintId, new Transaction(date, price, transactionType));
    }

    @Override
    public Transaction getLastTransaction(String blueprintId) {
        Transaction transaction = (Transaction) _blueprintIdLastTransaction.get(blueprintId);
        if (transaction == null) {
            transaction = _delegate.getLastTransaction(blueprintId);
            _blueprintIdLastTransaction.put(blueprintId, transaction);
        }
        return transaction;
    }
}
