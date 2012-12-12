package com.gempukku.lotro.db;

import java.util.Date;

public interface MerchantDAO {
    public Transaction getLastTransaction(String blueprintId);

    public void addTransaction(String blueprintId, float price, Date date, TransactionType transactionType);

    public enum TransactionType {
        SELL, BUY
    }

    public static class Transaction {
        private float _price;
        private Date _date;
        private TransactionType _transactionType;

        public Transaction(Date date, float price, TransactionType transactionType) {
            _date = date;
            _price = price;
            _transactionType = transactionType;
        }

        public Date getDate() {
            return _date;
        }

        public float getPrice() {
            return _price;
        }

        public TransactionType getTransactionType() {
            return _transactionType;
        }
    }
}
