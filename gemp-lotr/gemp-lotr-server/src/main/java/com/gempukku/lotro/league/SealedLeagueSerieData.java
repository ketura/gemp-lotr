package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;

public class SealedLeagueSerieData implements LeagueSerieData {
    private SealedLeaguePrizes _leaguePrizes;
    private String _name;
    private int _start;
    private int _end;
    private int _maxMatches;
    private String _format;
    private CollectionType _collectionType;

    public SealedLeagueSerieData(SealedLeaguePrizes leaguePrizes, String name, int start, int end, int maxMatches, String format, CollectionType collectionType) {
        _leaguePrizes = leaguePrizes;
        _name = name;
        _start = start;
        _end = end;
        _maxMatches = maxMatches;
        _format = format;
        _collectionType = collectionType;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getStart() {
        return _start;
    }

    @Override
    public int getEnd() {
        return _end;
    }

    @Override
    public int getMaxMatches() {
        return _maxMatches;
    }

    @Override
    public boolean isLimited() {
        return true;
    }

    @Override
    public String getFormat() {
        return _format;
    }

    @Override
    public CollectionType getCollectionType() {
        return _collectionType;
    }

    @Override
    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        return _leaguePrizes.getPrizeForLeagueMatchWinner(winCountThisSerie, totalGamesPlayedThisSerie, _format);
    }

    @Override
    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        return _leaguePrizes.getPrizeForLeagueMatchLoser(winCountThisSerie, totalGamesPlayedThisSerie, _format);
    }
}
