package com.gempukku.lotro.hall;

import java.util.Map;

public interface HallChannelVisitor {
    public void channelNumber(int channelNumber);
    public void motdChanged(String motd);
    
    public void serverTime(String serverTime);
    public void runningPlayerGame(String gameId);
    public void playerBusy(boolean busy);

    public void addTournamentQueue(String queueId, Map<String, String> props);
    public void updateTournamentQueue(String queueId, Map<String, String> props);
    public void removeTournamentQueue(String queueId);

    public void addTable(String tableId, Map<String, String> props);
    public void updateTable(String tableId, Map<String, String> props);
    public void removeTable(String tableId);
}
