package com.gempukku.lotro.builder;

import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.packs.*;
import org.apache.log4j.Logger;

public class PacksStorageBuilder {
    private static final Logger _logger = Logger.getLogger(PacksStorageBuilder.class);

    public static PacksStorage createPacksStorage(CardSets cardSets) {
        try {
            PacksStorage packStorage = new PacksStorage();
            packStorage.addPackBox("FotR - League Starter", new LeagueStarterBox());
            packStorage.addPackBox("Random FotR Foil Common", new RandomFoilPack("C", new String[]{"1", "2", "3"}, cardSets));
            packStorage.addPackBox("Random FotR Foil Uncommon", new RandomFoilPack("U", new String[]{"1", "2", "3"}, cardSets));

            packStorage.addPackBox("(S)FotR - Tengwar", new TengwarPackBox(new int[]{1, 2, 3}, cardSets));
            packStorage.addPackBox("(S)TTT - Tengwar", new TengwarPackBox(new int[]{4, 5, 6}, cardSets));
            packStorage.addPackBox("(S)RotK - Tengwar", new TengwarPackBox(new int[]{7, 8, 10}, cardSets));
            packStorage.addPackBox("(S)SH - Tengwar", new TengwarPackBox(new int[]{11, 12, 13}, cardSets));
            packStorage.addPackBox("(S)Tengwar", new TengwarPackBox(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, cardSets));

            packStorage.addPackBox("FotR - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("1")));
            packStorage.addPackBox("MoM - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("2")));
            packStorage.addPackBox("RotEL - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("3")));

            packStorage.addPackBox("TTT - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("4")));
            packStorage.addPackBox("BoHD - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("5")));
            packStorage.addPackBox("EoF - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("6")));

            packStorage.addPackBox("RotK - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("7")));
            packStorage.addPackBox("SoG - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("8")));
            packStorage.addPackBox("MD - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("10")));

            packStorage.addPackBox("SH - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("11")));
            packStorage.addPackBox("BR - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("12")));
            packStorage.addPackBox("BL - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("13")));

            packStorage.addPackBox("HU - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("15")));
            packStorage.addPackBox("RoS - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("17")));
            packStorage.addPackBox("TaD - Booster", new RarityPackBox(cardSets.getSetDefinitions().get("18")));

            packStorage.addPackBox("REF - Booster", new ReflectionsPackBox(cardSets));

            return packStorage;
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        return null;
    }
}
