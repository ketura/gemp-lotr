package com.gempukku.lotro.server.provider;

import com.gempukku.lotro.packs.PacksStorage;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.Context;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapProvider implements InjectableProvider<Context, Type> {
    private static final Logger _logger = Logger.getLogger(PacksStorageBuilder.class);
    private Map<Type, Object> _objectMap = new HashMap<Type, Object>();

    public MapProvider() {
        try {
            _objectMap.put(PacksStorage.class, PacksStorageBuilder.createPacksStorage());
            DaoBuilder.fillObjectMap(_objectMap);
            ServerBuilder.fillObjectMap(_objectMap);
        } catch (Exception exp) {
            _logger.error("Unable to startup the server", exp);
        }
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    @PostConstruct
    public void constructObjects() {
        ServerBuilder.constructObjects(_objectMap);
    }

    @PreDestroy
    public void destroyObjects() {
        ServerBuilder.destroyObjects(_objectMap);
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, final Type type) {
        return new Injectable() {
            @Override
            public Object getValue() {
                return _objectMap.get(type);
            }
        };
    }
}
