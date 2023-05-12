package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.league.LeagueSerieData;

public class GameSettings {
    private final CollectionType collectionType;
    private final LotroFormat lotroFormat;
    private final League league;
    private final LeagueSerieData leagueSerie;
    private final boolean competitive;
    private final boolean privateGame;
    private final boolean hiddenGame;
    private final String timerName;
    private final int maxSecondsPerPlayer;
    private final int maxSecondsPerDecision;
    private final String userDescription;
    private final boolean isInviteOnly;

    public GameSettings(CollectionType collectionType, LotroFormat lotroFormat, League league, LeagueSerieData leagueSerie,
                        boolean competitive, boolean privateGame, boolean hiddenGame, String timerName, int maxSecondsPerPlayer, int maxSecondsPerDecision,
                        String description, boolean isInviteOnly) {
        this.collectionType = collectionType;
        this.lotroFormat = lotroFormat;
        this.league = league;
        this.leagueSerie = leagueSerie;
        this.competitive = competitive;
        this.privateGame = privateGame;
        this.hiddenGame = hiddenGame;
        this.timerName = timerName;
        this.maxSecondsPerPlayer = maxSecondsPerPlayer;
        this.maxSecondsPerDecision = maxSecondsPerDecision;
        this.userDescription = description;
        this.isInviteOnly = isInviteOnly;
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

    public boolean isHiddenGame() {
        return hiddenGame;
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

    public String getUserDescription() { return userDescription; }

    public boolean isUserInviteOnly() { return isInviteOnly; }
}
