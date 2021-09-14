package com.gempukku.lotro.hall;

import com.gempukku.lotro.*;
import com.gempukku.lotro.chat.ChatCommandCallback;
import com.gempukku.lotro.chat.ChatCommandErrorException;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.IgnoreDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.service.AdminService;
import com.gempukku.lotro.tournament.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HallServer extends AbstractServer {
    private static final GameTimer DEFAULT_TIMER = new GameTimer(false, "Default", 60 * 80, 60 * 5);
    private static final GameTimer BLITZ_TIMER = new GameTimer(false, "Blitz!", 60 * 30, 60 * 3);
    private static final GameTimer SLOW_TIMER = new GameTimer(false, "Slow", 60 * 120, 60 * 10);
    private static final GameTimer GLACIAL_TIMER = new GameTimer(true, "Glacial", 60 * 60 * 24 * 3, 60 * 60 * 24);
    // 5 minutes timeout, 40 minutes per game per player
    private static final GameTimer COMPETITIVE_TIMER = new GameTimer(false, "Competitive", 60 * 40, 60 * 5);

    private static final int _playerInactivityPeriod = 1000 * 20; // 20 seconds
    private static final long _scheduledTournamentLoadTime = 1000 * 60 * 60 * 24 * 7; // Week
    // Repeat tournaments every 2 days
    private static final long _repeatTournaments = 1000 * 60 * 60 * 24 * 2;

    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private TournamentService _tournamentService;
    private LotroCardBlueprintLibrary _library;
    private LotroFormatLibrary _formatLibrary;
    private CollectionsManager _collectionsManager;
    private LotroServer _lotroServer;
    private PairingMechanismRegistry _pairingMechanismRegistry;
    private CardSets _cardSets;
    private AdminService _adminService;
    private TournamentPrizeSchemeRegistry _tournamentPrizeSchemeRegistry;

    private CollectionType _defaultCollectionType = CollectionType.ALL_CARDS;
    private CollectionType _tournamentCollectionType = CollectionType.OWNED_TOURNAMENT_CARDS;

    private String _motd;

    private boolean _shutdown;

    private ReadWriteLock _hallDataAccessLock = new ReentrantReadWriteLock(false);

    private TableHolder tableHolder;

    private Map<Player, HallCommunicationChannel> _playerChannelCommunication = new ConcurrentHashMap<Player, HallCommunicationChannel>();
    private int _nextChannelNumber = 0;

    private Map<String, Tournament> _runningTournaments = new LinkedHashMap<String, Tournament>();

    private Map<String, TournamentQueue> _tournamentQueues = new LinkedHashMap<String, TournamentQueue>();
    private final ChatRoomMediator _hallChat;
    private final GameResultListener _notifyHallListeners = new NotifyHallListenersGameResultListener();

    public HallServer(IgnoreDAO ignoreDAO, LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, TournamentService tournamentService, LotroCardBlueprintLibrary library,
                      LotroFormatLibrary formatLibrary, CollectionsManager collectionsManager,
                      AdminService adminService,
                      TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry,
                      PairingMechanismRegistry pairingMechanismRegistry, CardSets cardSets) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _tournamentService = tournamentService;
        _library = library;
        _formatLibrary = formatLibrary;
        _collectionsManager = collectionsManager;
        _adminService = adminService;
        _tournamentPrizeSchemeRegistry = tournamentPrizeSchemeRegistry;
        _pairingMechanismRegistry = pairingMechanismRegistry;
        _cardSets = cardSets;

        tableHolder = new TableHolder(leagueService, ignoreDAO);

        _hallChat = _chatServer.createChatRoom("Game Hall", true, 15, true,
                "You're now in the Game Hall, use /help to get a list of available commands.<br>Don't forget to check out the new Discord chat integration! Click the 'Switch to Discord' button in the lower right ---->");
        _hallChat.addChatCommandCallback("ban",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banUser(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });
        _hallChat.addChatCommandCallback("banIp",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banIp(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });
        _hallChat.addChatCommandCallback("banIpRange",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banIpPrefix(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });
        _hallChat.addChatCommandCallback("ignore",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        final String playerName = parameters.trim();
                        if (playerName.length() >= 2 && playerName.length() <= 10) {
                            if (!from.equals(playerName) && ignoreDAO.addIgnoredUser(from, playerName)) {
                                _hallChat.sendToUser(from, from, "/ignore " + playerName);
                                _hallChat.sendToUser("System", from, "User " + playerName + " added to ignore list");
                            }
                        }
                    }
                });
        _hallChat.addChatCommandCallback("unignore",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        final String playerName = parameters.trim();
                        if (playerName.length() >= 2 && playerName.length() <= 10) {
                            if (ignoreDAO.removeIgnoredUser(from, playerName)) {
                                _hallChat.sendToUser(from, from, "/unignore " + playerName);
                                _hallChat.sendToUser("System", from, "User " + playerName + " removed from ignore list");
                            }
                        }
                    }
                });
        _hallChat.addChatCommandCallback("listIgnores",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        final Set<String> ignoredUsers = ignoreDAO.getIgnoredUsers(from);
                        _hallChat.sendToUser(from, from, "/listIgnores");
                        _hallChat.sendToUser("System", from, "Your ignores: " + Arrays.toString(ignoredUsers.toArray(new String[0])));
                    }
                });
        _hallChat.addChatCommandCallback("incognito",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        _hallChat.setIncognito(from, true);
                        _hallChat.sendToUser(from, from, "/incognito");
                        _hallChat.sendToUser("System", from, "You are now incognito (do not appear in user list)");
                    }
                });
        _hallChat.addChatCommandCallback("endIncognito",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        _hallChat.setIncognito(from, false);
                        _hallChat.sendToUser(from, from, "/endIncognito");
                        _hallChat.sendToUser("System", from, "You are no longer incognito");
                    }
                });
        _hallChat.addChatCommandCallback("help",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        _hallChat.sendToUser("System", from, "List of available commands:");
                        _hallChat.sendToUser("System", from, "/ignore username - Adds user 'username' to list of your ignores");
                        _hallChat.sendToUser("System", from, "/unignore username - Removes user 'username' from list of your ignores");
                        _hallChat.sendToUser("System", from, "/listIgnores - Lists all your ignored users");
                        _hallChat.sendToUser("System", from, "/incognito - Makes you incognito (not visible in user list)");
                        _hallChat.sendToUser("System", from, "/endIncognito - Turns your visibility 'on' again");
                        if (admin) {
                            _hallChat.sendToUser("System", from, "Admin only commands:");
                            _hallChat.sendToUser("System", from, "/ban username - Bans user 'username' permanently");
                            _hallChat.sendToUser("System", from, "/banIp ip - Bans specified ip permanently");
                            _hallChat.sendToUser("System", from, "/banIpRange ip - Bans ips with the specified prefix, ie. 10.10.10.");
                        }
                    }
                });

        _tournamentQueues.put("fotr_queue", new ImmediateRecurringQueue(1500, "fotr_block",
                CollectionType.ALL_CARDS, "fotrQueue-", "Fellowship Block", 4,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));
        _tournamentQueues.put("ts_queue", new ImmediateRecurringQueue(1500, "towers_standard",
                CollectionType.ALL_CARDS, "tsQueue-", "Towers Standard", 4,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));
        _tournamentQueues.put("movie_queue", new ImmediateRecurringQueue(1500, "movie",
                CollectionType.ALL_CARDS, "movieQueue-", "Movie Block", 4,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));
        _tournamentQueues.put("expanded_queue", new ImmediateRecurringQueue(1500, "expanded",
                CollectionType.ALL_CARDS, "expandedQueue-", "Expanded", 4,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            _tournamentQueues.put("fotr_daily_eu", new RecurringScheduledQueue(sdf.parse("2013-01-15 19:30:00").getTime(), _repeatTournaments, "fotrDailyEu-", "Daily Gondor Fellowship Block", 0,
                    true, _defaultCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "fotr_block", 4));
            _tournamentQueues.put("fotr_daily_us", new RecurringScheduledQueue(sdf.parse("2013-01-16 00:30:00").getTime(), _repeatTournaments, "fotrDailyUs-", "Daily Rohan Fellowship Block", 0,
                    true, _defaultCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "fotr_block", 4));
            _tournamentQueues.put("movie_daily_eu", new RecurringScheduledQueue(sdf.parse("2013-01-16 19:30:00").getTime(), _repeatTournaments, "movieDailyEu-", "Daily Gondor Movie Block", 0,
                    true, _defaultCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "movie", 4));
            _tournamentQueues.put("movie_daily_us", new RecurringScheduledQueue(sdf.parse("2013-01-17 00:30:00").getTime(), _repeatTournaments, "movieDailyUs-", "Daily Rohan Movie Block", 0,
                    true, _defaultCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "movie", 4));
        } catch (ParseException exp) {
            // Ignore, can't happen
        }
    }

    private void hallChanged() {
        for (HallCommunicationChannel hallCommunicationChannel : _playerChannelCommunication.values())
            hallCommunicationChannel.hallChanged();
    }

    @Override
    protected void doAfterStartup() {
        for (Tournament tournament : _tournamentService.getLiveTournaments())
            _runningTournaments.put(tournament.getTournamentId(), tournament);
    }

    public void setShutdown(boolean shutdown) {
        _hallDataAccessLock.writeLock().lock();
        try {
            _shutdown = shutdown;
            if (shutdown) {
                cancelWaitingTables();
                cancelTournamentQueues();
                _chatServer.sendSystemMessageToAllChatRooms("System is entering shutdown mode and will be restarted when all games are finished");
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void setMOTD(String motd) {
        _hallDataAccessLock.writeLock().lock();
        try {
            _motd = motd;
            hallChanged();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public int getTablesCount() {
        _hallDataAccessLock.readLock().lock();
        try {
            return tableHolder.getTableCount();
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private void cancelWaitingTables() {
        tableHolder.cancelWaitingTables();
    }

    private void cancelTournamentQueues() {
        for (TournamentQueue tournamentQueue : _tournamentQueues.values())
            tournamentQueue.leaveAllPlayers(_collectionsManager);
    }

    /**
     * @return If table created, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public void createNewTable(String type, Player player, String deckName, String timer) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        GameSettings gameSettings = createGameSettings(type, timer);

        LotroDeck lotroDeck = validateUserAndDeck(gameSettings.getLotroFormat(), player, deckName, gameSettings.getCollectionType());

        _hallDataAccessLock.writeLock().lock();
        try {
            final GameTable table = tableHolder.createTable(player, gameSettings, lotroDeck);
            if (table != null)
                createGameFromTable(table);

            hallChanged();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private GameSettings createGameSettings(String type, String timer) throws HallException {
        League league = null;
        LeagueSerieData leagueSerie = null;
        CollectionType collectionType = _defaultCollectionType;
        LotroFormat format = _formatLibrary.getHallFormats().get(type);
        GameTimer gameTimer = resolveTimer(timer);

        if (format == null) {
            // Maybe it's a league format?
            league = _leagueService.getLeagueByType(type);
            if (league != null) {
                leagueSerie = _leagueService.getCurrentLeagueSerie(league);
                if (leagueSerie == null)
                    throw new HallException("There is no ongoing serie for that league");

                format = _formatLibrary.getFormat(leagueSerie.getFormat());
                collectionType = leagueSerie.getCollectionType();

                gameTimer = COMPETITIVE_TIMER;
            }
        }
        // It's not a normal format and also not a league one
        if (format == null)
            throw new HallException("This format is not supported: " + type);

        return new GameSettings(collectionType, format, league, leagueSerie,
                league != null, gameTimer.isLongGame(), gameTimer.getName(), gameTimer.getMaxSecondsPerPlayer(), gameTimer.getMaxSecondsPerDecision());
    }

    private GameTimer resolveTimer(String timer) {
        if (timer != null) {
            if (timer.equals("blitz")) {
                return BLITZ_TIMER;
            } else if (timer.equals("slow")) {
                return SLOW_TIMER;
            } else if (timer.equals("glacial")) {
                return GLACIAL_TIMER;
            }
        }
        return DEFAULT_TIMER;
    }

    public boolean joinQueue(String queueId, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            TournamentQueue tournamentQueue = _tournamentQueues.get(queueId);
            if (tournamentQueue == null)
                throw new HallException("Tournament queue already finished accepting players, try again in a few seconds");
            if (tournamentQueue.isPlayerSignedUp(player.getName()))
                throw new HallException("You have already joined that queue");

            LotroDeck lotroDeck = null;
            if (tournamentQueue.isRequiresDeck())
                lotroDeck = validateUserAndDeck(_formatLibrary.getFormat(tournamentQueue.getFormat()), player, deckName, tournamentQueue.getCollectionType());

            tournamentQueue.joinPlayer(_collectionsManager, player, lotroDeck);

            hallChanged();

            return true;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    /**
     * @return If table joined, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public boolean joinTableAsPlayer(String tableId, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        GameSettings gameSettings = tableHolder.getGameSettings(tableId);
        LotroDeck lotroDeck = validateUserAndDeck(gameSettings.getLotroFormat(), player, deckName, gameSettings.getCollectionType());

        _hallDataAccessLock.writeLock().lock();
        try {
            final GameTable runningTable = tableHolder.joinTable(tableId, player, lotroDeck);
            if (runningTable != null)
                createGameFromTable(runningTable);

            hallChanged();

            return true;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void leaveQueue(String queueId, Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            TournamentQueue tournamentQueue = _tournamentQueues.get(queueId);
            if (tournamentQueue != null && tournamentQueue.isPlayerSignedUp(player.getName())) {
                tournamentQueue.leavePlayer(_collectionsManager, player);
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private boolean leaveQueuesForLeavingPlayer(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            boolean result = false;
            for (TournamentQueue tournamentQueue : _tournamentQueues.values()) {
                if (tournamentQueue.isPlayerSignedUp(player.getName())) {
                    tournamentQueue.leavePlayer(_collectionsManager, player);
                    result = true;
                }
            }
            return result;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void dropFromTournament(String tournamentId, Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament != null) {
                tournament.dropPlayer(player.getName());
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void leaveAwaitingTable(Player player, String tableId) {
        _hallDataAccessLock.writeLock().lock();
        try {
            if (tableHolder.leaveAwaitingTable(player, tableId))
                hallChanged();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public boolean leaveAwaitingTablesForLeavingPlayer(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            return tableHolder.leaveAwaitingTablesForPlayer(player);
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void signupUserForHall(Player player, HallChannelVisitor hallChannelVisitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            HallCommunicationChannel channel = new HallCommunicationChannel(_nextChannelNumber++);
            channel.processCommunicationChannel(this, player, hallChannelVisitor);
            _playerChannelCommunication.put(player, channel);
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public HallCommunicationChannel getCommunicationChannel(Player player, int channelNumber) throws SubscriptionExpiredException, SubscriptionConflictException {
        _hallDataAccessLock.readLock().lock();
        try {
            HallCommunicationChannel communicationChannel = _playerChannelCommunication.get(player);
            if (communicationChannel != null) {
                if (communicationChannel.getChannelNumber() == channelNumber) {
                    return communicationChannel;
                } else {
                    throw new SubscriptionConflictException();
                }
            } else {
                throw new SubscriptionExpiredException();
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    protected void processHall(Player player, HallInfoVisitor visitor) {
        final boolean isAdmin = player.getType().contains("a");

        _hallDataAccessLock.readLock().lock();
        try {
            visitor.serverTime(DateUtils.getStringDateWithHour());
            if (_motd != null)
                visitor.motd(_motd);

            tableHolder.processTables(isAdmin, player, visitor);

            for (Map.Entry<String, TournamentQueue> tournamentQueueEntry : _tournamentQueues.entrySet()) {
                String tournamentQueueKey = tournamentQueueEntry.getKey();
                TournamentQueue tournamentQueue = tournamentQueueEntry.getValue();
                visitor.visitTournamentQueue(tournamentQueueKey, tournamentQueue.getCost(), tournamentQueue.getCollectionType().getFullName(),
                        _formatLibrary.getFormat(tournamentQueue.getFormat()).getName(), tournamentQueue.getTournamentQueueName(),
                        tournamentQueue.getPrizesDescription(), tournamentQueue.getPairingDescription(), tournamentQueue.getStartCondition(),
                        tournamentQueue.getPlayerCount(), tournamentQueue.isPlayerSignedUp(player.getName()), tournamentQueue.isJoinable());
            }

            for (Map.Entry<String, Tournament> tournamentEntry : _runningTournaments.entrySet()) {
                String tournamentKey = tournamentEntry.getKey();
                Tournament tournament = tournamentEntry.getValue();
                visitor.visitTournament(tournamentKey, tournament.getCollectionType().getFullName(),
                        _formatLibrary.getFormat(tournament.getFormat()).getName(), tournament.getTournamentName(), tournament.getPlayOffSystem(),
                        tournament.getTournamentStage().getHumanReadable(),
                        tournament.getCurrentRound(), tournament.getPlayersInCompetitionCount(), tournament.isPlayerInCompetition(player.getName()));
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, String deckName, CollectionType collectionType) throws HallException {
        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(player, deckName);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            lotroDeck = format.applyErrata(lotroDeck);
            lotroDeck = validateUserAndDeck(format, player, collectionType, lotroDeck);
        } catch (DeckInvalidException e) {
            throw new HallException("Your selected deck is not valid for this format: " + e.getMessage());
        }

        return lotroDeck;
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, CollectionType collectionType, LotroDeck lotroDeck) throws HallException, DeckInvalidException {
        format.validateDeck(lotroDeck);

        // Now check if player owns all the cards
        if (collectionType.getCode().equals("default")) {
            CardCollection ownedCollection = _collectionsManager.getPlayerCollection(player, "permanent+trophy");

            LotroDeck filteredSpecialCardsDeck = new LotroDeck(lotroDeck.getDeckName());
            if (lotroDeck.getRing() != null)
                filteredSpecialCardsDeck.setRing(filterCard(lotroDeck.getRing(), ownedCollection));
            filteredSpecialCardsDeck.setRingBearer(filterCard(lotroDeck.getRingBearer(), ownedCollection));

            for (String site : lotroDeck.getSites())
                filteredSpecialCardsDeck.addSite(filterCard(site, ownedCollection));

            for (Map.Entry<String, Integer> cardCount : CollectionUtils.getTotalCardCount(lotroDeck.getAdventureCards()).entrySet()) {
                String blueprintId = cardCount.getKey();
                int count = cardCount.getValue();

                int owned = ownedCollection.getItemCount(blueprintId);
                int fromOwned = Math.min(owned, count);

                for (int i = 0; i < fromOwned; i++)
                    filteredSpecialCardsDeck.addCard(blueprintId);
                if (count - fromOwned > 0) {
                    String baseBlueprintId = _library.getBaseBlueprintId(blueprintId);
                    for (int i = 0; i < (count - fromOwned); i++)
                        filteredSpecialCardsDeck.addCard(baseBlueprintId);
                }
            }

            lotroDeck = filteredSpecialCardsDeck;
        } else {
            CardCollection collection = _collectionsManager.getPlayerCollection(player, collectionType.getCode());
            if (collection == null)
                throw new HallException("You don't have cards in the required collection to play in this format");

            Map<String, Integer> deckCardCounts = CollectionUtils.getTotalCardCountForDeck(lotroDeck);

            for (Map.Entry<String, Integer> cardCount : deckCardCounts.entrySet()) {
                final int collectionCount = collection.getItemCount(cardCount.getKey());
                if (collectionCount < cardCount.getValue()) {
                    String cardName = null;
                    try {
                        cardName = GameUtils.getFullName(_library.getLotroCardBlueprint(cardCount.getKey()));
                        throw new HallException("You don't have the required cards in collection: " + cardName + " required " + cardCount.getValue() + ", owned " + collectionCount);
                    } catch (CardNotFoundException e) {
                        // Ignore, card player has in a collection, should not disappear
                    }
                }
            }
        }
        return lotroDeck;
    }

    private String filterCard(String blueprintId, CardCollection ownedCollection) {
        if (ownedCollection.getItemCount(blueprintId) == 0)
            return _library.getBaseBlueprintId(blueprintId);
        return blueprintId;
    }

    private String getTournamentName(GameTable table) {
        final League league = table.getGameSettings().getLeague();
        if (league != null)
            return league.getName() + " - " + table.getGameSettings().getLeagueSerie().getName();
        else
            return "Casual - " + table.getGameSettings().getTimerName();
    }

    private void createGameFromTable(GameTable gameTable) {
        Set<LotroGameParticipant> players = gameTable.getPlayers();
        LotroGameParticipant[] participants = players.toArray(new LotroGameParticipant[0]);
        final League league = gameTable.getGameSettings().getLeague();
        final LeagueSerieData leagueSerie = gameTable.getGameSettings().getLeagueSerie();

        GameResultListener listener = null;
        if (league != null) {
            listener = new GameResultListener() {
                @Override
                public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                    _leagueService.reportLeagueGameResult(league, leagueSerie, winnerPlayerId, loserPlayerIdsWithReasons.keySet().iterator().next());
                }

                @Override
                public void gameCancelled() {
                    // Do nothing...
                }
            };
        }

        LotroGameMediator mediator = createGameMediator(participants, listener, getTournamentName(gameTable), gameTable.getGameSettings());
        gameTable.startGame(mediator);
    }

    private LotroGameMediator createGameMediator(LotroGameParticipant[] participants, GameResultListener listener, String tournamentName, GameSettings gameSettings) {
        final LotroGameMediator lotroGameMediator = _lotroServer.createNewGame(tournamentName, participants, gameSettings);
        if (listener != null)
            lotroGameMediator.addGameResultListener(listener);
        lotroGameMediator.startGame();
        lotroGameMediator.addGameResultListener(_notifyHallListeners);

        return lotroGameMediator;
    }

    private class NotifyHallListenersGameResultListener implements GameResultListener {
        @Override
        public void gameCancelled() {
            hallChanged();
        }

        @Override
        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
            hallChanged();
        }
    }

    private int _tickCounter = 60;

    @Override
    protected void cleanup() {
        _hallDataAccessLock.writeLock().lock();
        try {
            // Remove finished games
            tableHolder.removeFinishedGames();

            long currentTime = System.currentTimeMillis();
            Map<Player, HallCommunicationChannel> visitCopy = new LinkedHashMap<Player, HallCommunicationChannel>(_playerChannelCommunication);
            for (Map.Entry<Player, HallCommunicationChannel> lastVisitedPlayer : visitCopy.entrySet()) {
                if (currentTime > lastVisitedPlayer.getValue().getLastAccessed() + _playerInactivityPeriod) {
                    Player player = lastVisitedPlayer.getKey();
                    _playerChannelCommunication.remove(player);
                    boolean leftTables = leaveAwaitingTablesForLeavingPlayer(player);
                    boolean leftQueues = leaveQueuesForLeavingPlayer(player);
                    if (leftTables || leftQueues)
                        hallChanged();
                }
            }

            for (Map.Entry<String, TournamentQueue> runningTournamentQueue : new HashMap<String, TournamentQueue>(_tournamentQueues).entrySet()) {
                String tournamentQueueKey = runningTournamentQueue.getKey();
                TournamentQueue tournamentQueue = runningTournamentQueue.getValue();
                HallTournamentQueueCallback queueCallback = new HallTournamentQueueCallback();
                // If it's finished, remove it
                if (tournamentQueue.process(queueCallback, _collectionsManager)) {
                    _tournamentQueues.remove(tournamentQueueKey);
                    hallChanged();
                }
            }

            for (Map.Entry<String, Tournament> tournamentEntry : new HashMap<String, Tournament>(_runningTournaments).entrySet()) {
                Tournament runningTournament = tournamentEntry.getValue();
                boolean changed = runningTournament.advanceTournament(new HallTournamentCallback(runningTournament), _collectionsManager);
                if (runningTournament.getTournamentStage() == Tournament.Stage.FINISHED)
                    _runningTournaments.remove(tournamentEntry.getKey());
                if (changed)
                    hallChanged();
            }

            if (_tickCounter == 60) {
                _tickCounter = 0;
                List<TournamentQueueInfo> unstartedTournamentQueues = _tournamentService.getUnstartedScheduledTournamentQueues(
                        System.currentTimeMillis() + _scheduledTournamentLoadTime);
                for (TournamentQueueInfo unstartedTournamentQueue : unstartedTournamentQueues) {
                    String scheduledTournamentId = unstartedTournamentQueue.getScheduledTournamentId();
                    if (!_tournamentQueues.containsKey(scheduledTournamentId)) {
                        ScheduledTournamentQueue scheduledQueue = new ScheduledTournamentQueue(scheduledTournamentId, unstartedTournamentQueue.getCost(),
                                true, _tournamentService, unstartedTournamentQueue.getStartTime(), unstartedTournamentQueue.getTournamentName(),
                                unstartedTournamentQueue.getFormat(), CollectionType.ALL_CARDS, Tournament.Stage.PLAYING_GAMES,
                                _pairingMechanismRegistry.getPairingMechanism(unstartedTournamentQueue.getPlayOffSystem()),
                                _tournamentPrizeSchemeRegistry.getTournamentPrizes(_cardSets, unstartedTournamentQueue.getPrizeScheme()), unstartedTournamentQueue.getMinimumPlayers());
                        _tournamentQueues.put(scheduledTournamentId, scheduledQueue);
                        hallChanged();
                    }
                }
            }
            _tickCounter++;

        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private class HallTournamentQueueCallback implements TournamentQueueCallback {
        @Override
        public void createTournament(Tournament tournament) {
            _runningTournaments.put(tournament.getTournamentId(), tournament);
        }
    }

    private class HallTournamentCallback implements TournamentCallback {
        private Tournament _tournament;
        private GameSettings tournamentGameSettings;

        private HallTournamentCallback(Tournament tournament) {
            _tournament = tournament;
            tournamentGameSettings = new GameSettings(null, _formatLibrary.getFormat(_tournament.getFormat()),
                    null, null, true, false, "Tournament", 60 * 40, 60 * 5);
        }

        @Override
        public void createGame(String playerOne, LotroDeck deckOne, String playerTwo, LotroDeck deckTwo) {
            final LotroGameParticipant[] participants = new LotroGameParticipant[2];
            participants[0] = new LotroGameParticipant(playerOne, deckOne);
            participants[1] = new LotroGameParticipant(playerTwo, deckTwo);
            createGameInternal(participants);
        }

        private void createGameInternal(final LotroGameParticipant[] participants) {
            _hallDataAccessLock.writeLock().lock();
            try {
                if (!_shutdown) {
                    final GameTable gameTable = tableHolder.setupTournamentTable(tournamentGameSettings, participants);
                    final LotroGameMediator mediator = createGameMediator(participants,
                            new GameResultListener() {
                                @Override
                                public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                                    _tournament.reportGameFinished(winnerPlayerId, loserPlayerIdsWithReasons.keySet().iterator().next());
                                }

                                @Override
                                public void gameCancelled() {
                                    createGameInternal(participants);
                                }
                            }, _tournament.getTournamentName(), tournamentGameSettings);
                    gameTable.startGame(mediator);
                }
            } finally {
                _hallDataAccessLock.writeLock().unlock();
            }
        }

        @Override
        public void broadcastMessage(String message) {
            try {
                _hallChat.sendMessage("TournamentSystem", message, true);
            } catch (PrivateInformationException exp) {
                // Ignore, sent as admin
            } catch (ChatCommandErrorException e) {
                // Ignore, no command
            }
        }
    }
}
