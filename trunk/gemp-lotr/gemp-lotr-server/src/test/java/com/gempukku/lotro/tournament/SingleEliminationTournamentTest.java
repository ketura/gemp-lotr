package com.gempukku.lotro.tournament;

import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SingleEliminationTournamentTest {
    @Test
    public void simpleTest() {
        TournamentService service = Mockito.mock(TournamentService.class);
        SingleEliminationTournament tournament = new SingleEliminationTournament(service, "tid", "fotr_block,default,All cards,Tournament name");

        assertEquals("fotr_block", tournament.getFormat());
        assertEquals("default", tournament.getCollectionType().getCode());
        assertEquals("All cards", tournament.getCollectionType().getFullName());
        assertEquals("Tournament name", tournament.getTournamentName());
    }

    @Test
    public void testPairing() {
        Map<String, LotroDeck> players = new HashMap<String, LotroDeck>();
        players.put("p1", new LotroDeck("d1"));
        players.put("p2", new LotroDeck("d2"));

        TournamentService service = Mockito.mock(TournamentService.class);
        Mockito.when(service.getPlayers("tid")).thenReturn(players);
        SingleEliminationTournament tournament = new SingleEliminationTournament(service, "tid", "fotr_block,default,All cards,Tournament name");

        TournamentCallback tournamentCallback = Mockito.mock(TournamentCallback.class);
        tournament.advanceTournament(tournamentCallback);

        Mockito.verify(tournamentCallback, new Times(1)).createGame(Mockito.<LotroGameParticipant>any(), Mockito.<LotroGameParticipant>any());

        assertTrue(tournament.isPlayerInCompetition("p1"));
        assertTrue(tournament.isPlayerInCompetition("p2"));
        assertFalse(tournament.isPlayerInCompetition("p3"));

        tournament.dropPlayer("p2");

        assertFalse(tournament.isPlayerInCompetition("p2"));

        tournament.reportGameFinished(tournamentCallback, "p1", "p2");

        tournament.advanceTournament(tournamentCallback);

        assertTrue(tournament.isFinished());
        assertFalse(tournament.isPlayerInCompetition("p1"));
        assertFalse(tournament.isPlayerInCompetition("p2"));
    }

    @Test
    public void testLongerTournament() {
        Map<String, LotroDeck> players = new HashMap<String, LotroDeck>();
        players.put("p1", new LotroDeck("d1"));
        players.put("p2", new LotroDeck("d2"));
        players.put("p3", new LotroDeck("d3"));
        players.put("p4", new LotroDeck("d4"));

        TournamentService service = Mockito.mock(TournamentService.class);
        Mockito.when(service.getPlayers("tid")).thenReturn(players);
        SingleEliminationTournament tournament = new SingleEliminationTournament(service, "tid", "fotr_block,default,All cards,Tournament name");
        tournament.setWaitForPairingsTime(0);
        assertEquals(0, tournament.getCurrentRound());

        TournamentCallback tournamentCallback = Mockito.mock(TournamentCallback.class);
        tournament.advanceTournament(tournamentCallback);
        assertEquals(1, tournament.getCurrentRound());

        ArgumentCaptor<LotroGameParticipant> playerOne = ArgumentCaptor.forClass(LotroGameParticipant.class);
        ArgumentCaptor<LotroGameParticipant> playerTwo = ArgumentCaptor.forClass(LotroGameParticipant.class);
        Mockito.verify(tournamentCallback, new Times(2)).createGame(playerOne.capture(), playerTwo.capture());

        List<LotroGameParticipant> playerOnes = playerOne.getAllValues();
        List<LotroGameParticipant> playerTwos = playerTwo.getAllValues();

        List<String> oneNames = extractNames(playerOnes);
        List<String> twoNames = extractNames(playerTwos);

        assertTrue(oneNames.contains("p1") || twoNames.contains("p1"));
        assertTrue(oneNames.contains("p2") || twoNames.contains("p2"));
        assertTrue(oneNames.contains("p3") || twoNames.contains("p3"));
        assertTrue(oneNames.contains("p4") || twoNames.contains("p4"));

        assertTrue(tournament.isPlayerInCompetition("p1"));
        assertTrue(tournament.isPlayerInCompetition("p2"));
        assertTrue(tournament.isPlayerInCompetition("p3"));
        assertTrue(tournament.isPlayerInCompetition("p4"));

        String playerAgainstP1 = getPlayerPlayingAgainst(oneNames, twoNames, "p1");
        String secondWinner;

        tournament.reportGameFinished(tournamentCallback, "p1", playerAgainstP1);
        if (playerAgainstP1.equals("p2")) {
            secondWinner = "p3";
            tournament.reportGameFinished(tournamentCallback, "p3", "p4");
        } else if (playerAgainstP1.equals("p3")) {
            secondWinner = "p2";
            tournament.reportGameFinished(tournamentCallback, "p2", "p4");
        } else {
            secondWinner = "p2";
            tournament.reportGameFinished(tournamentCallback, "p2", "p3");
        }

        assertEquals(1, tournament.getCurrentRound());
        tournamentCallback = Mockito.mock(TournamentCallback.class);
        tournament.advanceTournament(tournamentCallback);
        assertEquals(2, tournament.getCurrentRound());

        ArgumentCaptor<LotroGameParticipant> playerOne2 = ArgumentCaptor.forClass(LotroGameParticipant.class);
        ArgumentCaptor<LotroGameParticipant> playerTwo2 = ArgumentCaptor.forClass(LotroGameParticipant.class);
        Mockito.verify(tournamentCallback, new Times(1)).createGame(playerOne2.capture(), playerTwo2.capture());

        assertTrue(oneNames.contains("p1") || twoNames.contains("p1"));
        assertTrue(oneNames.contains(secondWinner) || twoNames.contains(secondWinner));

        tournament.reportGameFinished(tournamentCallback, "p1", secondWinner);

        assertTrue(tournament.isFinished());
        assertFalse(tournament.isPlayerInCompetition("p1"));
        assertFalse(tournament.isPlayerInCompetition("p2"));
        assertFalse(tournament.isPlayerInCompetition("p3"));
        assertFalse(tournament.isPlayerInCompetition("p4"));
    }

    private String getPlayerPlayingAgainst(List<String> oneNames, List<String> twoNames, String player) {
        String playerAgainstP1;
        if (oneNames.contains(player))
            playerAgainstP1 = twoNames.get(oneNames.indexOf(player));
        else
            playerAgainstP1 = oneNames.get(twoNames.indexOf(player));
        return playerAgainstP1;
    }

    private List<String> extractNames(List<LotroGameParticipant> playerOnes) {
        List<String> result = new ArrayList<String>();
        for (LotroGameParticipant playerOne : playerOnes) {
            result.add(playerOne.getPlayerId());
        }
        return result;
    }
}
