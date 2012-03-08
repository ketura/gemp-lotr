package com.gempukku.lotro.league;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SealedLeagueData implements LeagueData {
    private String _format;
    private List<LeagueSerieData> _series;
    private CollectionType _collectionType;
    private CollectionType _prizeCollectionType = new CollectionType("permanent", "My cards");
    private LeaguePrizes _leaguePrizes;
    private SealedLeagueProduct _leagueProduct;

    public SealedLeagueData(String parameters) {
        String[] params = parameters.split(",");
        _format = params[0];
        int start = Integer.parseInt(params[1]);
        _collectionType = new CollectionType(params[2], params[3]);

        int serieDuration = 7;
        int maxMatches = 10;

        _leaguePrizes = new LeaguePrizes();
        _leagueProduct = new SealedLeagueProduct();

        _series = new LinkedList<LeagueSerieData>();
        for (int i = 0; i < 4; i++) {
            _series.add(
                    new DefaultLeagueSerieData(_leaguePrizes, true, "Week " + (i + 1),
                            getDate(start, i * serieDuration), getDate(start, (i + 1) * serieDuration - 1), maxMatches,
                            _format, _format, _collectionType));
        }
    }

    private int getDate(int start, int dayOffset) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(String.valueOf(start));
            date.setDate(date.getDate() + dayOffset);
            return Integer.parseInt(format.format(date));
        } catch (ParseException exp) {
            throw new RuntimeException("Can't parse date", exp);
        }
    }

    @Override
    public List<LeagueSerieData> getSeries() {
        return Collections.unmodifiableList(_series);
    }

    @Override
    public CardCollection joinLeague(CollectionsManager collectionManager, Player player, int currentTime) {
        MutableCardCollection startingCollection = new DefaultCardCollection();
        for (int i = 0; i < _series.size(); i++) {
            LeagueSerieData serie = _series.get(i);
            if (currentTime >= serie.getStart()) {
                CardCollection leagueProduct = _leagueProduct.getCollectionForSerie(_format, i);

                for (Map.Entry<String, Integer> serieCollectionItem : leagueProduct.getAll().entrySet())
                    startingCollection.addItem(serieCollectionItem.getKey(), serieCollectionItem.getValue());
            }
        }
        collectionManager.addPlayerCollection(player, _collectionType, startingCollection);
        return startingCollection;
    }

    @Override
    public int process(CollectionsManager collectionsManager, List<LeagueStanding> leagueStandings, int oldStatus, int currentTime) {
        int status = oldStatus;

        for (int i = status; i < _series.size(); i++) {
            LeagueSerieData serie = _series.get(i);
            if (currentTime >= serie.getStart()) {
                CardCollection leagueProduct = _leagueProduct.getCollectionForSerie(_format, i);
                Map<Player, CardCollection> map = collectionsManager.getPlayersCollection(_collectionType.getCode());
                for (Map.Entry<Player, CardCollection> playerCardCollectionEntry : map.entrySet()) {
                    collectionsManager.addItemsToPlayerCollection(playerCardCollectionEntry.getKey(), _collectionType, leagueProduct.getAll());
                }
                status = i + 1;
            }
        }

        if (status == _series.size()) {
            LeagueSerieData lastSerie = _series.get(_series.size() - 1);
            if (currentTime > getDate(lastSerie.getEnd(), 1)) {
                for (LeagueStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), 1f, _format);
                    collectionsManager.addItemsToPlayerCollection(leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll());
                }
                for (LeagueStanding leagueStanding : leagueStandings) {
                    collectionsManager.moveCollectionToCollection(leagueStanding.getPlayerName(), _collectionType, _prizeCollectionType);
                }
                status++;
            }
        }

        return status;
    }
}
