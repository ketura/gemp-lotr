package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;

public class TournamentInfo {
    private String _tournamentId;
    private String _draftType;
    private String _tournamentName;
    private String _tournamentFormat;
    private CollectionType _collectionType;
    private int _tournamentRound;
    private String _pairingMechanism;
    private Tournament.Stage _tournamentStage;

    public TournamentInfo(String tournamentId, String draftType, String tournamentName, String tournamentFormat, CollectionType collectionType, Tournament.Stage tournamentStage, String pairingMechanism, int tournamentRound) {
        _tournamentId = tournamentId;
        _draftType = draftType;
        _tournamentName = tournamentName;
        _tournamentFormat = tournamentFormat;
        _collectionType = collectionType;
        _tournamentRound = tournamentRound;
        _pairingMechanism = pairingMechanism;
        _tournamentStage = tournamentStage;
    }

    public String getTournamentId() {
        return _tournamentId;
    }

    public String getDraftType() {
        return _draftType;
    }

    public String getTournamentName() {
        return _tournamentName;
    }

    public String getTournamentFormat() {
        return _tournamentFormat;
    }

    public CollectionType getCollectionType() {
        return _collectionType;
    }

    public int getTournamentRound() {
        return _tournamentRound;
    }

    public String getPairingMechanism() {
        return _pairingMechanism;
    }

    public Tournament.Stage getTournamentStage() {
        return _tournamentStage;
    }
}
