package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NewSealedLeagueData implements LeagueData {
    private String _format;
    private List<LeagueSerieData> _series;
    private CollectionType _collectionType;
    private CollectionType _prizeCollectionType = new CollectionType("permanent", "My cards");
    private NewLeaguePrizes _leaguePrizes;
    private SealedLeagueProduct _leagueProduct;

    public NewSealedLeagueData(String parameters) {
        String[] params = parameters.split(",");
        _format = params[0];
        int start = Integer.parseInt(params[1]);
        int serieDuration = Integer.parseInt(params[2]);
        int maxMatches = Integer.parseInt(params[3]);

        _collectionType = new CollectionType(params[4], params[5]);

        _leaguePrizes = new NewLeaguePrizes();
        _leagueProduct = new SealedLeagueProduct();

        _series = new LinkedList<LeagueSerieData>();
        for (int i = 0; i < 4; i++) {
            _series.add(
                    new DefaultLeagueSerieData(_leaguePrizes, true, "Serie " + (i + 1),
                            DateUtils.offsetDate(start, i * serieDuration), DateUtils.offsetDate(start, (i + 1) * serieDuration - 1), maxMatches,
                            _format, _format, _collectionType));
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
    public int process(CollectionsManager collectionsManager, List<PlayerStanding> leagueStandings, int oldStatus, int currentTime) {
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
            if (currentTime > DateUtils.offsetDate(lastSerie.getEnd(), 1)) {
                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), 1f, _format);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll());
                }
//                for (PlayerStanding leagueStanding : leagueStandings) {
//                    collectionsManager.moveCollectionToCollection(leagueStanding.getPlayerName(), _collectionType, _prizeCollectionType);
//                }
                status++;
            }
        }

        return status;
    }
}