package com.gempukku.lotro.league;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.*;

public class NewLeaguePrizes implements LeaguePrizes {
    private List<String> _commons = new ArrayList<String>();
    private List<String> _uncommons = new ArrayList<String>();
    private List<String> _promos = new ArrayList<String>();

    public NewLeaguePrizes() {
        RarityReader rarityReader = new RarityReader();

        for (int i = 0; i <= 19; i++) {
            SetRarity setRarity = rarityReader.getSetRarity(String.valueOf(i));
            _commons.addAll(setRarity.getCardsOfRarity("C"));
            _uncommons.addAll(setRarity.getCardsOfRarity("U"));
            _promos.addAll(setRarity.getCardsOfRarity("P"));
        }
    }

    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie, String format) {
        DefaultCardCollection winnerPrize = new DefaultCardCollection();
        if (winCountThisSerie == 1 || winCountThisSerie == 3 || winCountThisSerie == 5 || winCountThisSerie == 8 || winCountThisSerie == 10)
            winnerPrize.addItem("(S)Booster Choice", 1);
        else {
            if (winCountThisSerie == 2)
                winnerPrize.addItem(getRandom(_commons) + "*", 1);
            else if (winCountThisSerie == 4)
                winnerPrize.addItem(getRandom(_promos), 1);
            else if (winCountThisSerie == 6)
                winnerPrize.addItem(getRandom(_promos) + "*", 1);
            else if (winCountThisSerie == 7)
                winnerPrize.addItem(getRandom(_uncommons) + "*", 1);
            else if (winCountThisSerie == 9) {
                winnerPrize.addItem(getRandom(_promos), 1);
                winnerPrize.addItem(getRandom(_commons) + "*", 1);
            }
        }
        return winnerPrize;
    }

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie, String format) {
        return null;
    }

    public CardCollection getPrizeForLeague(int position, int playersCount, float multiplier, String format) {
        DefaultCardCollection leaguePrize = new DefaultCardCollection();
        int count = (int) Math.floor((2 * playersCount + 24) / (position + 9) - 2.4);
        if (count > 0) {
            count = 1 + (int) Math.floor((count - 1) * multiplier);
            leaguePrize.addItem("(S)Booster Choice", count);
        }
        int tengwar = getTengwarCount(position);
        if (tengwar > 0)
            leaguePrize.addItem("(S)Tengwar", tengwar);

        if (position == 1) {
            addPrizes(leaguePrize, getRandomFoil(_promos, 3));
        } else if (position == 2) {
            addPrizes(leaguePrize, getRandomFoil(_promos, 2));
            addPrizes(leaguePrize, getRandom(_promos, 1));
        } else if (position == 3) {
            addPrizes(leaguePrize, getRandomFoil(_promos, 1));
            addPrizes(leaguePrize, getRandom(_promos, 2));
        } else if (position >= 4 && position <= 6) {
            addPrizes(leaguePrize, getRandom(_promos, 7 - position));
        } else if (position >= 9 && position <= 12) {
            addPrizes(leaguePrize, getRandom(_promos, 1));
        }

        if (leaguePrize.getAll().size() == 0)
            return null;
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
