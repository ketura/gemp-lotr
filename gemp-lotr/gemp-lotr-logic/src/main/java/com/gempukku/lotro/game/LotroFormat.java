package com.gempukku.lotro.game;

import com.gempukku.lotro.common.JSONDefs;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.adventure.Adventure;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.util.List;
import java.util.Map;

public interface LotroFormat {
    public boolean isOrderedSites();

    public boolean canCancelRingBearerSkirmish();

    public boolean hasRuleOfFour();

    public boolean hasMulliganRule();

    public boolean winWhenShadowReconciles();

    public boolean discardPileIsPublic();

    public boolean winOnControlling5Sites();

    public boolean isPlaytest();
    public boolean hallVisible();

    public String getName();

    public String getCode();
    public int getOrder();

    public String validateCard(String cardId);

    public List<String> validateDeck(LotroDeck deck);
    public String validateDeckForHall(LotroDeck deck);

    public LotroDeck applyErrata(LotroDeck deck);

    public List<Integer> getValidSets();

    public List<String> getBannedCards();

    public List<String> getRestrictedCards();

    public List<String> getValidCards();

    public List<String> getLimit2Cards();

    public List<String> getLimit3Cards();

    public List<String> getRestrictedCardNames();

    public Map<String,String> getErrataCardMap();

    public String applyErrata(String bpID);

    public List<String> findBaseCards(String bpID);

    public SitesBlock getSiteBlock();

    public String getSurveyUrl();

    public int getHandSize();

    public Adventure getAdventure();
    public JSONDefs.Format Serialize();
}
