package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft2.SoloDraftDefinitions;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.util.ArrayList;
import java.util.List;

public class ConstructedLeagueData implements LeagueData {
    private LeaguePrizes _leaguePrizes;
    private List<LeagueSerieData> _series = new ArrayList<LeagueSerieData>();
    private CollectionType _prizeCollectionType = CollectionType.MY_CARDS;
    private CollectionType _collectionType;

    // Example params - 20120312,fotr_block,0.7,default,All cards,7,10,3,fotr1_block,fotr_block,fotr2_block,fotr_block,fotr_block,fotr_block
    // Which means - start date,league prize pool,prizes multiplier,collection type,collection name,serie length,serie match count,series count,
    // serie1 format, serie1 prize pool,
    // serie2 format, serie2 prize pool,
    // serie3 format, serie3 prize pool,
    public ConstructedLeagueData(CardSets cardSets, SoloDraftDefinitions soloDraftDefinitions, String parameters) {
        _leaguePrizes = new FixedLeaguePrizes(cardSets);
        
        String[] params = parameters.split(",");
        final int start = Integer.parseInt(params[0]);
        _collectionType = new CollectionType(params[3], params[4]);
        int days = Integer.parseInt(params[5]);
        int matchCount = Integer.parseInt(params[6]);
        int series = Integer.parseInt(params[7]);
        for (int i = 0; i < series; i++) {
            String format = params[8 + i * 2];

            DefaultLeagueSerieData data = new DefaultLeagueSerieData(_leaguePrizes, false, "Week " + (i + 1),
                    DateUtils.offsetDate(start, i * days), DateUtils.offsetDate(start, ((i + 1) * days) - 1),
                    matchCount, format, _collectionType);
            _series.add(data);
        }
    }

    @Override
    public boolean isSoloDraftLeague() {
        return false;
    }

    @Override
    public List<LeagueSerieData> getSeries() {
        return _series;
    }

    @Override
    public CardCollection joinLeague(CollectionsManager collecionsManager, Player player, int currentTime) {
        return null;
    }

    @Override
    public int process(CollectionsManager collectionsManager, List<PlayerStanding> leagueStandings, int oldStatus, int currentTime) {
        int status = oldStatus;
        if (status == 0) {
            int maxGamesCount = 0;
            for (LeagueSerieData sery : _series) {
                maxGamesCount+=sery.getMaxMatches();
            }

            LeagueSerieData lastSerie = _series.get(_series.size() - 1);
            if (currentTime > DateUtils.offsetDate(lastSerie.getEnd(), 1)) {
                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesCount, _collectionType);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league prizes", leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll().values());
                    final CardCollection leagueTrophies = _leaguePrizes.getTrophiesForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesCount, _collectionType);
                    if (leagueTrophies != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league trophies", leagueStanding.getPlayerName(), CollectionType.TROPHY, leagueTrophies.getAll().values());
                }
                status++;
            }
        }

        return status;
    }
}
