package com.gempukku.lotro.hall;

public class RunningTable {
    private String _gameId;
    private String _formatName;
    private String _tournamentName;

    public RunningTable(String gameId, String formatName, String tournamentName) {
        _gameId = gameId;
        _formatName = formatName;
        _tournamentName = tournamentName;
    }

    public String getFormatName() {
        return _formatName;
    }

    public String getGameId() {
        return _gameId;
    }

    public String getTournamentName() {
        return _tournamentName;
    }
}
