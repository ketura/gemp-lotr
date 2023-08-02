package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.cards.sets.SetDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FixedLeaguePrizes implements LeaguePrizes {
    private final List<String> _commons = new ArrayList<>();
    private final List<String> _uncommons = new ArrayList<>();
    private final List<String> _rares = new ArrayList<>();

    public FixedLeaguePrizes(CardBlueprintLibrary library) {
        for (SetDefinition setDefinition : library.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("originalSet")) {
                _commons.addAll(setDefinition.getCardsOfRarity("C"));
                _uncommons.addAll(setDefinition.getCardsOfRarity("U"));
                _rares.addAll(setDefinition.getCardsOfRarity("R"));
            }
        }
    }

    @Override
    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        DefaultCardCollection winnerPrize = new DefaultCardCollection();
        if (winCountThisSerie % 2 == 1) {
            winnerPrize.addItem("(S)All Decipher Choice - Booster", 1);
        } else {
            if (winCountThisSerie <= 4) {
                winnerPrize.addItem(getRandom(_commons) + "*", 1);
            } else if (winCountThisSerie <= 8) {
                winnerPrize.addItem(getRandom(_uncommons) + "*", 1);
            } else {
                winnerPrize.addItem(getRandom(_rares) + "*", 1);
            }
        }
        return winnerPrize;
    }

    @Override
    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        return null;
    }

    @Override
    public CardCollection getPrizeForLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed, CollectionType collectionType) {
        if (collectionType.equals(CollectionType.ALL_CARDS)) {
            return getPrizeForConstructedLeague(position, playersCount, gamesPlayed, maxGamesPlayed);
        } else if (collectionType.equals(CollectionType.MY_CARDS) || collectionType.equals(CollectionType.OWNED_TOURNAMENT_CARDS)) {
            return getPrizeForCollectorsLeague(position, playersCount, gamesPlayed, maxGamesPlayed);
        } else {
            return getPrizeForSealedLeague(position, playersCount, gamesPlayed, maxGamesPlayed);
        }
    }

    @Override
    public CardCollection getTrophiesForLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed, CollectionType collectionType) {
        DefaultCardCollection prize = new DefaultCardCollection();
        prize.addItem("(S)Tengwar", getTengwarCount(position));
        if (prize.getAll().iterator().hasNext())
            return prize;
        return null;
    }

    //1st - 60 boosters, 4 tengwar, 3 foil rares
//2nd - 55 boosters, 3 tengwar, 2 foil rares
//3rd - 50 boosters, 2 tengwar, 1 foil rare
//4th - 45 boosters, 1 tengwar
//5th-8th - 40 boosters
//9th-16th - 35 boosters
//17th-32nd - 20 boosters
//33rd-64th - 10 boosters
//65th-128th - 5 boosters
    private CardCollection getPrizeForSealedLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed) {
        DefaultCardCollection prize = new DefaultCardCollection();
        prize.addItem("(S)All Decipher Choice - Booster", getSealedBoosterCount(position));
        addPrizes(prize, getRandomFoil(_rares, getRandomRareFoilCount(position)));
        if (prize.getAll().iterator().hasNext())
            return prize;
        return null;
    }

    private int getSealedBoosterCount(int position) {
        if (position < 5)
            return 65 - position * 5;
        else if (position < 9)
            return 40;
        else if (position < 17)
            return 35;
        else if (position < 33)
            return 20;
        else if (position < 65)
            return 10;
        else if (position < 129)
            return 5;
        return 0;
    }

    //1st - 30 boosters, 4 tengwar, 3 foil rares
//2nd - 25 boosters, 3 tengwar, 2 foil rares
//3rd - 20 boosters, 2 tengwar, 1 foil rares
//4th - 15 boosters, 1 tengwar
//5th-8th - 10 boosters
//9th-16th - 5 boosters
//17th-32nd - 2 boosters
    private CardCollection getPrizeForCollectorsLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed) {
        DefaultCardCollection prize = new DefaultCardCollection();
        prize.addItem("(S)All Decipher Choice - Booster", getCollectorsBoosterCount(position));
        addPrizes(prize, getRandomFoil(_rares, getRandomRareFoilCount(position)));
        if (prize.getAll().iterator().hasNext())
            return prize;
        return null;
    }

    private int getCollectorsBoosterCount(int position) {
        if (position < 5)
            return 35 - position * 5;
        else if (position < 9)
            return 10;
        else if (position < 17)
            return 5;
        else if (position < 33)
            return 2;
        return 0;
    }

    //1st - 10 boosters, 4 tengwar, 3 foil rares
//2nd - 8 boosters, 3 tengwar, 2 foil rares
//3rd - 6 boosters, 2 tengwar, 1 foil rare
//4th - 4 boosters, 1 tengwar
//5th-8th - 3 boosters
//9th-16th - 2 boosters
//17th-32nd - 1 boosters
    private CardCollection getPrizeForConstructedLeague(int position, int playersCount, int gamesPlayed, int maxGamesPlayed) {
        DefaultCardCollection prize = new DefaultCardCollection();
        prize.addItem("(S)All Decipher Choice - Booster", getConstructedBoosterCount(position));
        addPrizes(prize, getRandomFoil(_rares, getRandomRareFoilCount(position)));
        if (prize.getAll().iterator().hasNext())
            return prize;
        return null;
    }

    private int getConstructedBoosterCount(int position) {
        if (position < 5)
            return 12 - position * 2;
        else if (position < 9)
            return 3;
        else if (position < 17)
            return 2;
        else if (position < 33)
            return 1;
        return 0;
    }


    private int getRandomRareFoilCount(int position) {
        if (position < 4)
            return 4 - position;
        return 0;
    }

    private void addPrizes(DefaultCardCollection leaguePrize, List<String> cards) {
        for (String card : cards)
            leaguePrize.addItem(card, 1);
    }

    private int getTengwarCount(int position) {
        if (position < 5)
            return 5 - position;
        return 0;
    }

    private String getRandom(List<String> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private List<String> getRandomFoil(List<String> list, int count) {
        List<String> result = new LinkedList<>();
        for (String element : list)
            result.add(element + "*");
        Collections.shuffle(result, ThreadLocalRandom.current());
        return result.subList(0, count);
    }

}
