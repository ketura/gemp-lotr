package com.gempukku.lotro.builder;

import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.packs.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class PacksStorageBuilder {
    private static final Logger _logger = Logger.getLogger(PacksStorageBuilder.class);

    public static PacksStorage createPacksStorage(CardSets cardSets) {
        try {
            PacksStorage packStorage = new PacksStorage();
            packStorage.addPackBox("FotR - League Starter", new LeagueStarterBox());
            packStorage.addPackBox("Random FotR Foil Common", new RandomFoilPack("C", new String[]{"1", "2", "3"}, cardSets));
            packStorage.addPackBox("Random FotR Foil Uncommon", new RandomFoilPack("U", new String[]{"1", "2", "3"}, cardSets));

            packStorage.addPackBox("(S)FotR - Starter", new FixedPackBox("(S)FotR - Starter"));
            packStorage.addPackBox("(S)MoM - Starter", new FixedPackBox("(S)MoM - Starter"));
            packStorage.addPackBox("(S)RotEL - Starter", new FixedPackBox("(S)RotEL - Starter"));

            packStorage.addPackBox("(S)TTT - Starter", new FixedPackBox("(S)TTT - Starter"));
            packStorage.addPackBox("(S)BoHD - Starter", new FixedPackBox("(S)BoHD - Starter"));
            packStorage.addPackBox("(S)EoF - Starter", new FixedPackBox("(S)EoF - Starter"));

            packStorage.addPackBox("(S)SH - Starter", new FixedPackBox("(S)SH - Starter"));
            packStorage.addPackBox("(S)BR - Starter", new FixedPackBox("(S)BR - Starter"));
            packStorage.addPackBox("(S)BL - Starter", new FixedPackBox("(S)BL - Starter"));

            packStorage.addPackBox("(S)RotK - Starter", new FixedPackBox("(S)RotK - Starter"));
            packStorage.addPackBox("(S)SoG - Starter", new FixedPackBox("(S)SoG - Starter"));
            packStorage.addPackBox("(S)MD - Starter", new FixedPackBox("(S)MD - Starter"));

            packStorage.addPackBox("(S)FotR - Tengwar", new TengwarPackBox(new int[]{1, 2, 3}, cardSets));
            packStorage.addPackBox("(S)TTT - Tengwar", new TengwarPackBox(new int[]{4, 5, 6}, cardSets));
            packStorage.addPackBox("(S)RotK - Tengwar", new TengwarPackBox(new int[]{7, 8, 10}, cardSets));
            packStorage.addPackBox("(S)SH - Tengwar", new TengwarPackBox(new int[]{11, 12, 13}, cardSets));
            packStorage.addPackBox("(S)Tengwar", new TengwarPackBox(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, cardSets));

            packStorage.addPackBox("(S)Booster Choice", new FixedPackBox("(S)Booster Choice"));
            packStorage.addPackBox("(S)Movie Booster Choice", new FixedPackBox("(S)Movie Booster Choice"));

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

            packStorage.addPackBox("RotK - Aragorn Starter", new FixedPackBox("RotK - Aragorn Starter"));
            packStorage.addPackBox("RotK - Eomer Starter", new FixedPackBox("RotK - Eomer Starter"));

            packStorage.addPackBox("SoG - Merry Starter", new FixedPackBox("SoG - Merry Starter"));
            packStorage.addPackBox("SoG - Pippin Starter", new FixedPackBox("SoG - Pippin Starter"));

            packStorage.addPackBox("MD - Frodo Starter", new FixedPackBox("MD - Frodo Starter"));
            packStorage.addPackBox("MD - Sam Starter", new FixedPackBox("MD - Sam Starter"));

            packStorage.addPackBox("SH - Aragorn Starter", new FixedPackBox("SH - Aragorn Starter"));
            packStorage.addPackBox("SH - Eowyn Starter", new FixedPackBox("SH - Eowyn Starter"));
            packStorage.addPackBox("SH - Gandalf Starter", new FixedPackBox("SH - Gandalf Starter"));
            packStorage.addPackBox("SH - Legolas Starter", new FixedPackBox("SH - Legolas Starter"));

            packStorage.addPackBox("BR - Mouth Starter", new FixedPackBox("BR - Mouth Starter"));
            packStorage.addPackBox("BR - Saruman Starter", new FixedPackBox("BR - Saruman Starter"));

            packStorage.addPackBox("BL - Arwen Starter", new FixedPackBox("BL - Arwen Starter"));
            packStorage.addPackBox("BL - Boromir Starter", new FixedPackBox("BL - Boromir Starter"));

            packStorage.addPackBox("Expanded", new FixedPackBox("Expanded"));
            packStorage.addPackBox("Wraith", new FixedPackBox("Wraith"));
            packStorage.addPackBox("AgesEnd", new FixedPackBox("AgesEnd"));

            packStorage.addPackBox("(S)Special-1-3", new FixedPackBox("(S)Special-1-3"));
            packStorage.addPackBox("(S)Special-4-6", new FixedPackBox("(S)Special-4-6"));
            packStorage.addPackBox("(S)Special-7-9", new FixedPackBox("(S)Special-7-9"));

            packStorage.addPackBox("Special-01", new FixedPackBox("Special-01"));
            packStorage.addPackBox("Special-02", new FixedPackBox("Special-02"));
            packStorage.addPackBox("Special-03", new FixedPackBox("Special-03"));
            packStorage.addPackBox("Special-04", new FixedPackBox("Special-04"));
            packStorage.addPackBox("Special-05", new FixedPackBox("Special-05"));
            packStorage.addPackBox("Special-06", new FixedPackBox("Special-06"));
            packStorage.addPackBox("Special-07", new FixedPackBox("Special-07"));
            packStorage.addPackBox("Special-08", new FixedPackBox("Special-08"));
            packStorage.addPackBox("Special-09", new FixedPackBox("Special-09"));

            packStorage.addPackBox("(S)TSSealed-S1", new FixedPackBox("(S)TSSealed-S1"));
            packStorage.addPackBox("TSSealedS1D1", new FixedPackBox("TSSealedS1D1"));
            packStorage.addPackBox("TSSealedS1D2", new FixedPackBox("TSSealedS1D2"));
            packStorage.addPackBox("TSSealedS1D3", new FixedPackBox("TSSealedS1D3"));

            packStorage.addPackBox("(S)TSSealed-S2", new FixedPackBox("(S)TSSealed-S2"));
            packStorage.addPackBox("TSSealedS2D1", new FixedPackBox("TSSealedS2D1"));
            packStorage.addPackBox("TSSealedS2D2", new FixedPackBox("TSSealedS2D2"));
            packStorage.addPackBox("TSSealedS2D3", new FixedPackBox("TSSealedS2D3"));

            packStorage.addPackBox("(S)TSSealed-S3", new FixedPackBox("(S)TSSealed-S3"));
            packStorage.addPackBox("TSSealedS3D1", new FixedPackBox("TSSealedS3D1"));
            packStorage.addPackBox("TSSealedS3D2", new FixedPackBox("TSSealedS3D2"));
            packStorage.addPackBox("TSSealedS3D3", new FixedPackBox("TSSealedS3D3"));

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
        } catch (IOException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        return null;
    }
}
