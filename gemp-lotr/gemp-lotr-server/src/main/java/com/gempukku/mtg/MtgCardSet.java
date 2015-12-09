package com.gempukku.mtg;

public class MtgCardSet {
    private String _urlPostfix;
    private String _infoLine;

    public MtgCardSet(String urlPostfix, String infoLine) {
        _urlPostfix = urlPostfix;
        _infoLine = infoLine;
    }

    public String getInfoLine() {
        return _infoLine;
    }

    public String getUrlPostfix() {
        return _urlPostfix;
    }
}
