package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft2.SoloDraft;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SealedLeagueData implements LeagueData {
    private final String _format;
    private final List<LeagueSerieData> _series;
    private final CollectionType _collectionType;
    private final CollectionType _prizeCollectionType = CollectionType.MY_CARDS;
    private final LeaguePrizes _leaguePrizes;
    private final LotroFormatLibrary _formatLibrary;

    public SealedLeagueData(CardBlueprintLibrary library, LotroFormatLibrary formatLibrary, String parameters) {
        _leaguePrizes = new FixedLeaguePrizes(library);
        _formatLibrary = formatLibrary;
        
        String[] params = parameters.split(",");
        _format = params[0];
        int start = Integer.parseInt(params[1]);
        _collectionType = new CollectionType(params[2], params[3]);

        int serieDuration = 7;
        int maxMatches = 10;

        _series = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            _series.add(
                    new DefaultLeagueSerieData(_leaguePrizes, true, "Week " + (i + 1),
                            DateUtils.offsetDate(start, i * serieDuration), DateUtils.offsetDate(start, (i + 1) * serieDuration - 1), maxMatches,
                            formatLibrary.getFormatByName(_format), _collectionType));
        }
    }

    @Override
    public boolean isSoloDraftLeague() {
        return false;
    }

    @Override
    public SoloDraft getSoloDraft() {
        return null;
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
                var sealedLeague = _formatLibrary.GetSealedTemplate(_format);
                var leagueProduct = sealedLeague.GetProductForSerie(i);

                for (CardCollection.Item serieCollectionItem : leagueProduct)
                    startingCollection.addItem(serieCollectionItem.getBlueprintId(), serieCollectionItem.getCount());
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
                var sealedLeague = _formatLibrary.GetSealedTemplate(_format);
                var leagueProduct = sealedLeague.GetProductForSerie(i);

                Map<Player, CardCollection> map = collectionsManager.getPlayersCollection(_collectionType.getCode());
                for (Map.Entry<Player, CardCollection> playerCardCollectionEntry : map.entrySet()) {
                    collectionsManager.addItemsToPlayerCollection(true, "New sealed league product", playerCardCollectionEntry.getKey(), _collectionType, leagueProduct);
                }
                status = i + 1;
            }
        }

        if (status == _series.size()) {
            int maxGamesPlayed = 0;
            for (LeagueSerieData sery : _series) {
                maxGamesPlayed+=sery.getMaxMatches();
            }

            LeagueSerieData lastSerie = _series.get(_series.size() - 1);
            if (currentTime > DateUtils.offsetDate(lastSerie.getEnd(), 1)) {
                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesPlayed, _collectionType);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league prizes", leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll());
                    final CardCollection leagueTrophies = _leaguePrizes.getTrophiesForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesPlayed, _collectionType);
                    if (leagueTrophies != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league trophies", leagueStanding.getPlayerName(), CollectionType.TROPHY, leagueTrophies.getAll());
                }
                status++;
            }
        }

        return status;
    }
}
