package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class CollectionSerializerTest {
    @Test
    public void testSerializeDeserialize() throws IOException {
        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCards("1_1", 2);
        collection.addCards("1_231T", 3);
        collection.addCards("1_23*", 3);
        collection.addCards("1_237T*", 3);
        collection.addPacks("FotR - Booster", 2);
        collection.finishedReading();

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        final Map<String, Integer> result = resultCollection.getAll();
        assertEquals(5, result.size());
        assertEquals(2, (int) result.get("1_1"));
        assertEquals(3, (int) result.get("1_231T"));
        assertEquals(3, (int) result.get("1_23*"));
        assertEquals(3, (int) result.get("1_237T*"));
        assertEquals(2, (int) result.get("FotR - Booster"));
    }

    @Test
    public void testJustPack() throws IOException {
        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addPacks("FotR - Booster", 8);
        collection.finishedReading();

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        final Map<String, Integer> result = resultCollection.getAll();
        assertEquals(1, result.size());
        assertEquals(8, (int) result.get("FotR - Booster"));
    }
}
