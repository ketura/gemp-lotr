package com.gempukku.mtg;

import com.gempukku.lotro.AbstractServer;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MtgCardServer extends AbstractServer {
    private static final Logger LOG = Logger.getLogger(MtgCardServer.class);

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
            _nextStart += _downloadEvery;
            updateDatabase();
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

    private class UpdateDatabase implements Runnable {
        private static final int SLEEP_MINIMUM = 60 * 1000;
        private static final int SLEEP_MAXIMUM = 120 * 1000;

        private static final int TIMEOUT = 5 * 60 * 1000;

        public void run() {
            String updateMarker = String.valueOf(System.currentTimeMillis());

            try {
                JSONArray resultArray = new JSONArray();
                List<MtgCardSet> mtgCardSets = downloadSetList();
                for (MtgCardSet mtgCardSet : mtgCardSets) {
                    downloadSet(mtgCardSet, resultArray);
                }

                String resultJson = resultArray.toJSONString();
                _cardDatabaseHolder = new CardDatabaseHolder(resultJson.getBytes("UTF-8"), updateMarker);
            } catch (Exception exp) {
                LOG.error("Error downloading card database", exp);

                // Temporarily we sent the error as response
                StringWriter writer = new StringWriter();
                exp.printStackTrace(new PrintWriter(writer));
                _cardDatabaseHolder = new CardDatabaseHolder(writer.toString().getBytes(Charset.defaultCharset()), updateMarker);
            }
        }

        private List<MtgCardSet> downloadSetList() throws IOException {
            List<MtgCardSet> results = new LinkedList<MtgCardSet>();
            Document doc = Jsoup.connect("http://www.mtggoldfish.com/prices/paper/standard").get();
            String[] priceListTypes = new String[]{"Standard", "Modern", "Legacy", "Special"};
            for (String priceListType : priceListTypes) {
                Elements cardElements = doc.select(".priceList-setMenu-" + priceListType);
                Elements setImages = cardElements.select("li img");
                for (Element setImage : setImages) {
                    String uriPostfix = setImage.attr("alt");
                    String setName = setImage.parent().text();
                    results.add(new MtgCardSet(uriPostfix, setName));
                }
            }

            return results;
        }

        private void downloadSet(MtgCardSet mtgCardSet, JSONArray jsonArray) throws IOException {
            try {
                Thread.sleep(getSleepTime());
            } catch (InterruptedException e) {

            }
            Document doc = Jsoup.parse(new URL("http://www.mtggoldfish.com/index/" + mtgCardSet.getUrlPostfix()), TIMEOUT);
            boolean isInPaper = (doc.select("#priceHistoryTabs [href=#tab-paper]").size() > 0);
            if (isInPaper) {
                JSONArray setArray = new JSONArray();

                Map<String, Object> setObject = new LinkedHashMap<String, Object>();
                setObject.put("name", mtgCardSet.getInfoLine());
                setObject.put("cards", setArray);
                jsonArray.add(setObject);

                Elements cardElements = doc.select(".index-price-table-paper tbody tr");
                for (Element cardElement : cardElements) {
                    String cardName = cardElement.select("td:nth-of-type(1) a").text();
                    String cardId = mtgCardSet.getUrlPostfix() + "-" + cardName;
                    float price = Float.parseFloat(cardElement.select("td:nth-of-type(4)").text());
                    JSONObject cardObject = new JSONObject();
                    cardObject.put("id", cardId);
                    cardObject.put("name", cardName);
                    cardObject.put("price", Math.round(price * 100));
                    setArray.add(cardObject);
                }

                boolean hasFoils = doc.select(".index-price-header-foil-switcher").size() > 0;
                if (hasFoils) {
                    downloadSet(new MtgCardSet(mtgCardSet.getUrlPostfix() + "_F", mtgCardSet.getInfoLine() + " · Foil"), jsonArray);
                }
            }
        }

        private int getSleepTime() {
            Random rnd = new Random();
            return SLEEP_MINIMUM + rnd.nextInt(SLEEP_MAXIMUM - SLEEP_MINIMUM);
        }
    }

    public static void main(String[] args) {
        MtgCardServer mtgCardServer = new MtgCardServer(0, 1);

        UpdateDatabase updateDatabase = mtgCardServer.new UpdateDatabase();
        updateDatabase.run();
    }
}
