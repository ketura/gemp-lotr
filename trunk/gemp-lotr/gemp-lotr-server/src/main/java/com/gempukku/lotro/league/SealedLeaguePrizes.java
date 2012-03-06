package com.gempukku.lotro.league;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.*;

public class SealedLeaguePrizes {
    private Map<String, List<String>> _blockPromos = new HashMap<String, List<String>>();
    private Map<String, List<String>> _blockCommons = new HashMap<String, List<String>>();
    private Map<String, List<String>> _blockUncommons = new HashMap<String, List<String>>();
    private static final String FOTR_BLOCK = "fotr_block";
    private static final String TTT_BLOCK = "ttt_block";

    public SealedLeaguePrizes() {
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
        _blockPromos.put(FOTR_BLOCK, fotrPromos);

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
        _blockPromos.put(TTT_BLOCK, tttPromos);

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
        _blockCommons.put(FOTR_BLOCK, fotrCommons);

        List<String> fotrUncommons = new ArrayList<String>();
        fotrUncommons.addAll(fotrRarity.getCardsOfRarity("U"));
        fotrUncommons.addAll(momRarity.getCardsOfRarity("U"));
        fotrUncommons.addAll(rotelRarity.getCardsOfRarity("U"));
        _blockUncommons.put(FOTR_BLOCK, fotrUncommons);

        List<String> tttCommons = new ArrayList<String>();
        tttCommons.addAll(tttRarity.getCardsOfRarity("C"));
        tttCommons.addAll(bohdRarity.getCardsOfRarity("C"));
        tttCommons.addAll(eofRarity.getCardsOfRarity("C"));
        _blockCommons.put(TTT_BLOCK, tttCommons);

        List<String> tttUncommons = new ArrayList<String>();
        tttUncommons.addAll(tttRarity.getCardsOfRarity("U"));
        tttUncommons.addAll(bohdRarity.getCardsOfRarity("U"));
        tttUncommons.addAll(eofRarity.getCardsOfRarity("U"));
        _blockUncommons.put(TTT_BLOCK, tttUncommons);
    }

    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie, String format) {
        DefaultCardCollection winnerPrize = new DefaultCardCollection();
        if (winCountThisSerie == 1 || winCountThisSerie == 3 || winCountThisSerie == 5 || winCountThisSerie == 8 || winCountThisSerie == 10)
            winnerPrize.addItem("(S)Booster Choice", 1);
        else {
            List<String> blockCommons = _blockCommons.get(format);
            List<String> blockUncommons = _blockUncommons.get(format);
            List<String> blockPromos = _blockPromos.get(format);
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

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie, String format) {
        return null;
    }

    public CardCollection getPrizeForLeague(int position, int playersCount, String format) {
        DefaultCardCollection leaguePrize = new DefaultCardCollection();
        int count = (int) Math.floor((2 * playersCount + 24) / (position + 9) - 2.4);
        if (count > 0)
            leaguePrize.addItem("(S)Booster Choice", count);
        int tengwar = getTengwarCount(position);
        if (tengwar > 0) {
            if (format.equals(FOTR_BLOCK))
                leaguePrize.addItem("(S)FotR - Tengwar", tengwar);
            else if (format.equals(TTT_BLOCK))
                leaguePrize.addItem("(S)TTT - Tengwar", tengwar);
        }
        if (position == 1) {
            addPrizes(leaguePrize, getRandomFoil(_blockPromos.get(format), 3));
        } else if (position == 2) {
            addPrizes(leaguePrize, getRandomFoil(_blockPromos.get(format), 2));
            addPrizes(leaguePrize, getRandom(_blockPromos.get(format), 1));
        } else if (position == 3) {
            addPrizes(leaguePrize, getRandomFoil(_blockPromos.get(format), 1));
            addPrizes(leaguePrize, getRandom(_blockPromos.get(format), 2));
        } else if (position >= 4 && position <= 6) {
            addPrizes(leaguePrize, getRandom(_blockPromos.get(format), 7 - position));
        } else if (position >= 9 && position <= 12) {
            addPrizes(leaguePrize, getRandom(_blockPromos.get(format), 1));
        }

        return leaguePrize;
    }

    private void addPrizes(DefaultCardCollection leaguePrize, List<String> cards) {
        for (String card : cards)
            leaguePrize.addItem(card, 1);
    }

    private int getTengwarCount(int position) {
        if (position == 1)
            return 4;
        else if (position == 2)
            return 3;
        else if (position == 3)
            return 2;
        else if (position <= 8)
            return 1;
        return 0;
    }

    private String getRandom(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    private List<String> getRandom(List<String> list, int count) {
        List<String> result = new LinkedList<String>(list);
        Collections.shuffle(result);
        return result.subList(0, count);
    }

    private List<String> getRandomFoil(List<String> list, int count) {
        List<String> result = new LinkedList<String>();
        for (String element : list)
            result.add(element + "*");
        Collections.shuffle(result);
        return result.subList(0, count);
    }
}
