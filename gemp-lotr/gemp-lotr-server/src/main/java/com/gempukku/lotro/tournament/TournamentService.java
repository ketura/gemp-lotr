package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft.DraftPack;
import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.packs.DraftPackStorage;
import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;

public class TournamentService {
    private PacksStorage _packsStorage;
    private DraftPackStorage _draftPackStorage;
    private PairingMechanismRegistry _pairingMechanismRegistry;
    private TournamentPrizeSchemeRegistry _tournamentPrizeSchemeRegistry;
    private TournamentDAO _tournamentDao;
    private TournamentPlayerDAO _tournamentPlayerDao;
    private TournamentMatchDAO _tournamentMatchDao;
    private CardSets _cardSets;

    private CollectionsManager _collectionsManager;

    private Map<String, Tournament> _tournamentById = new HashMap<String, Tournament>();

    public TournamentService(CollectionsManager collectionsManager, PacksStorage packsStorage, DraftPackStorage draftPackStorage,
                             PairingMechanismRegistry pairingMechanismRegistry, TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry,
                             TournamentDAO tournamentDao, TournamentPlayerDAO tournamentPlayerDao, TournamentMatchDAO tournamentMatchDao,
                             CardSets cardSets) {
        _collectionsManager = collectionsManager;
        _packsStorage = packsStorage;
        _draftPackStorage = draftPackStorage;
        _pairingMechanismRegistry = pairingMechanismRegistry;
        _tournamentPrizeSchemeRegistry = tournamentPrizeSchemeRegistry;
        _tournamentDao = tournamentDao;
        _tournamentPlayerDao = tournamentPlayerDao;
        _tournamentMatchDao = tournamentMatchDao;
        _cardSets = cardSets;
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

    public Set<String> getPlayers(String tournamentId) {
        return _tournamentPlayerDao.getPlayers(tournamentId);
    }

    public Map<String, LotroDeck> getPlayerDecks(String tournamentId) {
        return _tournamentPlayerDao.getPlayerDecks(tournamentId);
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

    public void setPlayerDeck(String tournamentId, String player, LotroDeck deck) {
        _tournamentPlayerDao.updatePlayerDeck(tournamentId, player, deck);
    }

    public List<TournamentMatch> getMatches(String tournamentId) {
        return _tournamentMatchDao.getMatches(tournamentId);
    }

    public Tournament addTournament(String tournamentId, String draftType, String tournamentName, String format, CollectionType collectionType, Tournament.Stage stage, String pairingMechanism, String prizeScheme, Date start) {
        _tournamentDao.addTournament(tournamentId, draftType, tournamentName, format, collectionType, stage, pairingMechanism, prizeScheme, start);
        return createTournamentAndStoreInCache(tournamentId, new TournamentInfo(tournamentId, draftType, tournamentName, format, collectionType, stage, pairingMechanism, prizeScheme, 0));
    }

    public void updateTournamentStage(String tournamentId, Tournament.Stage stage) {
        _tournamentDao.updateTournamentStage(tournamentId, stage);
    }

    public void updateTournamentRound(String tournamentId, int round) {
        _tournamentDao.updateTournamentRound(tournamentId, round);
    }

    public List<Tournament> getOldTournaments(long since) {
        List<Tournament> result = new ArrayList<Tournament>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getFinishedTournamentsSince(since)) {
            Tournament tournament = _tournamentById.get(tournamentInfo.getTournamentId());
            if (tournament == null)
                tournament = createTournamentAndStoreInCache(tournamentInfo.getTournamentId(), tournamentInfo);
            result.add(tournament);
        }
        return result;
    }

    public List<Tournament> getLiveTournaments() {
        List<Tournament> result = new ArrayList<Tournament>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getUnfinishedTournaments()) {
            Tournament tournament = _tournamentById.get(tournamentInfo.getTournamentId());
            if (tournament == null)
                tournament = createTournamentAndStoreInCache(tournamentInfo.getTournamentId(), tournamentInfo);
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
        try {
            DraftPack draftPack = null;
            String draftType = tournamentInfo.getDraftType();
            if (draftType != null)
                _draftPackStorage.getDraftPack(draftType);

            tournament = new DefaultTournament(_collectionsManager, this, _packsStorage, draftPack,
                    tournamentId,  tournamentInfo.getTournamentName(), tournamentInfo.getTournamentFormat(),
                    tournamentInfo.getCollectionType(), tournamentInfo.getTournamentRound(), tournamentInfo.getTournamentStage(), 
                    _pairingMechanismRegistry.getPairingMechanism(tournamentInfo.getPairingMechanism()),
                    _tournamentPrizeSchemeRegistry.getTournamentPrizes(_cardSets, tournamentInfo.getPrizesScheme()));

        } catch (Exception exp) {
            throw new RuntimeException("Unable to create Tournament", exp);
        }
        _tournamentById.put(tournamentId, tournament);
        return tournament;
    }

    public void addRoundBye(String tournamentId, String player, int round) {
        _tournamentMatchDao.addBye(tournamentId, player, round);
    }

    public Map<String, Integer> getPlayerByes(String tournamentId) {
        return _tournamentMatchDao.getPlayerByes(tournamentId);
    }

    public List<TournamentQueueInfo> getUnstartedScheduledTournamentQueues(long tillDate) {
        return _tournamentDao.getUnstartedScheduledTournamentQueues(tillDate);
    }

    public void updateScheduledTournamentStarted(String scheduledTournamentId) {
        _tournamentDao.updateScheduledTournamentStarted(scheduledTournamentId);
    }
}
