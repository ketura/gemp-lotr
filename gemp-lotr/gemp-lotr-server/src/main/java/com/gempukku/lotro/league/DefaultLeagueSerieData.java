package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;

public class DefaultLeagueSerieData implements LeagueSerieData {
    private final LeaguePrizes _leaguePrizes;
    private final boolean _limited;
    private final String _name;
    private final int _start;
    private final int _end;
    private final int _maxMatches;
    private final String _format;
    private final CollectionType _collectionType;

    public DefaultLeagueSerieData(LeaguePrizes leaguePrizes, boolean limited, String name, int start, int end, int maxMatches, String format, CollectionType collectionType) {
        _leaguePrizes = leaguePrizes;
        _limited = limited;
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
        return _limited;
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
        return _leaguePrizes.getPrizeForLeagueMatchWinner(winCountThisSerie, totalGamesPlayedThisSerie);
    }

    @Override
    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie) {
        return _leaguePrizes.getPrizeForLeagueMatchLoser(winCountThisSerie, totalGamesPlayedThisSerie);
    }
}
