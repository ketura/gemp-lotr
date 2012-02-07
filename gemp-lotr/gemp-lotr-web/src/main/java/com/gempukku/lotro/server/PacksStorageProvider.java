package com.gempukku.lotro.server;

import com.gempukku.lotro.packs.FixedPackBox;
import com.gempukku.lotro.packs.LeagueStarterBox;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.packs.RarityPackBox;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Type;

@Provider
public class PacksStorageProvider implements Injectable<PacksStorage>, InjectableProvider<Context, Type> {
    private static final Logger _logger = Logger.getLogger(PacksStorageProvider.class);
    private PacksStorage _packsStorage;

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(PacksStorage.class))
            return this;
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    private PacksStorage createPacksStorage() {
        try {
            PacksStorage packStorage = new PacksStorage();
            packStorage.addPackBox("FotR - League Starter", new LeagueStarterBox());

            packStorage.addPackBox("(S)FotR - Starter", new FixedPackBox("(S)FotR - Starter"));
            packStorage.addPackBox("(S)MoM - Starter", new FixedPackBox("(S)MoM - Starter"));
            packStorage.addPackBox("(S)RotEL - Starter", new FixedPackBox("(S)RotEL - Starter"));

            packStorage.addPackBox("(S)TTT - Starter", new FixedPackBox("(S)TTT - Starter"));
            packStorage.addPackBox("(S)BoHD - Starter", new FixedPackBox("(S)BoHD - Starter"));
            packStorage.addPackBox("(S)EoF - Starter", new FixedPackBox("(S)EoF - Starter"));

            packStorage.addPackBox("(S)FotR - Tengwar", new FixedPackBox("(S)FotR - Tengwar"));
            packStorage.addPackBox("(S)Booster Choice", new FixedPackBox("(S)Booster Choice"));

            packStorage.addPackBox("FotR - Gandalf Starter", new FixedPackBox("FotR - Gandalf Starter"));
            packStorage.addPackBox("FotR - Aragorn Starter", new FixedPackBox("FotR - Aragorn Starter"));

            packStorage.addPackBox("MoM - Gandalf Starter", new FixedPackBox("MoM - Gandalf Starter"));
            packStorage.addPackBox("MoM - Gimli Starter", new FixedPackBox("MoM - Gimli Starter"));

            packStorage.addPackBox("RotEL - Boromir Starter", new FixedPackBox("RotEL - Boromir Starter"));
            packStorage.addPackBox("RotEL - Legolas Starter", new FixedPackBox("RotEL - Legolas Starter"));

            packStorage.addPackBox("TTT - Aragorn Starter", new FixedPackBox("TTT - Aragorn Starter"));
            packStorage.addPackBox("TTT - Theoden Starter", new FixedPackBox("TTT - Theoden Starter"));

            packStorage.addPackBox("BoHD - Eowyn Starter", new FixedPackBox("BoHD - Eowyn Starter"));
            packStorage.addPackBox("BoHD - Legolas Starter", new FixedPackBox("BoHD - Legolas Starter"));

            packStorage.addPackBox("EoF - Faramir Starter", new FixedPackBox("EoF - Faramir Starter"));
            packStorage.addPackBox("EoF - Witch-king Starter", new FixedPackBox("EoF - Witch-king Starter"));

            packStorage.addPackBox("FotR - Booster", new RarityPackBox(1));
            packStorage.addPackBox("MoM - Booster", new RarityPackBox(2));
            packStorage.addPackBox("RotEL - Booster", new RarityPackBox(3));

            packStorage.addPackBox("TTT - Booster", new RarityPackBox(4));
            packStorage.addPackBox("BoHD - Booster", new RarityPackBox(5));
            packStorage.addPackBox("EoF - Booster", new RarityPackBox(6));

            return packStorage;
        } catch (IOException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized PacksStorage getValue() {
        if (_packsStorage == null)
            _packsStorage = createPacksStorage();
        return _packsStorage;
    }
}
