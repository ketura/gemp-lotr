package com.gempukku.lotro.db.vo;

import java.util.Date;

public class GameHistoryEntry {
    private String _winner;
    private String _loser;

    private String _winReason;
    private String _loseReason;

    private String _winnerRecording;
    private String _loserRecording;

    private Date _startTime;
    private Date _endTime;

    public GameHistoryEntry(String winner, String winReason, String winnerRecording, String loser, String loseReason, String loserRecording, Date startTime, Date endTime) {
        _winner = winner;
        _winReason = winReason;
        _winnerRecording = winnerRecording;
        _loser = loser;
        _loseReason = loseReason;
        _loserRecording = loserRecording;
        _startTime = startTime;
        _endTime = endTime;
    }

    public String getLoser() {
        return _loser;
    }

    public String getLoseReason() {
        return _loseReason;
    }

    public String getLoserRecording() {
        return _loserRecording;
    }

    public String getWinner() {
        return _winner;
    }

    public String getWinnerRecording() {
        return _winnerRecording;
    }

    public String getWinReason() {
        return _winReason;
    }

    public Date getEndTime() {
        return _endTime;
    }

    public Date getStartTime() {
        return _startTime;
    }
}
