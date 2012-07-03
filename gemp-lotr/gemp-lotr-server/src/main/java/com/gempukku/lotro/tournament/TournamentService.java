package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

import java.lang.reflect.Constructor;
import java.util.*;

public class TournamentService {
    private TournamentDAO _tournamentDao;
    private TournamentPlayerDAO _tournamentPlayerDao;
    private TournamentMatchDAO _tournamentMatchDao;

    public TournamentService(TournamentDAO tournamentDao, TournamentPlayerDAO tournamentPlayerDao, TournamentMatchDAO tournamentMatchDao) {
        _tournamentDao = tournamentDao;
        _tournamentPlayerDao = tournamentPlayerDao;
        _tournamentMatchDao = tournamentMatchDao;
    }

    public void addPlayer(String tournamentId, String playerName, LotroDeck deck) {
        _tournamentPlayerDao.addPlayer(tournamentId, playerName, deck);
    }

    public void dropPlayer(String tournamentId, String playerName) {
        _tournamentPlayerDao.dropPlayer(tournamentId, playerName);
    }

    public Map<String, LotroDeck> getPlayers(String tournamentId) {
        return _tournamentPlayerDao.getPlayers(tournamentId);
    }

    public Set<String> getDroppedPlayers(String tournamentId) {
        return _tournamentPlayerDao.getDroppedPlayers(tournamentId);
    }

    public LotroDeck getPlayerDeck(String tournamentId, String player) {
        return _tournamentPlayerDao.getPlayerDeck(tournamentId, player);
    }

    public void addMatch(String tournamentId, int round, String playerOne, String playerTwo) {
        _tournamentMatchDao.addMatch(tournamentId, round, playerOne, playerTwo);
    }

    public void setMatchResult(String tournamentId, int round, String winner) {
        _tournamentMatchDao.setMatchResult(tournamentId, round, winner);
    }

    public List<TournamentMatch> getMatches(String tournamentId, int round) {
        return _tournamentMatchDao.getMatches(tournamentId, round);
    }

    public void addTournament(int cost, String tournamentId, String tournamentClass, String parameters, Date start) {
        _tournamentDao.addTournament(cost, tournamentId, tournamentClass, parameters, start);
    }

    public void markTournamentFinished(String tournamentId) {
        _tournamentDao.markTournamentFinished(tournamentId);
    }

    public List<Tournament> getLiveTournaments() {
        List<Tournament> result = new ArrayList<Tournament>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getUnfinishedTournaments()) {
            String clazz = tournamentInfo.getTournamentClass();
            try {
                Class<?> aClass = Class.forName(clazz);
                Constructor<?> constructor = aClass.getConstructor(TournamentService.class, String.class, String.class);
                Tournament tournament = (Tournament) constructor.newInstance(this, tournamentInfo.getTournamentId(), tournamentInfo.getParameters());

                result.add(tournament);
            } catch (Exception exp) {
                throw new RuntimeException("Unable to create Tournament", exp);
            }
        }
        return result;
    }

    public Tournament getTournamentById(String tournamentId) {
        TournamentInfo tournamentInfo = _tournamentDao.getTournamentById(tournamentId);
        if (tournamentInfo == null)
            return null;
        String clazz = tournamentInfo.getTournamentClass();
        try {
            Class<?> aClass = Class.forName(clazz);
            Constructor<?> constructor = aClass.getConstructor(TournamentService.class, String.class, String.class);
            Tournament tournament = (Tournament) constructor.newInstance(this, tournamentInfo.getTournamentId(), tournamentInfo.getParameters());

            return tournament;
        } catch (Exception exp) {
            throw new RuntimeException("Unable to create Tournament", exp);
        }
    }
}
