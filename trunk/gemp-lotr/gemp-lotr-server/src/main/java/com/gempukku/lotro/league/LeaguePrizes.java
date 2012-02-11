package com.gempukku.lotro.league;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.*;

public class LeaguePrizes {
    private Map<String, List<String>> _blockPromos = new HashMap<String, List<String>>();
    private Map<String, List<String>> _blockCommons = new HashMap<String, List<String>>();
    private Map<String, List<String>> _blockUncommons = new HashMap<String, List<String>>();

    public LeaguePrizes() {
        List<String> fotrPromos = new ArrayList<String>();
        fotrPromos.add("0_1");
        fotrPromos.add("0_2");
        fotrPromos.add("0_3");
        fotrPromos.add("0_4");
        fotrPromos.add("0_5");
        fotrPromos.add("0_6");
        fotrPromos.add("0_7");
        fotrPromos.add("0_8");
        fotrPromos.add("0_9");
        fotrPromos.add("0_10");
        fotrPromos.add("0_11");
        fotrPromos.add("0_12");
        fotrPromos.add("0_13");
        fotrPromos.add("0_14");
        fotrPromos.add("0_15");
        fotrPromos.add("0_34");
        fotrPromos.add("0_36");
        fotrPromos.add("0_37");
        fotrPromos.add("0_41");
        fotrPromos.add("0_42");
        fotrPromos.add("0_43");
        _blockPromos.put("fotr_block", fotrPromos);

        List<String> tttPromos = new ArrayList<String>();
        tttPromos.add("0_16");
        tttPromos.add("0_17");
        tttPromos.add("0_18");
        tttPromos.add("0_19");
        tttPromos.add("0_21");
        tttPromos.add("0_22");
        tttPromos.add("0_30");
        tttPromos.add("0_32");
        tttPromos.add("0_33");
        tttPromos.add("0_35");
        tttPromos.add("0_38");
        tttPromos.add("0_39");
        tttPromos.add("0_40");
        tttPromos.add("0_44");
        tttPromos.add("0_45");
        tttPromos.add("0_46");
        tttPromos.add("0_47");
        _blockPromos.put("ttt_block", tttPromos);

        RarityReader rarityReader = new RarityReader();
        SetRarity fotrRarity = rarityReader.getSetRarity("1");
        SetRarity momRarity = rarityReader.getSetRarity("2");
        SetRarity rotelRarity = rarityReader.getSetRarity("3");

        SetRarity tttRarity = rarityReader.getSetRarity("4");
        SetRarity bohdRarity = rarityReader.getSetRarity("5");
        SetRarity eofRarity = rarityReader.getSetRarity("6");

        List<String> fotrCommons = new ArrayList<String>();
        fotrCommons.addAll(fotrRarity.getCardsOfRarity("C"));
        fotrCommons.addAll(momRarity.getCardsOfRarity("C"));
        fotrCommons.addAll(rotelRarity.getCardsOfRarity("C"));
        _blockCommons.put("fotr_block", fotrCommons);

        List<String> fotrUncommons = new ArrayList<String>();
        fotrUncommons.addAll(fotrRarity.getCardsOfRarity("U"));
        fotrUncommons.addAll(momRarity.getCardsOfRarity("U"));
        fotrUncommons.addAll(rotelRarity.getCardsOfRarity("U"));
        _blockUncommons.put("fotr_block", fotrUncommons);

        List<String> tttCommons = new ArrayList<String>();
        tttCommons.addAll(tttRarity.getCardsOfRarity("C"));
        tttCommons.addAll(bohdRarity.getCardsOfRarity("C"));
        tttCommons.addAll(eofRarity.getCardsOfRarity("C"));
        _blockCommons.put("ttt_block", tttCommons);

        List<String> tttUncommons = new ArrayList<String>();
        tttUncommons.addAll(tttRarity.getCardsOfRarity("U"));
        tttUncommons.addAll(bohdRarity.getCardsOfRarity("U"));
        tttUncommons.addAll(eofRarity.getCardsOfRarity("U"));
        _blockUncommons.put("ttt_block", tttUncommons);
    }

    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie, League league, LeagueSerie leagueSerie) {
        DefaultCardCollection winnerPrize = new DefaultCardCollection();
        if (winCountThisSerie == 1 || winCountThisSerie == 3 || winCountThisSerie == 5 || winCountThisSerie == 8 || winCountThisSerie == 10)
            winnerPrize.addItem("(S)Booster Choice", 1);
        else {
            List<String> blockCommons = _blockCommons.get(leagueSerie.getFormat());
            List<String> blockUncommons = _blockUncommons.get(leagueSerie.getFormat());
            List<String> blockPromos = _blockPromos.get(leagueSerie.getFormat());
            if (winCountThisSerie == 2)
                winnerPrize.addItem(getRandom(blockCommons) + "*", 1);
            else if (winCountThisSerie == 4)
                winnerPrize.addItem(getRandom(blockPromos), 1);
            else if (winCountThisSerie == 6)
                winnerPrize.addItem(getRandom(blockPromos) + "*", 1);
            else if (winCountThisSerie == 7)
                winnerPrize.addItem(getRandom(blockUncommons) + "*", 1);
            else if (winCountThisSerie == 9) {
                winnerPrize.addItem(getRandom(blockPromos), 1);
                winnerPrize.addItem(getRandom(blockCommons) + "*", 1);
            }
        }
        return winnerPrize;
    }

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie, League league, LeagueSerie leagueSerie) {
        return null;
    }

    private String getRandom(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
