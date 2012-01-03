package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CollectionSerializerTest {
    @Test
    public void testSerializeDeserialize() throws IOException {
        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        DefaultCardCollection collection = new DefaultCardCollection();
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
        collection.addItem("FotR - Booster", 8);

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
