package com.gempukku.lotro.league;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.*;

public class SealedLeaguePrizes implements LeaguePrizes {
    private List<String> _commons = new ArrayList<String>();
    private List<String> _uncommons = new ArrayList<String>();
    private List<String> _promos = new ArrayList<String>();
    private List<String> _rares = new ArrayList<String>();

    public SealedLeaguePrizes() {
        RarityReader rarityReader = new RarityReader();

        for (int i = 0; i <= 19; i++) {
            SetRarity setRarity = rarityReader.getSetRarity(String.valueOf(i));
            _commons.addAll(setRarity.getCardsOfRarity("C"));
            _uncommons.addAll(setRarity.getCardsOfRarity("U"));
            _promos.addAll(setRarity.getCardsOfRarity("P"));
            _rares.addAll(setRarity.getCardsOfRarity("R"));
        }
    }

    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        DefaultCardCollection winnerPrize = new DefaultCardCollection();
        if (winCountThisSerie == 1 || winCountThisSerie == 3 || winCountThisSerie == 5 || winCountThisSerie == 8
                || winCountThisSerie == 10 || winCountThisSerie == 12)
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
            } else if (winCountThisSerie == 11)
                winnerPrize.addItem(getRandom(_promos), 1);
            else if (winCountThisSerie == 13) {
                winnerPrize.addItem(getRandom(_promos)+"*", 1);
                winnerPrize.addItem(getRandom(_rares)+"*", 1);
            }

        }
        return winnerPrize;
    }

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        return null;
    }

    public CardCollection getPrizeForLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed, float multiplier) {
        if (gamesPlayed*4>=maxGamesPlayed) {
            DefaultCardCollection leaguePrize = new DefaultCardCollection();
            leaguePrize.addItem("(S)Booster Choice", getBoosterCount(position));
            leaguePrize.addItem("(S)Tengwar", getTengwarCount(position));
            addPrizes(leaguePrize, getRandomFoil(_promos, getFoilCount(position)));
            addPrizes(leaguePrize, getRandom(_promos, getPromoCount(position)));

            if (leaguePrize.getAll().size() == 0)
                return null;
            return leaguePrize;
        } else {
            return null;
        }
    }

    private int getFoilCount(int position) {
        if (position<=3)
            return 4-position;
        else
            return 0;
    }

    private int getPromoCount(int position) {
        if (position<=4)
            return position-1;
        else if (position<=8)
            return 2;
        else if (position<=16)
            return 1;
        else
            return 0;
    }

    private int getTengwarCount(int position) {
        if (position<=4)
            return 5-position;
        else
            return 0;
    }

    private int getBoosterCount(int position) {
        if (position == 1)
            return 60;
        else if (position == 2)
            return 55;
        else if (position == 3)
            return 50;
        else if (position == 4)
            return 45;
        else if (position<=8)
            return 40;
        else if (position<=16)
            return 35;
        else if (position<=32)
            return 20;
        else if (position<=64)
            return 10;
        else if (position<=128)
            return 5;
        else
            return 0;
    }

    private void addPrizes(DefaultCardCollection leaguePrize, List<String> cards) {
        for (String card : cards)
            leaguePrize.addItem(card, 1);
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
