package com.gempukku.lotro.common;

public enum SitesBlock {
    FELLOWSHIP("Fellowship"), TWO_TOWERS("Towers"), KING("King"),
    SHADOWS("Shadows and onwards"), SPECIAL("Special"),
    SECOND_ED("2nd edition"),
	
	//Additional Hobbit Draft block
	HOBBIT("Hobbit");

    private String _humanReadable;

    private SitesBlock(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
