package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;

import java.util.Date;
import java.util.List;

public interface TournamentDAO {
    public void addTournament(String tournamentId, String draftType, String tournamentName, String format,
                              CollectionType collectionType, Tournament.Stage stage, String pairingMechanism, String prizeScheme, Date start);

    public List<TournamentInfo> getUnfinishedTournaments();

    public List<TournamentInfo> getFinishedTournamentsSince(long time);

    public TournamentInfo getTournamentById(String tournamentId);

    public void updateTournamentStage(String tournamentId, Tournament.Stage stage);

    public void updateTournamentRound(String tournamentId, int round);

    public List<TournamentQueueInfo> getUnstartedScheduledTournamentQueues();

    public void updateScheduledTournamentStarted(String scheduledTournamentId);
}
