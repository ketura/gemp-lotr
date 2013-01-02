package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft.DraftCardChoice;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.List;

public interface Tournament {
    public enum Stage {
        DRAFT("Drafting"), DECK_BUILDING("Deck building"), PLAYING_GAMES("Playing games"), FINISHED("Finished");

        private String _humanReadable;

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

    public void advanceTournament(TournamentCallback tournamentCallback, CollectionsManager collectionsManager);

    public void reportGameFinished(String winner, String loser);

    public void playerChosenCard(String playerName, String cardId);
    public void playerSummittedDeck(String player, LotroDeck deck) throws DeckInvalidException;
    public boolean dropPlayer(String player);

    public DraftCardChoice getCardChoice(String playerName);
    public List<PlayerStanding> getCurrentStandings();

    public boolean isPlayerInCompetition(String player);
}
