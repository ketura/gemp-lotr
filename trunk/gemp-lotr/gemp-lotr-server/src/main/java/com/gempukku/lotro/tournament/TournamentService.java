package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

import java.lang.reflect.Constructor;
import java.util.*;

public class TournamentService {
    private TournamentDAO _tournamentDao;
    private TournamentPlayerDAO _tournamentPlayerDao;
    private TournamentMatchDAO _tournamentMatchDao;

    private Map<String, Tournament> _tournamentById = new HashMap<String, Tournament>();

    public TournamentService(TournamentDAO tournamentDao, TournamentPlayerDAO tournamentPlayerDao, TournamentMatchDAO tournamentMatchDao) {
        _tournamentDao = tournamentDao;
        _tournamentPlayerDao = tournamentPlayerDao;
        _tournamentMatchDao = tournamentMatchDao;
    }

    public void clearCache() {
        _tournamentById.clear();
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

    public Tournament addTournament(String tournamentId, String tournamentClass, String parameters, Date start) {
        _tournamentDao.addTournament(tournamentId, tournamentClass, parameters, start);
        return createTournamentAndStoreInCache(tournamentId, new TournamentInfo(tournamentId, tournamentClass, parameters, start));
    }

    public void markTournamentFinished(String tournamentId) {
        _tournamentDao.markTournamentFinished(tournamentId);
    }

    public List<Tournament> getLiveTournaments() {
        List<Tournament> result = new ArrayList<Tournament>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getUnfinishedTournaments()) {
            Tournament tournament = _tournamentById.get(tournamentInfo.getTournamentId());
            if (tournament == null)
                tournament = createTournamentAndStoreInCache(tournament.getTournamentId(), tournamentInfo);
            result.add(tournament);
        }
        return result;
    }

    public Tournament getTournamentById(String tournamentId) {
        Tournament tournament = _tournamentById.get(tournamentId);
        if (tournament == null) {
            TournamentInfo tournamentInfo = _tournamentDao.getTournamentById(tournamentId);
            if (tournamentInfo == null)
                return null;

            tournament = createTournamentAndStoreInCache(tournamentId, tournamentInfo);
        }
        return tournament;
    }

    private Tournament createTournamentAndStoreInCache(String tournamentId, TournamentInfo tournamentInfo) {
        Tournament tournament;
        String clazz = tournamentInfo.getTournamentClass();
        try {
            Class<?> aClass = Class.forName(clazz);
            Constructor<?> constructor = aClass.getConstructor(TournamentService.class, String.class, String.class);
            tournament = (Tournament) constructor.newInstance(this, tournamentInfo.getTournamentId(), tournamentInfo.getParameters());

        } catch (Exception exp) {
            throw new RuntimeException("Unable to create Tournament", exp);
        }
        _tournamentById.put(tournamentId, tournament);
        return tournament;
    }
}
