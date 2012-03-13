package com.gempukku.lotro.db;

import java.util.Date;

public class DbMerchantDAO implements MerchantDAO {
    private DbAccess _dbAccess;

    public DbMerchantDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public void addTransaction(String blueprintId, float price, Date date) {

    }

    @Override
    public Transaction getLastTransaction(String blueprintId) {
        return null;
    }
}
