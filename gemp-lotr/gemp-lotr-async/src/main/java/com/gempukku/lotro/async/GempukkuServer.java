package com.gempukku.lotro.async;

import com.gempukku.lotro.async.handler.RootUriRequestHandler;
import com.gempukku.lotro.async.handler.UriRequestHandler;
import com.gempukku.lotro.builder.DaoBuilder;
import com.gempukku.lotro.builder.PacksStorageBuilder;
import com.gempukku.lotro.builder.ServerBuilder;
import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.service.LoggedUserHolder;
import com.gempukku.polling.LongPollingSystem;
import org.jboss.netty.channel.ChannelHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GempukkuServer {
    private Map<Type, Object> context;

    public GempukkuServer() {
        Map<Type, Object> objects = new HashMap<Type, Object>();
        final CardSets cardSets = new CardSets();

        LoggedUserHolder loggedUserHolder = new LoggedUserHolder();
        loggedUserHolder.start();
        objects.put(LoggedUserHolder.class, loggedUserHolder);

        objects.put(CardSets.class, cardSets);
        objects.put(PacksStorage.class, PacksStorageBuilder.createPacksStorage(cardSets));
        DaoBuilder.fillObjectMap(objects);
        ServerBuilder.fillObjectMap(objects);
        ServerBuilder.constructObjects(objects);

        context = objects;
    }

    public Map<Type, Object> getContext() {
        return context;
    }
}
