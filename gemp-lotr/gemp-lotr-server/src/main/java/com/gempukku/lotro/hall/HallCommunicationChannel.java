package com.gempukku.lotro.hall;

import com.gempukku.lotro.game.Player;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HallCommunicationChannel {
    private int _channelNumber;
    private long _lastConsumed;
    private Map<String, Map<String, String>> _tournamentQueuePropsOnClient = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> _tablePropsOnClient = new HashMap<String, Map<String, String>>();

    public HallCommunicationChannel(int channelNumber) {
        _channelNumber = channelNumber;
    }

    public int getChannelNumber() {
        return _channelNumber;
    }

    public long getLastConsumed() {
        return _lastConsumed;
    }

    public synchronized void processCommunicationChannel(HallServer hallServer, Player player, final HallChannelVisitor hallChannelVisitor) {
        hallChannelVisitor.channelNumber(_channelNumber);

        final Map<String, Map<String, String>> tournamentsOnServer = new HashMap<String, Map<String, String>>();
        final Map<String, Map<String, String>> tablesOnServer = new HashMap<String, Map<String, String>>();

        hallServer.processHall(player,
                new HallInfoVisitor() {
                    @Override
                    public void serverTime(String time) {
                        hallChannelVisitor.serverTime(time);
                    }

                    @Override
                    public void playerBusy(boolean busy) {
                        hallChannelVisitor.playerBusy(busy);
                    }

                    @Override
                    public void visitTable(String tableId, String gameId, boolean watchable, TableStatus status, String statusDescription, String formatName, String tournamentName, List<String> playerIds, String winner) {
                        Map<String, String> props = new HashMap<String, String>();
                        props.put("gameId", gameId);
                        props.put("watchable", String.valueOf(watchable));
                        props.put("status", String.valueOf(status));
                        props.put("statusDescription", statusDescription);
                        props.put("format", formatName);
                        props.put("tournament", tournamentName);
                        props.put("players", StringUtils.join(playerIds, ","));
                        if (winner != null)
                            props.put("winner", winner);

                        tablesOnServer.put(tableId, props);
                    }

                    @Override
                    public void visitTournamentQueue(String tournamentQueueKey, int cost, String collectionName, String formatName, String tournamentQueueName,
                                                     String tournamentPrizes, int playerCount, boolean playerSignedUp) {
                        Map<String, String> props = new HashMap<String, String>();
                        props.put("cost", String.valueOf(cost));
                        props.put("collection", collectionName);
                        props.put("format", formatName);
                        props.put("queue", tournamentQueueName);
                        props.put("playerCount", String.valueOf(playerCount));
                        props.put("prizes", tournamentPrizes);
                        props.put("signedUp", String.valueOf(playerSignedUp));

                        tournamentsOnServer.put(tournamentQueueKey, props);
                    }

                    @Override
                    public void runningPlayerGame(String gameId) {
                        hallChannelVisitor.runningPlayerGame(gameId);
                    }
                });

        for (Map.Entry<String, Map<String, String>> tournamentQueueOnClient : _tournamentQueuePropsOnClient.entrySet()) {
            String tournamentQueueId = tournamentQueueOnClient.getKey();
            Map<String, String> tournamentProps = tournamentQueueOnClient.getValue();
            Map<String, String> tournamentLatestProps = tournamentsOnServer.get(tournamentQueueId);
            if (tournamentLatestProps != null) {
                if (!tournamentProps.equals(tournamentLatestProps))
                    hallChannelVisitor.updateTournamentQueue(tournamentQueueId, tournamentLatestProps);
            } else {
                hallChannelVisitor.removeTournamentQueue(tournamentQueueId);
            }
        }

        for (Map.Entry<String, Map<String, String>> tournamentQueueOnServer : tournamentsOnServer.entrySet())
            if (!_tournamentQueuePropsOnClient.containsKey(tournamentQueueOnServer.getKey()))
                hallChannelVisitor.addTournamentQueue(tournamentQueueOnServer.getKey(), tournamentQueueOnServer.getValue());

        _tournamentQueuePropsOnClient = tournamentsOnServer;

        for (Map.Entry<String, Map<String, String>> tableOnClient : _tablePropsOnClient.entrySet()) {
            String tableId = tableOnClient.getKey();
            Map<String, String> tableProps = tableOnClient.getValue();
            Map<String, String> tableLatestProps = tablesOnServer.get(tableId);
            if (tableLatestProps != null) {
                if (!tableProps.equals(tableLatestProps))
                    hallChannelVisitor.updateTable(tableId, tableLatestProps);
            } else {
                hallChannelVisitor.removeTable(tableId);
            }
        }

        for (Map.Entry<String, Map<String, String>> tableOnServer : tablesOnServer.entrySet())
            if (!_tablePropsOnClient.containsKey(tableOnServer.getKey()))
                hallChannelVisitor.addTable(tableOnServer.getKey(), tableOnServer.getValue());

        _tablePropsOnClient = tablesOnServer;

        _lastConsumed = System.currentTimeMillis();
    }
}
