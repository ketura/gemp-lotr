package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.Assert.*;

public class SingleEliminationRecurringQueueTest {
    @Test
    public void joiningQueue() {
        TournamentService tournamentService = Mockito.mock(TournamentService.class);

        ImmediateRecurringQueue queue = new ImmediateRecurringQueue(10, "format", CollectionType.MY_CARDS,
                "id-", "name-", 2, false, tournamentService, new NoPrizes(), new SingleEliminationPairing("singleElimination"));

        Player player = new Player(1, "p1", "pass", "u", null, null, null, null);

        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);
        Mockito.when(collectionsManager.removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10)))
                .thenReturn(true);

        queue.joinPlayer(collectionsManager, player, null);

        Mockito.verify(collectionsManager).removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10));
        Mockito.verifyNoMoreInteractions(collectionsManager, tournamentService);

        assertEquals(1, queue.getPlayerCount());
        assertTrue(queue.isPlayerSignedUp("p1"));
    }

    @Test
    public void leavingQueue() {
        TournamentService tournamentService = Mockito.mock(TournamentService.class);

        ImmediateRecurringQueue queue = new ImmediateRecurringQueue(10, "format", CollectionType.MY_CARDS,
                "id-", "name-", 2, false, tournamentService, new NoPrizes(), new SingleEliminationPairing("singleElimination"));

        Player player = new Player(1, "p1", "pass", "u", null, null, null, null);

        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);
        Mockito.when(collectionsManager.removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10)))
                .thenReturn(true);

        queue.joinPlayer(collectionsManager, player, null);

        Mockito.verify(collectionsManager).removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10));

        queue.leavePlayer(collectionsManager, player);
        Mockito.verify(collectionsManager).addCurrencyToPlayerCollection(Mockito.anyBoolean(), Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10));
        Mockito.verifyNoMoreInteractions(collectionsManager, tournamentService);

        assertEquals(0, queue.getPlayerCount());
        assertFalse(queue.isPlayerSignedUp("p1"));
    }
    
    @Test
    public void cancellingQueue() {
        TournamentService tournamentService = Mockito.mock(TournamentService.class);

        ImmediateRecurringQueue queue = new ImmediateRecurringQueue(10, "format", CollectionType.MY_CARDS,
                "id-", "name-", 2, false, tournamentService, new NoPrizes(), new SingleEliminationPairing("singleElimination"));

        Player player = new Player(1, "p1", "pass", "u", null, null, null, null);

        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);
        Mockito.when(collectionsManager.removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10)))
                .thenReturn(true);

        queue.joinPlayer(collectionsManager, player, null);

        Mockito.verify(collectionsManager).removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.eq(player), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10));

        queue.leaveAllPlayers(collectionsManager);
        Mockito.verify(collectionsManager).addCurrencyToPlayerCollection(Mockito.anyBoolean(), Mockito.anyString(), Mockito.eq("p1"), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10));
        Mockito.verifyNoMoreInteractions(collectionsManager, tournamentService);

        assertEquals(0, queue.getPlayerCount());
        assertFalse(queue.isPlayerSignedUp("p1"));
    }

    @Test
    public void fillingQueue() {
        Tournament tournament = Mockito.mock(Tournament.class);
        TournamentService tournamentService = Mockito.mock(TournamentService.class);
        Mockito.when(tournamentService.addTournament(Mockito.anyString(), Mockito.<String>eq(null), Mockito.anyString(), Mockito.eq("format"),
                Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(Tournament.Stage.PLAYING_GAMES), Mockito.eq("singleElimination"), Mockito.nullable(String.class), Mockito.<Date>any()))
                .thenReturn(tournament);

        ImmediateRecurringQueue queue = new ImmediateRecurringQueue(10, "format", CollectionType.MY_CARDS,
                "id-", "name-", 2, false, tournamentService, new NoPrizes(), new SingleEliminationPairing("singleElimination"));


        Player player1 = new Player(1, "p1", "pass", "u", null, null, null, null);
        Player player2 = new Player(2, "p2", "pass", "u", null, null, null, null);

        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);
        Mockito.when(collectionsManager.removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.<Player>any(), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10)))
                .thenReturn(true);

        queue.joinPlayer(collectionsManager, player1, null);

        TournamentQueueCallback queueCallback = Mockito.mock(TournamentQueueCallback.class);
        assertFalse(queue.process(queueCallback, collectionsManager));

        Mockito.verifyNoMoreInteractions(queueCallback);

        queue.joinPlayer(collectionsManager, player2, null);

        assertEquals(2, queue.getPlayerCount());

        assertFalse(queue.process(queueCallback, collectionsManager));

        assertEquals(0, queue.getPlayerCount());
        assertFalse(queue.isPlayerSignedUp("p1"));
        assertFalse(queue.isPlayerSignedUp("p2"));

        Mockito.verify(tournamentService).addTournament(Mockito.anyString(), Mockito.<String>eq(null), Mockito.anyString(), Mockito.eq("format"),
                Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(Tournament.Stage.PLAYING_GAMES), Mockito.eq("singleElimination"), Mockito.nullable(String.class), Mockito.<Date>any());
        
        Mockito.verify(tournamentService).addPlayer(Mockito.anyString(), Mockito.eq("p1"), Mockito.<LotroDeck>eq(null));
        Mockito.verify(tournamentService).addPlayer(Mockito.anyString(), Mockito.eq("p2"), Mockito.<LotroDeck>eq(null));

        Mockito.verify(queueCallback).createTournament(tournament);
        Mockito.verifyNoMoreInteractions(tournamentService, queueCallback);
    }

    @Test
    public void overflowingQueue() {
        Tournament tournament = Mockito.mock(Tournament.class);

        TournamentService tournamentService = Mockito.mock(TournamentService.class);
        Mockito.when(tournamentService.addTournament(Mockito.anyString(), Mockito.<String>eq(null), Mockito.anyString(), Mockito.eq("format"),
                Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(Tournament.Stage.PLAYING_GAMES), Mockito.eq("singleElimination"), Mockito.nullable(String.class), Mockito.<Date>any()))
                .thenReturn(tournament);

        ImmediateRecurringQueue queue = new ImmediateRecurringQueue(10, "format", CollectionType.MY_CARDS,
                "id-", "name-", 2, false, tournamentService, new NoPrizes(), new SingleEliminationPairing("singleElimination"));

        Player player1 = new Player(1, "p1", "pass", "u", null, null, null, null);
        Player player2 = new Player(2, "p2", "pass", "u", null, null, null, null);
        Player player3 = new Player(3, "p3", "pass", "u", null, null, null, null);

        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);
        Mockito.when(collectionsManager.removeCurrencyFromPlayerCollection(Mockito.anyString(), Mockito.<Player>any(), Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(10)))
                .thenReturn(true);

        queue.joinPlayer(collectionsManager, player1, null);

        TournamentQueueCallback queueCallback = Mockito.mock(TournamentQueueCallback.class);
        assertFalse(queue.process(queueCallback, collectionsManager));

        Mockito.verifyNoMoreInteractions(queueCallback);

        queue.joinPlayer(collectionsManager, player2, null);
        queue.joinPlayer(collectionsManager, player3, null);

        assertEquals(3, queue.getPlayerCount());

        assertFalse(queue.process(queueCallback, collectionsManager));

        assertEquals(1, queue.getPlayerCount());
        assertFalse(queue.isPlayerSignedUp("p1"));
        assertFalse(queue.isPlayerSignedUp("p2"));
        assertTrue(queue.isPlayerSignedUp("p3"));

        Mockito.verify(tournamentService).addTournament(Mockito.anyString(), Mockito.<String>eq(null), Mockito.anyString(), Mockito.eq("format"),
                Mockito.eq(CollectionType.MY_CARDS), Mockito.eq(Tournament.Stage.PLAYING_GAMES), Mockito.eq("singleElimination"), Mockito.nullable(String.class), Mockito.<Date>any());

        Mockito.verify(tournamentService).addPlayer(Mockito.anyString(), Mockito.eq("p1"), Mockito.<LotroDeck>eq(null));
        Mockito.verify(tournamentService).addPlayer(Mockito.anyString(), Mockito.eq("p2"), Mockito.<LotroDeck>eq(null));

        Mockito.verify(queueCallback).createTournament(tournament);
        Mockito.verifyNoMoreInteractions(tournamentService, queueCallback);
    }
}
