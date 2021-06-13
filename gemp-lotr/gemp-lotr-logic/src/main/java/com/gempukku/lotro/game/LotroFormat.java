package com.gempukku.lotro.game;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.List;
import java.util.Map;

public interface LotroFormat {
    public boolean isOrderedSites();

    public boolean canCancelRingBearerSkirmish();

    public boolean hasRuleOfFour();

    public boolean hasMulliganRule();

    public boolean winWhenShadowReconciles();

    public boolean winOnControlling5Sites();

    public boolean isPlaytest();

    public String getName();

    public void validateCard(String cardId) throws DeckInvalidException;

    public void validateDeck(LotroDeck deck) throws DeckInvalidException;

    public LotroDeck applyErrata(LotroDeck deck);

    public List<Integer> getValidSets();

    public List<String> getBannedCards();

    public List<String> getRestrictedCards();

    public List<String> getValidCards();

    public List<String> getLimit2Cards();

    public List<String> getLimit3Cards();

    public List<String> getRestrictedCardNames();

    public Map<String,String> getErrataCardMap();

    public SitesBlock getSiteBlock();

    public String getSurveyUrl();

    public int getHandSize();

    public Adventure getAdventure();
}
