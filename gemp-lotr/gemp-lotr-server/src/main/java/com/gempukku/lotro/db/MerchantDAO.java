package com.gempukku.lotro.db;

import java.util.Date;

public interface MerchantDAO {
    public Transaction getLastTransaction(String blueprintId);

    public void addTransaction(String blueprintId, float price, Date date);

    public static class Transaction {
        private float _price;
        private Date _date;

        public Transaction(Date date, float price) {
            _date = date;
            _price = price;
        }

        public Date getDate() {
            return _date;
        }

        public float getPrice() {
            return _price;
        }
    }
}
