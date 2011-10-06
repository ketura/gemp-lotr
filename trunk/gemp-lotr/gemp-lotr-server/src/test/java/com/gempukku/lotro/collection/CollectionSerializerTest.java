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

        DefaultCardCollection collection = new DefaultCardCollection(library);
        collection.addCards("1_1", null, 2);
        collection.addCards("1_231T", null, 3);
        collection.addCards("1_23*", null, 3);
        collection.addCards("1_237T*", null, 3);
        collection.addPacks("Fellowship of the Ring", 2);

        CollectionSerializer serializer = new CollectionSerializer(library);

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
        assertEquals(2, (int) result.get("Fellowship of the Ring"));
    }
}
