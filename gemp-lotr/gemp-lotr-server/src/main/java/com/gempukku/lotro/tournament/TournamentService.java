package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft.DraftPack;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.packs.DraftPackStorage;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.packs.ProductLibrary;

import java.util.*;

import org.apache.log4j.Logger;


public class TournamentService implements ITournamentService {
    private static final Logger logger = Logger.getLogger(ITournamentService.class);
    private final ProductLibrary _productLibrary;
    private final DraftPackStorage _draftPackStorage;
    private final PairingMechanismRegistry _pairingMechanismRegistry;
    private final TournamentPrizeSchemeRegistry _tournamentPrizeSchemeRegistry;
    private final TournamentDAO _tournamentDao;
    private final TournamentPlayerDAO _tournamentPlayerDao;
    private final TournamentMatchDAO _tournamentMatchDao;
    private final LotroCardBlueprintLibrary _library;

    private final CollectionsManager _collectionsManager;

    private final Map<String, Tournament> _tournamentById = new HashMap<>();

    public TournamentService(CollectionsManager collectionsManager, ProductLibrary productLibrary, DraftPackStorage draftPackStorage,
                             PairingMechanismRegistry pairingMechanismRegistry, TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry,
                             TournamentDAO tournamentDao, TournamentPlayerDAO tournamentPlayerDao, TournamentMatchDAO tournamentMatchDao,
                             LotroCardBlueprintLibrary library) {
        _collectionsManager = collectionsManager;
        _productLibrary = productLibrary;
        _draftPackStorage = draftPackStorage;
        _pairingMechanismRegistry = pairingMechanismRegistry;
        _tournamentPrizeSchemeRegistry = tournamentPrizeSchemeRegistry;
        _tournamentDao = tournamentDao;
        _tournamentPlayerDao = tournamentPlayerDao;
        _tournamentMatchDao = tournamentMatchDao;
        _library = library;
    }

    @Override
    public void clearCache() {
        _tournamentById.clear();
    }

    @Override
    public void addPlayer(String tournamentId, String playerName, LotroDeck deck) {
        _tournamentPlayerDao.addPlayer(tournamentId, playerName, deck);
    }

    @Override
    public void dropPlayer(String tournamentId, String playerName) {
        _tournamentPlayerDao.dropPlayer(tournamentId, playerName);
    }

    @Override
    public Set<String> getPlayers(String tournamentId) {
        return _tournamentPlayerDao.getPlayers(tournamentId);
    }

    @Override
    public Map<String, LotroDeck> getPlayerDecks(String tournamentId, String format) {
        return _tournamentPlayerDao.getPlayerDecks(tournamentId, format);
    }

    @Override
    public Set<String> getDroppedPlayers(String tournamentId) {
        return _tournamentPlayerDao.getDroppedPlayers(tournamentId);
    }

    @Override
    public LotroDeck getPlayerDeck(String tournamentId, String player, String format) {
        return _tournamentPlayerDao.getPlayerDeck(tournamentId, player, format);
    }

    @Override
    public void addMatch(String tournamentId, int round, String playerOne, String playerTwo) {
        _tournamentMatchDao.addMatch(tournamentId, round, playerOne, playerTwo);
    }

    @Override
    public void setMatchResult(String tournamentId, int round, String winner) {
        _tournamentMatchDao.setMatchResult(tournamentId, round, winner);
    }

    @Override
    public void setPlayerDeck(String tournamentId, String player, LotroDeck deck) {
        _tournamentPlayerDao.updatePlayerDeck(tournamentId, player, deck);
    }

    @Override
    public List<TournamentMatch> getMatches(String tournamentId) {
        return _tournamentMatchDao.getMatches(tournamentId);
    }

    @Override
    public Tournament addTournament(String tournamentId, String draftType, String tournamentName, String format, CollectionType collectionType, Tournament.Stage stage, String pairingMechanism, String prizeScheme, Date start) {
        _tournamentDao.addTournament(tournamentId, draftType, tournamentName, format, collectionType, stage, pairingMechanism, prizeScheme, start);
        return createTournamentAndStoreInCache(tournamentId, new TournamentInfo(tournamentId, draftType, tournamentName, format, collectionType, stage, pairingMechanism, prizeScheme, 0));
    }

    @Override
    public void updateTournamentStage(String tournamentId, Tournament.Stage stage) {
        _tournamentDao.updateTournamentStage(tournamentId, stage);
    }

    @Override
    public void updateTournamentRound(String tournamentId, int round) {
        _tournamentDao.updateTournamentRound(tournamentId, round);
    }

    @Override
    public List<Tournament> getOldTournaments(long since) {
        List<Tournament> result = new ArrayList<>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getFinishedTournamentsSince(since)) {
            Tournament tournament = _tournamentById.get(tournamentInfo.getTournamentId());
            if (tournament == null)
                tournament = createTournamentAndStoreInCache(tournamentInfo.getTournamentId(), tournamentInfo);
            result.add(tournament);
        }
        return result;
    }

    @Override
    public List<Tournament> getLiveTournaments() {
        logger.debug("Calling getLiveTournaments function");
        List<Tournament> result = new ArrayList<>();
        for (TournamentInfo tournamentInfo : _tournamentDao.getUnfinishedTournaments()) {
            Tournament tournament = _tournamentById.get(tournamentInfo.getTournamentId());
            logger.debug("Adding tournament " + tournament);
            if (tournament == null)
                tournament = createTournamentAndStoreInCache(tournamentInfo.getTournamentId(), tournamentInfo);
            result.add(tournament);
        }
        return result;
    }

    @Override
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

            tournament = new DefaultTournament(_collectionsManager, this, _productLibrary, draftPack,
                    tournamentId,  tournamentInfo.getTournamentName(), tournamentInfo.getTournamentFormat(),
                    tournamentInfo.getCollectionType(), tournamentInfo.getTournamentRound(), tournamentInfo.getTournamentStage(), 
                    _pairingMechanismRegistry.getPairingMechanism(tournamentInfo.getPairingMechanism()),
                    _tournamentPrizeSchemeRegistry.getTournamentPrizes(_library, tournamentInfo.getPrizesScheme()));

        } catch (Exception exp) {
            throw new RuntimeException("Unable to create Tournament", exp);
        }
        _tournamentById.put(tournamentId, tournament);
        return tournament;
    }

    @Override
    public void addRoundBye(String tournamentId, String player, int round) {
        _tournamentMatchDao.addBye(tournamentId, player, round);
    }

    @Override
    public Map<String, Integer> getPlayerByes(String tournamentId) {
        return _tournamentMatchDao.getPlayerByes(tournamentId);
    }

    @Override
    public List<TournamentQueueInfo> getUnstartedScheduledTournamentQueues(long tillDate) {
        return _tournamentDao.getUnstartedScheduledTournamentQueues(tillDate);
    }

    @Override
    public void updateScheduledTournamentStarted(String scheduledTournamentId) {
        _tournamentDao.updateScheduledTournamentStarted(scheduledTournamentId);
    }
}
