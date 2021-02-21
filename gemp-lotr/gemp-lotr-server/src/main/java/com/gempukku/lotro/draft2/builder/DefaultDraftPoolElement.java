package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.draft2.builder.DraftPoolElement;

import java.util.ArrayList;
import java.util.List;


public class DefaultDraftPoolElement implements DraftPoolElement {
    private List<ArrayList<String>> _draftPackList;
    private String _draftPoolType;
    private int _packsToDraft;
        
    public DefaultDraftPoolElement(String draftPoolType, List<ArrayList<String>> draftPackList, int packsToDraft) {
        _draftPoolType = draftPoolType;
        _draftPackList = draftPackList;
        _packsToDraft = packsToDraft;
    }

    @Override
    public String getDraftPoolType() {
        return _draftPoolType;
    }

    @Override
    public List<ArrayList<String>> getDraftPackList() {
        List draftPacksCopy = new ArrayList();
        for (List<String> pack : _draftPackList)
            draftPacksCopy.add(pack);
        return draftPacksCopy;
    }

    @Override
    public int getPacksToDraft() {
        return _packsToDraft;
    }
}
