package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft.Draft;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.util.List;

public interface Tournament {
    public enum Stage {
        DRAFT("Drafting"), DECK_BUILDING("Deck building"), PLAYING_GAMES("Playing games"), FINISHED("Finished");

        private final String _humanReadable;

        Stage(String humanReadable) {
            _humanReadable = humanReadable;
        }

        public String getHumanReadable() {
            return _humanReadable;
        }
    }

    public String getTournamentId();
    public String getFormat();
    public CollectionType getCollectionType();
    public String getTournamentName();
    public String getPlayOffSystem();

    public Stage getTournamentStage();
    public int getCurrentRound();
    public int getPlayersInCompetitionCount();

    public boolean advanceTournament(TournamentCallback tournamentCallback, CollectionsManager collectionsManager);

    public void reportGameFinished(String winner, String loser);

    public void playerChosenCard(String playerName, String cardId);
    public void playerSummittedDeck(String player, LotroDeck deck);
    public LotroDeck getPlayerDeck(String player);
    public boolean dropPlayer(String player);

    public Draft getDraft();

    public List<PlayerStanding> getCurrentStandings();

    public boolean isPlayerInCompetition(String player);
}
