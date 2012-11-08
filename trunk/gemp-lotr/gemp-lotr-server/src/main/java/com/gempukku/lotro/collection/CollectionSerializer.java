package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionSerializer {
    private List<String> _packIds = new ArrayList<String>();
    private List<String> _cardIds = new ArrayList<String>();

    public CollectionSerializer() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CollectionSerializer.class.getResourceAsStream("/packs.txt"), "UTF-8"));
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    _packIds.add(line);
            } finally {
                bufferedReader.close();
            }

            loadSet("1");
            loadSet("2");
            loadSet("3");
            loadSet("0");
            loadSet("4");
            loadSet("5");
            loadSet("6");
            loadSet("7");
            loadSet("8");
            loadSet("9");
            loadSet("10");
            loadSet("11");
            loadSet("12");
            loadSet("13");
            loadSet("14");
            loadSet("15");
            loadSet("16");
            loadSet("17");
            loadSet("18");
            loadSet("19");

        } catch (IOException exp) {
            throw new RuntimeException("Problem loading collection data", exp);
        }
    }

    private void loadSet(String setNo) throws IOException {
        BufferedReader cardReader = new BufferedReader(new InputStreamReader(CollectionSerializer.class.getResourceAsStream("/set" + setNo + "-rarity.txt"), "UTF-8"));
        try {
            String line;

            while ((line = cardReader.readLine()) != null) {
                if (!line.substring(0, setNo.length()).equals(setNo))
                    throw new IllegalStateException("Seems the rarity is for some other set");
                // Normal
                _cardIds.add(translateToBlueprintId(line));
                // Foil
                _cardIds.add(translateToBlueprintId(line) + "*");
            }
        } finally {
            cardReader.close();
        }
    }

    private String translateToBlueprintId(String rarityString) {
        int firstNonDigitIndex = getFirstNonDigitIndex(rarityString);
        final String setNo = rarityString.substring(0, firstNonDigitIndex);
        final String cardAfterSetNo = rarityString.substring(firstNonDigitIndex + 1);

        return setNo + "_" + cardAfterSetNo;
    }

    private static int getFirstNonDigitIndex(String string) {
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (!Character.isDigit(chars[i]))
                return i;
        return -1;
    }

    public void serializeCollection(CardCollection collection, OutputStream outputStream) throws IOException {
        byte version = 2;
        outputStream.write(version);

        int currency = collection.getCurrency();
        printInt(outputStream, currency, 3);

        byte packTypes = (byte) _packIds.size();
        outputStream.write(packTypes);

        final Map<String, CardCollection.Item> collectionCounts = collection.getAll();
        for (String packId : _packIds) {
            final CardCollection.Item count = collectionCounts.get(packId);
            if (count == null) {
                printInt(outputStream, 0, 2);
            } else {
                printInt(outputStream, count.getCount(), 2);
            }
        }

        int cardBytes = _cardIds.size();
        printInt(outputStream, cardBytes, 2);

        for (String cardId : _cardIds) {
            final CardCollection.Item count = collectionCounts.get(cardId);
            if (count == null)
                outputStream.write(0);
            else
                outputStream.write(count.getCount());
        }
    }

    public MutableCardCollection deserializeCollection(InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version == 0) {
            return deserializeCollectionVer0(new BufferedInputStream(inputStream));
        } else if (version == 1) {
            return deserializeCollectionVer1(new BufferedInputStream(inputStream));
        } else if (version == 2) {
            return deserializeCollectionVer2(new BufferedInputStream(inputStream));
        } else {
            throw new IllegalStateException("Unkown version of serialized collection: " + version);
        }
    }

    private MutableCardCollection deserializeCollectionVer0(BufferedInputStream inputStream) throws IOException {
        int packTypes = inputStream.read();

        DefaultCardCollection collection = new DefaultCardCollection();

        byte[] packs = new byte[packTypes];
        int read = inputStream.read(packs);
        if (read != packTypes)
            throw new IllegalStateException("Under-read the packs information");
        for (int i = 0; i < packs.length; i++)
            if (packs[i] > 0)
                collection.addItem(_packIds.get(i), packs[i]);

        int cardBytes = convertToInt(inputStream.read(), inputStream.read());
        byte[] cards = new byte[cardBytes];
        read = inputStream.read(cards);
        if (read != cardBytes)
            throw new IllegalArgumentException("Under-read the cards information");
        for (int i = 0; i < cards.length; i++)
            if (cards[i] > 0) {
                final String blueprintId = _cardIds.get(i);
                collection.addItem(blueprintId, cards[i]);
            }

        return collection;
    }

    private MutableCardCollection deserializeCollectionVer1(BufferedInputStream inputStream) throws IOException {
        int byte1 = inputStream.read();
        int byte2 = inputStream.read();
        int byte3 = inputStream.read();

        int currency = convertToInt(byte1, byte2, byte3);

        int packTypes = inputStream.read();

        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(currency);

        byte[] packs = new byte[packTypes];
        int read = inputStream.read(packs);
        if (read != packTypes)
            throw new IllegalStateException("Under-read the packs information");
        for (int i = 0; i < packs.length; i++)
            if (packs[i] > 0)
                collection.addItem(_packIds.get(i), packs[i]);

        int cardBytes = convertToInt(inputStream.read(), inputStream.read());
        byte[] cards = new byte[cardBytes];
        read = inputStream.read(cards);
        if (read != cardBytes)
            throw new IllegalArgumentException("Under-read the cards information");
        for (int i = 0; i < cards.length; i++)
            if (cards[i] > 0) {
                final String blueprintId = _cardIds.get(i);
                collection.addItem(blueprintId, cards[i]);
            }

        return collection;
    }

    private MutableCardCollection deserializeCollectionVer2(BufferedInputStream inputStream) throws IOException {
        int byte1 = inputStream.read();
        int byte2 = inputStream.read();
        int byte3 = inputStream.read();

        int currency = convertToInt(byte1, byte2, byte3);

        int packTypes = inputStream.read();

        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(currency);

        byte[] packs = new byte[packTypes * 2];

        int read = inputStream.read(packs);
        if (read != packTypes * 2)
            throw new IllegalStateException("Under-read the packs information");
        for (int i = 0; i < packTypes; i++) {
            int count = convertToInt(packs[i * 2], packs[i * 2 + 1]);
            if (count > 0)
                collection.addItem(_packIds.get(i), count);
        }

        int cardBytes = convertToInt(inputStream.read(), inputStream.read());
        byte[] cards = new byte[cardBytes];
        read = inputStream.read(cards);
        if (read != cardBytes)
            throw new IllegalArgumentException("Under-read the cards information");
        for (int i = 0; i < cards.length; i++)
            if (cards[i] > 0) {
                final String blueprintId = _cardIds.get(i);
                collection.addItem(blueprintId, cards[i]);
            }

        return collection;
    }

    private int convertToInt(int... bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] << ((bytes.length - i - 1) * 8);
            if (value < 0)
                value +=256;
            result += value;
        }
        return result;
    }

    private void printInt(OutputStream outputStream, int value, int byteCount) throws IOException {
        for (int i = 0; i < byteCount; i++)
            outputStream.write((value >> (8 * (byteCount - i - 1))) & 0x000000ff);
    }
}
