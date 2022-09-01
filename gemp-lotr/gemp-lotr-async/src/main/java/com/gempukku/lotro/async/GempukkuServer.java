package com.gempukku.lotro.async;

import com.gempukku.lotro.builder.DaoBuilder;
import com.gempukku.lotro.builder.PacksStorageBuilder;
import com.gempukku.lotro.builder.ServerBuilder;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.service.LoggedUserHolder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GempukkuServer {
    private final Map<Type, Object> context;

    public GempukkuServer() {
        Map<Type, Object> objects = new HashMap<>();
        final LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        LoggedUserHolder loggedUserHolder = new LoggedUserHolder();
        loggedUserHolder.start();
        objects.put(LoggedUserHolder.class, loggedUserHolder);

        objects.put(LotroCardBlueprintLibrary.class, library);
        objects.put(PacksStorage.class, PacksStorageBuilder.createPacksStorage(library));
        DaoBuilder.fillObjectMap(objects);
        ServerBuilder.fillObjectMap(objects);
        ServerBuilder.constructObjects(objects);

        context = objects;
    }

    public Map<Type, Object> getContext() {
        return context;
    }
}
