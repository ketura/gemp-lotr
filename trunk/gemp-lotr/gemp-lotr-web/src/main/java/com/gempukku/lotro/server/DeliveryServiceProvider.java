package com.gempukku.lotro.server;

import com.gempukku.lotro.collection.DeliveryService;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class DeliveryServiceProvider implements Injectable<DeliveryService>, InjectableProvider<Context, Type> {
    private DeliveryService _deliveryService;

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(DeliveryService.class))
            return this;
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    private DeliveryService createDeliveryService() {
        return new DeliveryService();
    }

    @Override
    public synchronized DeliveryService getValue() {
        if (_deliveryService == null)
            _deliveryService = createDeliveryService();
        return _deliveryService;
    }
}
