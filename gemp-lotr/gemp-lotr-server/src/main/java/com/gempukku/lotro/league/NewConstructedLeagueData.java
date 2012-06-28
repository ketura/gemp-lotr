package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewConstructedLeagueData implements LeagueData {
    private List<LeagueSerieData> _series = new ArrayList<LeagueSerieData>();

    private CollectionType _prizeCollectionType = new CollectionType("permanent", "My cards");
    private LeaguePrizes _leaguePrizes = new NewLeaguePrizes();
    private float _prizeMultiplier;

    public NewConstructedLeagueData(String parameters) {
        String[] params = parameters.split(",");
        int start = Integer.parseInt(params[0]);
        CollectionType collectionType;
        if (params[1].equals("default"))
            collectionType = new CollectionType("default", "All cards");
        else if (params[1].equals("permanent"))
            collectionType = new CollectionType("permanent", "My cards");
        else
            throw new IllegalArgumentException("Unkown collection type");
        _prizeMultiplier = Float.parseFloat(params[2]);
        int series = Integer.parseInt(params[3]);

        int serieStart = start;
        for (int i = 0; i < series; i++) {
            String format = params[4 + i * 3];
            int duration = Integer.parseInt(params[5 + i * 3]);
            int maxMatches = Integer.parseInt(params[6 + i * 3]);
            _series.add(new DefaultLeagueSerieData(_leaguePrizes, false, "Serie " + (i + 1),
                    serieStart, DateUtils.offsetDate(serieStart, duration - 1),
                    maxMatches, format, collectionType));

            serieStart = DateUtils.offsetDate(serieStart, duration);
        }
    }

    @Override
    public List<LeagueSerieData> getSeries() {
        return Collections.unmodifiableList(_series);
    }

    @Override
    public CardCollection joinLeague(CollectionsManager collecionsManager, Player player, int currentTime) {
        return null;
    }

    @Override
    public int process(CollectionsManager collectionsManager, List<PlayerStanding> leagueStandings, int oldStatus, int currentTime) {
        int status = oldStatus;
        if (status == 0) {
            LeagueSerieData lastSerie = _series.get(_series.size() - 1);
            if (currentTime > DateUtils.offsetDate(lastSerie.getEnd(), 1)) {
                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), _prizeMultiplier);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll().values());
                }
                status++;
            }
        }

        return status;
    }
}
