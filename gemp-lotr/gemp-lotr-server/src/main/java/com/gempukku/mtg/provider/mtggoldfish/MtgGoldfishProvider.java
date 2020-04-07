package com.gempukku.mtg.provider.mtggoldfish;

import com.gempukku.mtg.*;
import com.gempukku.mtg.provider.RetryUtil;
import com.gempukku.mtg.provider.Retryable;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class MtgGoldfishProvider implements MtgDataProvider {
    private static final Logger LOG = Logger.getLogger(MtgGoldfishProvider.class);

    private static final long DOWNLOAD_EVERY = 24L * 60 * 60 * 1000;

    private static final int SLEEP_MINIMUM = 8 * 1000;
    private static final int SLEEP_MAXIMUM = 12 * 1000;

    private static final int TIMEOUT = 5 * 60 * 1000;
    private static final int RETRY_MAX = 5;

    private long _nextStart;

    public MtgGoldfishProvider() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            long someDownloadTime = sdf.parse("2015-01-01 04:25:00").getTime();

            long number = (System.currentTimeMillis() - someDownloadTime) / DOWNLOAD_EVERY;

            _nextStart = someDownloadTime + number * DOWNLOAD_EVERY;
        } catch (ParseException exp) {
            // ignore
        }
    }

    @Override
    public String getDisplayName() {
        return "MtgGoldFish.com";
    }

    @Override
    public boolean shouldBeUpdated() {
        return _nextStart < System.currentTimeMillis();
    }

    @Override
    public void update(final UpdateCallback updateCallback) {
        _nextStart += DOWNLOAD_EVERY;
        Thread thr = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        updateCallback.callback(updateDatabase());
                    }
                });
        thr.start();
    }

    private TimestampedCardCollection updateDatabase() {
        long updateDate = System.currentTimeMillis();

        final List<SetCardData> resultArray = new LinkedList<SetCardData>();
        try {
            List<MtgGoldfishCardSet> mtgCardSets = downloadSetListWithRetry();
            for (final MtgGoldfishCardSet mtgCardSet : mtgCardSets) {
                downloadSetWithRetry(resultArray, mtgCardSet);
            }
        } catch (IOException exp) {
            LOG.error("Unable to download card list", exp);
        }
        return new TimestampedCardCollection(updateDate, resultArray);
    }

    private List<MtgGoldfishCardSet> downloadSetListWithRetry() throws IOException {
        return RetryUtil.executeInRetry(
                new Retryable<List<MtgGoldfishCardSet>, IOException>() {
                    @Override
                    public List<MtgGoldfishCardSet> execute() throws IOException {
                        return downloadSetList();
                    }

                    @Override
                    public boolean isRetryable(Exception exception) {
                        return exception instanceof IOException;
                    }
                }, RETRY_MAX);
    }

    private List<MtgGoldfishCardSet> downloadSetList() throws IOException {
        List<MtgGoldfishCardSet> results = new LinkedList<MtgGoldfishCardSet>();
        Document doc = Jsoup.connect("http://www.mtggoldfish.com/prices/paper/standard").get();
        String[] priceListTypes = new String[]{"Standard", "Modern", "Legacy", "Special"};
        for (String priceListType : priceListTypes) {
            Elements cardElements = doc.select(".priceList-setMenu-" + priceListType);
            Elements setImages = cardElements.select("li img");
            for (Element setImage : setImages) {
                String uriPostfix = setImage.attr("alt");
                String setName = setImage.parent().text();
                results.add(new MtgGoldfishCardSet(uriPostfix, setName));
            }
        }

        return results;
    }

    private void downloadSetWithRetry(final List<SetCardData> resultArray, final MtgGoldfishCardSet mtgCardSet) throws IOException {
        try {
            RetryUtil.executeInRetry(
                    new Retryable<Void, IOException>() {
                        @Override
                        public Void execute() throws IOException {
                            downloadSet(mtgCardSet, resultArray);
                            return null;
                        }

                        @Override
                        public boolean isRetryable(Exception exception) {
                            return exception instanceof IOException;
                        }
                    }, RETRY_MAX);
        } catch (HttpStatusException exp) {
            // Unable to download the set - skip it
        }
    }

    private void downloadSet(MtgGoldfishCardSet mtgCardSet, List<SetCardData> result) throws IOException {
        try {
            Thread.sleep(getSleepTime());
        } catch (InterruptedException e) {
            // Ignore
        }
        String link = "http://www.mtggoldfish.com/index/" + mtgCardSet.getUrlPostfix();
        Document doc = Jsoup.parse(new URL(link), TIMEOUT);
        boolean isInPaper = (doc.select("#priceHistoryTabs [href=#tab-paper]").size() > 0);
        if (isInPaper && !containsLinkSet(result, link)) {
            List<CardData> allCards = new LinkedList<CardData>();

            Elements cardElements = doc.select(".index-price-table-paper tbody tr");
            for (Element cardElement : cardElements) {
                String cardName = cardElement.select("td:nth-of-type(1) a").text();
                String cardId = mtgCardSet.getUrlPostfix() + "-" + cardName;
                float price = Float.parseFloat(cardElement.select("td:nth-of-type(4)").text().replace(",", ""));
                allCards.add(new CardData(cardId, cardName, Math.round(price * 100), null));
            }

            boolean hasFoils = doc.select(".index-price-header-foil-switcher").size() > 0;
            if (hasFoils) {
                downloadSet(new MtgGoldfishCardSet(mtgCardSet.getUrlPostfix() + "_F", mtgCardSet.getInfoLine() + " · Foil"), result);
            }
            result.add(new SetCardData(mtgCardSet.getInfoLine(), link, allCards));
        }
    }

    private boolean containsLinkSet(List<SetCardData> result, String link) {
        for (SetCardData setCardData : result) {
            if (setCardData.getSetLink().equals(link))
                return true;
        }
        return false;
    }

    private int getSleepTime() {
        return SLEEP_MINIMUM + ThreadLocalRandom.current().nextInt(SLEEP_MAXIMUM - SLEEP_MINIMUM);
    }

    public static void main(String[] args) {
        MtgGoldfishProvider mtgCardServer = new MtgGoldfishProvider();
        TimestampedCardCollection timestampedCardCollection = mtgCardServer.updateDatabase();
        System.out.println(new String(MtgCardServer.marshallData(timestampedCardCollection.getSetCardDataList()), Charset.forName("UTF-8")));
    }
}
