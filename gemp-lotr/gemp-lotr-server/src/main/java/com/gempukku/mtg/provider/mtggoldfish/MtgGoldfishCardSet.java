package com.gempukku.mtg.provider.mtggoldfish;

public class MtgGoldfishCardSet {
    private String _urlPostfix;
    private String _infoLine;

    public MtgGoldfishCardSet(String urlPostfix, String infoLine) {
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
