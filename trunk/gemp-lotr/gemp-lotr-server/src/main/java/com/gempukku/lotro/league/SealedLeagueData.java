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

public class SealedLeagueData implements LeagueData {
    private String _format;
    private List<LeagueSerieData> _series;
    private CollectionType _collectionType;
    private CollectionType _prizeCollectionType = CollectionType.MY_CARDS;
    private LeaguePrizes _leaguePrizes;
    private SealedLeagueProduct _leagueProduct;

    public SealedLeagueData(String parameters) {
        String[] params = parameters.split(",");
        _format = params[0];
        int start = Integer.parseInt(params[1]);
        _collectionType = new CollectionType(params[2], params[3]);

        int serieDuration = 7;
        int maxMatches = 10;

        _leaguePrizes = new NewLeaguePrizes();
        _leagueProduct = new SealedLeagueProduct();

        _series = new LinkedList<LeagueSerieData>();
        for (int i = 0; i < 4; i++) {
            _series.add(
                    new DefaultLeagueSerieData(_leaguePrizes, true, "Week " + (i + 1),
                            DateUtils.offsetDate(start, i * serieDuration), DateUtils.offsetDate(start, (i + 1) * serieDuration - 1), maxMatches,
                            _format, _collectionType));
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

                for (Map.Entry<String, CardCollection.Item> serieCollectionItem : leagueProduct.getAll().entrySet())
                    startingCollection.addItem(serieCollectionItem.getKey(), serieCollectionItem.getValue().getCount());
            }
        }
        collectionManager.addPlayerCollection(true, "Sealed league product", player, _collectionType, startingCollection);
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
                    collectionsManager.addItemsToPlayerCollection(true, "New sealed league product", playerCardCollectionEntry.getKey(), _collectionType, leagueProduct.getAll().values());
                }
                status = i + 1;
            }
        }

        if (status == _series.size()) {
            LeagueSerieData lastSerie = _series.get(_series.size() - 1);
            if (currentTime > DateUtils.offsetDate(lastSerie.getEnd(), 1)) {
                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), 1f);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league prizes", leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll().values());
                }
                status++;
            }
        }

        return status;
    }
}
