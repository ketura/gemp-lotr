package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CollectionSerializerTest {
    private CollectionSerializer _serializer = new CollectionSerializer();

    @Test
    public void testSerializeDeserialize() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(256 * 256 * 250);
        collection.addItem("1_1", 2);
        collection.addItem("1_231T", 3);
        collection.addItem("1_23*", 3);
        collection.addItem("1_237T*", 3);
        collection.addItem("FotR - Booster", 2);
        collection.addItem("15_2", 3);
        collection.addItem("15_4*", 3);

        CardCollection resultCollection = serializeAndDeserialize(collection);

        final Map<String, CardCollection.Item> result = resultCollection.getAll();
        assertEquals(7, result.size());
        assertEquals(256 * 256 * 250, resultCollection.getCurrency());
        assertEquals(2, result.get("1_1").getCount());
        assertEquals(3, result.get("1_231T").getCount());
        assertEquals(3, result.get("1_23*").getCount());
        assertEquals(3, result.get("1_237T*").getCount());
        assertEquals(3, result.get("15_2").getCount());
        assertEquals(3, result.get("15_4*").getCount());
        assertEquals(2, result.get("FotR - Booster").getCount());
    }

    @Test
    public void deserializeFromBytes() throws IOException {
        InputStream is = CollectionSerializerTest.class.getResourceAsStream("/testCollection");
        try {
            CardCollection resultCollection = _serializer.deserializeCollection(is);
            final Map<String, CardCollection.Item> result = resultCollection.getAll();
            assertEquals(6, result.size());
            assertEquals(256 * 256 * 250, resultCollection.getCurrency());
            assertEquals(2, result.get("1_1").getCount());
            assertEquals(3, result.get("1_231T").getCount());
            assertEquals(3, result.get("1_23*").getCount());
            assertEquals(3, result.get("1_237T*").getCount());
            assertEquals(2, result.get("FotR - Booster").getCount());
            assertEquals(1, result.get("15_2").getCount());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void testExtraInfo() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(12);
        collection.addItem("15_4*", 2);

        collection.setExtraInformation(Collections.singletonMap("a", (Object)"b"));

        CardCollection resultCollection = serializeAndDeserialize(collection);
        assertEquals(12, resultCollection.getCurrency());
        assertEquals(1, resultCollection.getAll().size());
        assertEquals(2, resultCollection.getAll().get("15_4*").getCount());
        assertEquals(1, resultCollection.getExtraInformation().size());
        assertEquals("b", resultCollection.getExtraInformation().get("a"));
    }

    @Test
    public void testJustPack() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("FotR - Booster", 8);

        final Map<String, CardCollection.Item> result = serializeAndDeserialize(collection).getAll();
        assertEquals(1, result.size());
        assertEquals(8, result.get("FotR - Booster").getCount());
    }

    private CardCollection serializeAndDeserialize(DefaultCardCollection collection) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return _serializer.deserializeCollection(bais);
    }

    @Test
    public void testLotsOfPacks() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("FotR - Booster", 500);

        final Map<String, CardCollection.Item> result = serializeAndDeserialize(collection).getAll();
        assertEquals(1, result.size());
        assertEquals(500, result.get("FotR - Booster").getCount());
    }

    @Test
    public void testLotsOfCurrency() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(127 * 255);

        assertEquals(127 * 255, serializeAndDeserialize(collection).getCurrency());
    }

    @Test
    public void testCardCount() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("1_1", 127);
        assertEquals(127, serializeAndDeserialize(collection).getItemCount("1_1"));

        collection.addItem("1_1", 1);
        assertEquals(128, serializeAndDeserialize(collection).getItemCount("1_1"));

        collection.addItem("1_1", 127);
        assertEquals(255, serializeAndDeserialize(collection).getItemCount("1_1"));

        // Card number is capped at 255
        collection.addItem("1_1", 1);
        assertEquals(255, serializeAndDeserialize(collection).getItemCount("1_1"));
    }
}
