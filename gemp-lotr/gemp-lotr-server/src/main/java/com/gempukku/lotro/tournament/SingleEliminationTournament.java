package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SingleEliminationTournament extends AbstractTournament {
    private static String param(String parameters, int index) {
        return parameters.split(",")[index];
    }

    public SingleEliminationTournament(TournamentService tournamentService, String tournamentId, String parameters) {
        super(tournamentService, tournamentId,
                param(parameters, 0),
                new CollectionType(param(parameters, 1), param(parameters, 2)),
                param(parameters, 3));
    }

    @Override
    protected void pairPlayers(TournamentCallback tournamentCallback) {
        List<String> playersRandomized = new ArrayList<String>(_playersInContention);
        Collections.shuffle(playersRandomized);

        Iterator<String> playerIterator = playersRandomized.iterator();
        while (playerIterator.hasNext()) {
            String playerOne = playerIterator.next();
            if (playerIterator.hasNext()) {
                String playerTwo = playerIterator.next();
                pairNewGame(tournamentCallback, playerOne, playerTwo);
            } else {
                awardNewBye(tournamentCallback, playerOne);
            }
        }
    }

    @Override
    public void reportGameFinished(TournamentCallback tournamentCallback, String winner, String loser) {
        _lock.writeLock().lock();
        try {
            dropPlayer(loser);
            _playersInContention.remove(loser);
            super.reportGameFinished(tournamentCallback, winner, loser);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isFinished() {
        _lock.readLock().lock();
        try {
            return _playersInContention.size() < 2;
        } finally {
            _lock.readLock().unlock();
        }
    }
}
