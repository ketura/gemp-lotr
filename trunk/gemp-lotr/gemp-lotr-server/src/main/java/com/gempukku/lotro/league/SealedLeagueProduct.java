package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SealedLeagueProduct {
    private Map<String, List<CardCollection>> _collections = new HashMap<String, List<CardCollection>>();

    public SealedLeagueProduct() {
        createFellowshipBlock();
        createTowersBlock();
    }

    private void createFellowshipBlock() {
        List<CardCollection> fotrBlock = new ArrayList<CardCollection>();

        MutableCardCollection firstWeek = new DefaultCardCollection();
        firstWeek.addItem("(S)FotR - Starter", 1);
        firstWeek.addItem("FotR - Booster", 6);
        firstWeek.addItem("1_231", 2);
        fotrBlock.add(firstWeek);

        MutableCardCollection secondWeek = new DefaultCardCollection();
        secondWeek.addItem("(S)MoM - Starter", 1);
        secondWeek.addItem("MoM - Booster", 3);
        secondWeek.addItem("2_51", 1);
        fotrBlock.add(secondWeek);

        MutableCardCollection thirdWeek = new DefaultCardCollection();
        thirdWeek.addItem("(S)RotEL - Starter", 1);
        thirdWeek.addItem("RotEL - Booster", 3);
        fotrBlock.add(thirdWeek);

        MutableCardCollection fourthWeek = new DefaultCardCollection();
        fourthWeek.addItem("FotR - Booster", 2);
        fourthWeek.addItem("MoM - Booster", 2);
        fourthWeek.addItem("RotEL - Booster", 2);
        fotrBlock.add(fourthWeek);

        _collections.put("fotr_block", fotrBlock);
    }

    private void createTowersBlock() {
        List<CardCollection> tttBlock = new ArrayList<CardCollection>();

        MutableCardCollection firstWeek = new DefaultCardCollection();
        firstWeek.addItem("(S)TTT - Starter", 1);
        firstWeek.addItem("TTT - Booster", 6);
        firstWeek.addItem("4_356", 1);
        firstWeek.addItem("4_249", 2);
        tttBlock.add(firstWeek);

        MutableCardCollection secondWeek = new DefaultCardCollection();
        secondWeek.addItem("(S)BoHD - Starter", 1);
        secondWeek.addItem("BoHD - Booster", 3);
        tttBlock.add(secondWeek);

        MutableCardCollection thirdWeek = new DefaultCardCollection();
        thirdWeek.addItem("(S)EoF - Starter", 1);
        thirdWeek.addItem("EoF - Booster", 3);
        tttBlock.add(thirdWeek);

        MutableCardCollection fourthWeek = new DefaultCardCollection();
        fourthWeek.addItem("TTT - Booster", 2);
        fourthWeek.addItem("BoHD - Booster", 2);
        fourthWeek.addItem("EoF - Booster", 2);
        tttBlock.add(fourthWeek);

        _collections.put("ttt_block", tttBlock);
    }

    public CardCollection getCollectionForSerie(String format, int serieIndex) {
        return _collections.get(format).get(serieIndex);
    }
}
