package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.league.LeagueSerieData;

public class GameSettings {
    private CollectionType collectionType;
    private LotroFormat lotroFormat;
    private League league;
    private LeagueSerieData leagueSerie;
    private boolean competitive;
    private boolean privateGame;
    private String timerName;
    private int maxSecondsPerPlayer;
    private int maxSecondsPerDecision;

    public GameSettings(CollectionType collectionType, LotroFormat lotroFormat, League league, LeagueSerieData leagueSerie,
                        boolean competitive, boolean privateGame, String timerName, int maxSecondsPerPlayer, int maxSecondsPerDecision) {
        this.collectionType = collectionType;
        this.lotroFormat = lotroFormat;
        this.league = league;
        this.leagueSerie = leagueSerie;
        this.competitive = competitive;
        this.privateGame = privateGame;
        this.timerName = timerName;
        this.maxSecondsPerPlayer = maxSecondsPerPlayer;
        this.maxSecondsPerDecision = maxSecondsPerDecision;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public LotroFormat getLotroFormat() {
        return lotroFormat;
    }

    public League getLeague() {
        return league;
    }

    public LeagueSerieData getLeagueSerie() {
        return leagueSerie;
    }

    public boolean isCompetitive() {
        return competitive;
    }

    public boolean isPrivateGame() {
        return privateGame;
    }

    public int getMaxSecondsPerPlayer() {
        return maxSecondsPerPlayer;
    }

    public int getMaxSecondsPerDecision() {
        return maxSecondsPerDecision;
    }

    public String getTimerName() {
        return timerName;
    }
}
