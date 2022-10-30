package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CollectionSerializerTest {
    private final CollectionSerializer _serializer = new CollectionSerializer();

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

        assertEquals(7, Iterables.size(resultCollection.getAll()));
        assertEquals(256 * 256 * 250, resultCollection.getCurrency());
        assertEquals(2, resultCollection.getItemCount("1_1"));
        assertEquals(3, resultCollection.getItemCount("1_231T"));
        assertEquals(3, resultCollection.getItemCount("1_23*"));
        assertEquals(3, resultCollection.getItemCount("1_237T*"));
        assertEquals(3, resultCollection.getItemCount("15_2"));
        assertEquals(3, resultCollection.getItemCount("15_4*"));
        assertEquals(2, resultCollection.getItemCount("FotR - Booster"));
    }

    @Test
    public void deserializeFromBytes() throws IOException {
        InputStream is = CollectionSerializerTest.class.getResourceAsStream("/testCollection");
        try {
            CardCollection resultCollection = _serializer.deserializeCollection(is);
            assertEquals(6, Iterables.size(resultCollection.getAll()));
            assertEquals(256 * 256 * 250, resultCollection.getCurrency());
            assertEquals(2, resultCollection.getItemCount("1_1"));
            assertEquals(3, resultCollection.getItemCount("1_231T"));
            assertEquals(3, resultCollection.getItemCount("1_23*"));
            assertEquals(3, resultCollection.getItemCount("1_237T*"));
            assertEquals(2, resultCollection.getItemCount("FotR - Booster"));
            assertEquals(1, resultCollection.getItemCount("15_2"));
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void deserializeFromBytesV4() throws IOException {
        InputStream is = CollectionSerializerTest.class.getResourceAsStream("/testCollection-v4");
        try {
            CardCollection resultCollection = _serializer.deserializeCollection(is);
            assertEquals(7, Iterables.size(resultCollection.getAll()));
            assertEquals(256 * 256 * 250, resultCollection.getCurrency());
            assertEquals(2, resultCollection.getItemCount("1_1"));
            assertEquals(3, resultCollection.getItemCount("1_231T"));
            assertEquals(3, resultCollection.getItemCount("1_23*"));
            assertEquals(3, resultCollection.getItemCount("1_237T*"));
            assertEquals(3, resultCollection.getItemCount("15_2"));
            assertEquals(3, resultCollection.getItemCount("15_4*"));
            assertEquals(2, resultCollection.getItemCount("FotR - Booster"));
            assertEquals(2, resultCollection.getExtraInformation().size());
            assertEquals("b", resultCollection.getExtraInformation().get("a"));
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void testExtraInfo() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addCurrency(12);
        collection.addItem("15_4*", 2);

        collection.setExtraInformation(Collections.singletonMap("a", "b"));

        CardCollection resultCollection = serializeAndDeserialize(collection);
        assertEquals(12, resultCollection.getCurrency());
        assertEquals(1, Iterables.size(resultCollection.getAll()));
        assertEquals(2, resultCollection.getItemCount("15_4*"));
        //added entry + the default currency storage
        assertEquals(2, resultCollection.getExtraInformation().size());
        assertEquals("b", resultCollection.getExtraInformation().get("a"));
    }

    @Test
    public void testJustPack() throws IOException {
        DefaultCardCollection collection = new DefaultCardCollection();
        collection.addItem("FotR - Booster", 8);

        CardCollection resultCollection = serializeAndDeserialize(collection);
        assertEquals(1, Iterables.size(resultCollection.getAll()));
        assertEquals(8, resultCollection.getItemCount("FotR - Booster"));
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

        CardCollection resultCollection = serializeAndDeserialize(collection);
        assertEquals(1, Iterables.size(resultCollection.getAll()));
        assertEquals(500, resultCollection.getItemCount("FotR - Booster"));
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
