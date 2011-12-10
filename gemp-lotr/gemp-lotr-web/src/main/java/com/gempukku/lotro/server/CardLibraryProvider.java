package com.gempukku.lotro.server;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class CardLibraryProvider implements InjectableProvider<Context, Type>, Injectable<LotroCardBlueprintLibrary> {
    private LotroCardBlueprintLibrary _library;

    public CardLibraryProvider() {
        _library = new LotroCardBlueprintLibrary();
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    @Override
    public LotroCardBlueprintLibrary getValue() {
        return _library;
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(LotroCardBlueprintLibrary.class))
            return this;
        return null;
    }
}
