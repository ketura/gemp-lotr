package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;

public class TournamentInfo {
    private final String _tournamentId;
    private final String _draftType;
    private final String _tournamentName;
    private final String _tournamentFormat;
    private final CollectionType _collectionType;
    private final String _prizesScheme;
    private final int _tournamentRound;
    private final String _pairingMechanism;
    private final Tournament.Stage _tournamentStage;

    public TournamentInfo(String tournamentId, String draftType, String tournamentName, String tournamentFormat, CollectionType collectionType,
                          Tournament.Stage tournamentStage, String pairingMechanism, String prizesScheme, int tournamentRound) {
        _tournamentId = tournamentId;
        _draftType = draftType;
        _tournamentName = tournamentName;
        _tournamentFormat = tournamentFormat;
        _collectionType = collectionType;
        _prizesScheme = prizesScheme;
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

    public String getPrizesScheme() {
        return _prizesScheme;
    }

    public Tournament.Stage getTournamentStage() {
        return _tournamentStage;
    }
}
