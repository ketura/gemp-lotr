package com.gempukku.lotro.logic.vo;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.LotroCardBlueprint;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LotroDeck {
    private LotroCardBlueprint _ringBearer;
    private LotroCardBlueprint _ring;
    private List<LotroCardBlueprint> _siteCards = new LinkedList<LotroCardBlueprint>();
    private List<LotroCardBlueprint> _nonSiteCards = new LinkedList<LotroCardBlueprint>();

    public void setRingBearer(LotroCardBlueprint ringBearer) {
        _ringBearer = ringBearer;
    }

    public void setRing(LotroCardBlueprint ring) {
        _ring = ring;
    }

    public void addCard(LotroCardBlueprint card) {
        if (card.getCardType() == CardType.SITE)
            _siteCards.add(card);
        else
            _nonSiteCards.add(card);
    }

    public List<LotroCardBlueprint> getAdventureCards() {
        return Collections.unmodifiableList(_nonSiteCards);
    }

    public List<LotroCardBlueprint> getSites() {
        return Collections.unmodifiableList(_siteCards);
    }

    public LotroCardBlueprint getRingBearer() {
        return _ringBearer;
    }

    public LotroCardBlueprint getRing() {
        return _ring;
    }
}
