package com.gempukku.lotro.league;

public enum SealedLeagueType {
    FOTR_BLOCK("fotr_block", "limited_fotr"),
    TTT_BLOCK("ttt_block", "limited_ttt"),
    MOVIE_BLOCK("movie", "limited_king");

    public static SealedLeagueType getLeagueType(String sealedCode) {
        for (SealedLeagueType sealedLeagueType : SealedLeagueType.values()) {
            if (sealedLeagueType.getSealedCode().equals(sealedCode))
                return sealedLeagueType;
        }
        return null;
    }

    private String _sealedCode;
    private String _format;

    private SealedLeagueType(String sealedCode, String format) {
        _sealedCode = sealedCode;
        _format = format;
    }

    public String getSealedCode() {
        return _sealedCode;
    }

    public String getFormat() {
        return _format;
    }
}
