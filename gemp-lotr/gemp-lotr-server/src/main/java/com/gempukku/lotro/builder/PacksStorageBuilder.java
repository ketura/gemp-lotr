package com.gempukku.lotro.builder;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.packs.*;
import org.apache.log4j.Logger;

public class PacksStorageBuilder {
    private static final Logger _logger = Logger.getLogger(PacksStorageBuilder.class);

    public static PacksStorage createPacksStorage(LotroCardBlueprintLibrary library) {
        try {
            PacksStorage packStorage = new PacksStorage();
            packStorage.addPackBox("FotR - League Starter", new LeagueStarterBox());
            packStorage.addPackBox("Random FotR Foil Common", new RandomFoilPack("C", new String[]{"1", "2", "3"}, library));
            packStorage.addPackBox("Random FotR Foil Uncommon", new RandomFoilPack("U", new String[]{"1", "2", "3"}, library));

            packStorage.addPackBox("(S)FotR - Tengwar", new TengwarPackBox(new String[]{"1", "2", "3"}, library));
            packStorage.addPackBox("(S)TTT - Tengwar", new TengwarPackBox(new String[]{"4", "5", "6"}, library));
            packStorage.addPackBox("(S)RotK - Tengwar", new TengwarPackBox(new String[]{"7", "8", "10"}, library));
            packStorage.addPackBox("(S)SH - Tengwar", new TengwarPackBox(new String[]{"11", "12", "13"}, library));
            packStorage.addPackBox("(S)Tengwar", new TengwarPackBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"}, library));

            packStorage.addPackBox("FotR - Booster", new RarityPackBox(library.getSetDefinitions().get("1")));
            packStorage.addPackBox("MoM - Booster", new RarityPackBox(library.getSetDefinitions().get("2")));
            packStorage.addPackBox("RotEL - Booster", new RarityPackBox(library.getSetDefinitions().get("3")));

            packStorage.addPackBox("TTT - Booster", new RarityPackBox(library.getSetDefinitions().get("4")));
            packStorage.addPackBox("BoHD - Booster", new RarityPackBox(library.getSetDefinitions().get("5")));
            packStorage.addPackBox("EoF - Booster", new RarityPackBox(library.getSetDefinitions().get("6")));

            packStorage.addPackBox("RotK - Booster", new RarityPackBox(library.getSetDefinitions().get("7")));
            packStorage.addPackBox("SoG - Booster", new RarityPackBox(library.getSetDefinitions().get("8")));
            packStorage.addPackBox("MD - Booster", new RarityPackBox(library.getSetDefinitions().get("10")));

            packStorage.addPackBox("SH - Booster", new RarityPackBox(library.getSetDefinitions().get("11")));
            packStorage.addPackBox("BR - Booster", new RarityPackBox(library.getSetDefinitions().get("12")));
            packStorage.addPackBox("BL - Booster", new RarityPackBox(library.getSetDefinitions().get("13")));

            packStorage.addPackBox("HU - Booster", new RarityPackBox(library.getSetDefinitions().get("15")));
            packStorage.addPackBox("RoS - Booster", new RarityPackBox(library.getSetDefinitions().get("17")));
            packStorage.addPackBox("TaD - Booster", new RarityPackBox(library.getSetDefinitions().get("18")));

            packStorage.addPackBox("REF - Booster", new ReflectionsPackBox(library));

            return packStorage;
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        return null;
    }
}
