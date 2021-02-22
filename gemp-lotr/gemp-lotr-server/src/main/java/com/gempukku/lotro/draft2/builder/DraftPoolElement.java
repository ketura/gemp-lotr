package com.gempukku.lotro.draft2.builder;

import java.util.ArrayList;
import java.util.List;

public interface DraftPoolElement {
    String getDraftPoolType();

    List<ArrayList<String>> getDraftPackList();

    int getPacksToDraft();
}
