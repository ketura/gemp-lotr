package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft2.SoloDraft;
import com.gempukku.lotro.draft2.SoloDraftDefinitions;
import com.gempukku.lotro.game.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoloDraftLeagueData implements LeagueData {
    public static final int HIGH_ENOUGH_PRIME_NUMBER = 8963;
    private SoloDraft _draft;
    private CollectionType _collectionType;
    private long _code;
    private CollectionType _prizeCollectionType = CollectionType.MY_CARDS;
    private LeaguePrizes _leaguePrizes;
    private LeagueSerieData _serie;

    public SoloDraftLeagueData(CardSets cardSets, SoloDraftDefinitions soloDraftDefinitions, String parameters) {
        _leaguePrizes = new FixedLeaguePrizes(cardSets);

        String[] params = parameters.split(",");
        _draft = soloDraftDefinitions.getSoloDraft(params[0]);
        int start = Integer.parseInt(params[1]);
        int serieDuration = Integer.parseInt(params[2]);
        int maxMatches = Integer.parseInt(params[3]);
        _code = Long.parseLong(params[4]);

        _collectionType = new CollectionType(params[4], params[5]);

        _serie = new DefaultLeagueSerieData(_leaguePrizes, true, "Serie 1",
                DateUtils.offsetDate(start, 0), DateUtils.offsetDate(start, serieDuration - 1), maxMatches,
                _draft.getFormat(), _collectionType);
    }

    public CollectionType getCollectionType() {
        return _collectionType;
    }

    @Override
    public SoloDraft getSoloDraft() {
        return _draft;
    }

    @Override
    public boolean isSoloDraftLeague() {
        return true;
    }

    @Override
    public List<LeagueSerieData> getSeries() {
        return Collections.singletonList(_serie);
    }

    private long getSeed(Player player) {
        return _collectionType.getCode().hashCode() + player.getId() * HIGH_ENOUGH_PRIME_NUMBER;
    }

    @Override
    public CardCollection joinLeague(CollectionsManager collectionsManager, Player player, int currentTime) {
        MutableCardCollection startingCollection = new DefaultCardCollection();
        long seed = getSeed(player);

        CardCollection leagueProduct = _draft.initializeNewCollection(seed);

        for (CardCollection.Item serieCollectionItem : leagueProduct.getAll())
            startingCollection.addItem(serieCollectionItem.getBlueprintId(), serieCollectionItem.getCount());

        startingCollection.setExtraInformation(createExtraInformation(seed));
        collectionsManager.addPlayerCollection(false, "Sealed league product", player, _collectionType, startingCollection);
        return startingCollection;
    }

    private Map<String, Object> createExtraInformation(long seed) {
        Map<String, Object> extraInformation = new HashMap<String, Object>();
        extraInformation.put("finished", false);
        extraInformation.put("stage", 0);
        extraInformation.put("seed", seed);
        extraInformation.put("draftPool", _draft.initializeDraftPool(seed, _code));
        return extraInformation;
    }

    @Override
    public int process(CollectionsManager collectionsManager, List<PlayerStanding> leagueStandings, int oldStatus, int currentTime) {
        int status = oldStatus;

        if (status == 0) {
            if (currentTime > DateUtils.offsetDate(_serie.getEnd(), 1)) {
                int maxGamesTotal = _serie.getMaxMatches();

                for (PlayerStanding leagueStanding : leagueStandings) {
                    CardCollection leaguePrize = _leaguePrizes.getPrizeForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesTotal, _collectionType);
                    if (leaguePrize != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league prizes", leagueStanding.getPlayerName(), _prizeCollectionType, leaguePrize.getAll());
                    final CardCollection leagueTrophies = _leaguePrizes.getTrophiesForLeague(leagueStanding.getStanding(), leagueStandings.size(), leagueStanding.getGamesPlayed(), maxGamesTotal, _collectionType);
                    if (leagueTrophies != null)
                        collectionsManager.addItemsToPlayerCollection(true, "End of league trophies", leagueStanding.getPlayerName(), CollectionType.TROPHY, leagueTrophies.getAll());
                }
                status++;
            }
        }

        return status;
    }
}
