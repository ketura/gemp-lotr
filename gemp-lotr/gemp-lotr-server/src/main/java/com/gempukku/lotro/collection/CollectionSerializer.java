package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionSerializer {
    private List<String> _packIds = new ArrayList<String>();
    private List<String> _cardIds = new ArrayList<String>();
    private LotroCardBlueprintLibrary _library;

    public CollectionSerializer(LotroCardBlueprintLibrary library) {
        _library = library;
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
        byte version = 0;
        outputStream.write(version);
        byte packBytes = (byte) _packIds.size();
        outputStream.write(packBytes);

        final Map<String, Integer> collectionCounts = collection.getAll();
        for (String packId : _packIds) {
            final Integer count = collectionCounts.get(packId);
            if (count == null)
                outputStream.write(0);
            else
                outputStream.write(count);
        }

        int cardBytes = _cardIds.size();
        outputStream.write((cardBytes >> 8) & 0x000000ff);
        outputStream.write(cardBytes & 0x000000ff);

        for (String cardId : _cardIds) {
            final Integer count = collectionCounts.get(cardId);
            if (count == null)
                outputStream.write(0);
            else
                outputStream.write(count);
        }
    }

    public MutableCardCollection deserializeCollection(InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version == 0) {
            return deserializeCollectionVer0(new BufferedInputStream(inputStream));
        } else {
            throw new IllegalStateException("Unkown version of serialized collection: " + version);
        }
    }

    private MutableCardCollection deserializeCollectionVer0(BufferedInputStream inputStream) throws IOException {
        int packBytes = inputStream.read();

        DefaultCardCollection collection = new DefaultCardCollection(_library);

        byte[] packs = new byte[packBytes];
        int read = inputStream.read(packs);
        if (read != packBytes)
            throw new IllegalStateException("Under-read the packs information");
        for (int i = 0; i < packs.length; i++)
            if (packs[i] > 0)
                collection.addPacks(_packIds.get(i), packs[i]);

        int cardBytes = (inputStream.read() << 8) + inputStream.read();
        byte[] cards = new byte[cardBytes];
        read = inputStream.read(cards);
        if (read != cardBytes)
            throw new IllegalArgumentException("Under-read the packs information");
        for (int i = 0; i < cards.length; i++)
            if (cards[i] > 0) {
                final String blueprintId = _cardIds.get(i);
                collection.addCards(blueprintId, _library.getLotroCardBlueprint(blueprintId), cards[i]);
            }

        return collection;
    }
}
