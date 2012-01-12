package com.gempukku.lotro.server;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class CollectionsManagerProvider implements InjectableProvider<Context, Type> {
    private Injectable<CollectionsManager> _collectionsManagerInjectable;

    @Context
    private PlayerDAO _playerDao;
    @Context
    private CollectionDAO _collectionDao;

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(CollectionsManager.class))
            return getCollectionsManagerInjectable();
        return null;
    }

    private synchronized Injectable<CollectionsManager> getCollectionsManagerInjectable() {
        if (_collectionsManagerInjectable == null) {
            final CollectionsManager collectionsManager = new CollectionsManager(_playerDao, _collectionDao);
            _collectionsManagerInjectable = new Injectable<CollectionsManager>() {
                @Override
                public CollectionsManager getValue() {
                    return collectionsManager;
                }
            };
        }
        return _collectionsManagerInjectable;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }
}
