package com.gempukku.mtg;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.mtg.provider.mtggoldfish.MtgGoldfishProvider;
import org.json.simple.JSONArray;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MtgCardServer extends AbstractServer {
    private Map<String, MtgDataProvider> _dataProviders = new HashMap<String, MtgDataProvider>();

    private Map<String, CardDatabaseHolder> _providersData = Collections.synchronizedMap(new HashMap<String, CardDatabaseHolder>());

    public MtgCardServer() {
        _dataProviders.put("mtgGoldFish", new MtgGoldfishProvider());
    }

    public CardDatabaseHolder getCardDatabaseHolder(String providerName) {
        return _providersData.get(providerName);
    }

    @Override
    protected void cleanup() {
        for (Map.Entry<String, MtgDataProvider> dataProviderEntry : _dataProviders.entrySet()) {
            final String key = dataProviderEntry.getKey();
            MtgDataProvider dataProvider = dataProviderEntry.getValue();
            if (dataProvider.shouldBeUpdated()) {
                dataProvider.update(
                        new MtgDataProvider.UpdateCallback() {
                            @Override
                            public void callback(TimestampedCardCollection result) {
                                _providersData.put(key, new CardDatabaseHolder(marshallData(result.getSetCardDataList()), result.getUpdateMarker()));
                            }
                        });
            }
        }
    }

    public static byte[] marshallData(List<SetCardData> setCardDataList) {
        JSONArray allSets = new JSONArray();
        for (SetCardData setCardData : setCardDataList) {
            List<Object> setArray = new LinkedList<Object>();

            for (CardData cardData : setCardData.getAllCards()) {
                Map<String, Object> cardObject = new LinkedHashMap<String, Object>();
                cardObject.put("id", cardData.getId());
                cardObject.put("name", cardData.getName());
                cardObject.put("price", cardData.getPrice());
                setArray.add(cardObject);
            }

            Map<String, Object> setObject = new LinkedHashMap<String, Object>();
            setObject.put("name", setCardData.getSetName());
            setObject.put("cards", setArray);

            allSets.add(setObject);
        }

        return allSets.toJSONString().getBytes(Charset.forName("UTF-8"));
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
}
