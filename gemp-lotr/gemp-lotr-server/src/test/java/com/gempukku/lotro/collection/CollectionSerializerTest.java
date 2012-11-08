package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class CollectionSerializerTest {
    @Test
    public void testSerializeDeserialize() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(256 * 256 * 250);
        collection.addItem("1_1", 2);
        collection.addItem("1_231T", 3);
        collection.addItem("1_23*", 3);
        collection.addItem("1_237T*", 3);
        collection.addItem("FotR - Booster", 2);

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        final Map<String, CardCollection.Item> result = resultCollection.getAll();
        assertEquals(5, result.size());
        assertEquals(256 * 256 * 250, resultCollection.getCurrency());
        assertEquals(2, (int) result.get("1_1").getCount());
        assertEquals(3, (int) result.get("1_231T").getCount());
        assertEquals(3, (int) result.get("1_23*").getCount());
        assertEquals(3, (int) result.get("1_237T*").getCount());
        assertEquals(2, (int) result.get("FotR - Booster").getCount());
    }

    @Test
    public void testJustPack() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("FotR - Booster", 8);

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        final Map<String, CardCollection.Item> result = resultCollection.getAll();
        assertEquals(1, result.size());
        assertEquals(8, (int) result.get("FotR - Booster").getCount());
    }

    @Test
    public void testLotsOfPacks() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("FotR - Booster", 500);

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        final Map<String, CardCollection.Item> result = resultCollection.getAll();
        assertEquals(1, result.size());
        assertEquals(500, (int) result.get("FotR - Booster").getCount());
    }
    
    @Test
    public void testLotsOfCurrency() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(127*255);

        CollectionSerializer serializer = new CollectionSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeCollection(collection, baos);

        final byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        CardCollection resultCollection = serializer.deserializeCollection(bais);

        assertEquals(127*255, resultCollection.getCurrency());
    }
}
