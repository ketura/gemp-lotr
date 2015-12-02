package com.gempukku.mtg;

import com.gempukku.lotro.AbstractServer;

import java.nio.charset.Charset;

public class MtgCardServer extends AbstractServer {
    private volatile CardDatabaseHolder _cardDatabaseHolder;
    private long _downloadEvery;
    private long _nextStart;

    public MtgCardServer(long someDownloadTime, long downloadEvery) {
        _downloadEvery = downloadEvery;

        long number = (System.currentTimeMillis() - someDownloadTime) / downloadEvery;

        _nextStart = someDownloadTime + number * downloadEvery;
    }

    private void updateDatabase() {
        Thread thr = new Thread(new UpdateDatabase());
        thr.start();
    }

    public CardDatabaseHolder getCardDatabaseHolder() {
        return _cardDatabaseHolder;
    }

    @Override
    protected void cleanup() {
        if (_nextStart < System.currentTimeMillis()) {
            updateDatabase();
            _nextStart += _downloadEvery;
        }
    }

    public class CardDatabaseHolder {
        private String _updateMarker;
        private byte[] _bytes;

        public CardDatabaseHolder(byte[] bytes, String updateMarker) {
            _bytes = bytes;
            _updateMarker = updateMarker;
        }

        public String getUpdateMarker() {
            return _updateMarker;
        }

        public byte[] getBytes() {
            return _bytes;
        }
    }

    private byte[] getDatabase() {
        String exampleString = "[" +
                "{\"id\":\"sc\",\"name\":\"Snapcaster Mage\",\"info\":\"Innistrad\",\"midPrice\":5956}," +
                "{\"id\":\"scf\",\"name\":\"Snapcaster Mage\",\"info\":\"Innistrad · Foil\",\"midPrice\":18040}," +
                "{\"id\":\"ara\",\"name\":\"Ancestral Recall\",\"info\":\"Alpha\",\"midPrice\":500000}," +
                "{\"id\":\"ara\",\"name\":\"Ancestral Recall\",\"info\":\"Beta\",\"midPrice\":300000}," +
                "{\"id\":\"ara\",\"name\":\"Ancestral Recall\",\"info\":\"Unlimited\",\"midPrice\":50000}" +
                "]";
        return exampleString.getBytes(Charset.forName("UTF-8"));
    }

    public class UpdateDatabase implements Runnable {
        public void run() {
            String updateMarker = String.valueOf(System.currentTimeMillis());

            _cardDatabaseHolder = new CardDatabaseHolder(getDatabase(), updateMarker);
        }
    }
}
