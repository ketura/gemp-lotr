package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.*;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LotroCardBlueprint {
    enum Direction {
        LEFT, RIGHT
    }

    public Side getSide();

    public CardType getCardType();

    public Culture getCulture();

    public Race getRace();

    public boolean isUnique();

    public String getTitle();

    public String getSubtitle();

    public Signet getSignet();

    public boolean hasKeyword(Keyword keyword);

    public int getKeywordCount(Keyword keyword);

    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self);

    public Map<Filter, Integer> getTargetCostModifiers(String playerId, LotroGame game, PhysicalCard self);

    public int getTwilightCost();

    public int getTwilightCostModifier(LotroGame game, PhysicalCard self);

    public int getStrength();

    public int getVitality();

    public int getResistance();

    public int[] getAllyHomeSiteNumbers();

    public SitesBlock getAllyHomeSiteBlock();

    PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);

    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self);

    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, PhysicalCard self);

    public List<? extends Modifier> getInDiscardModifiers(LotroGame game, PhysicalCard self);

    public List<? extends Modifier> getControlledSiteModifiers(LotroGame game, PhysicalCard self);

    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self);

    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self);

    public List<? extends Action> getPhaseActionsInHand(String playerId, LotroGame game, PhysicalCard self);

    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self);

    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self);

    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self);

    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self);


    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self);

    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);


    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self);

    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);


    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);

    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self);


    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);


    public RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(LotroGame game, PhysicalCard self);

    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, LotroGame game, PhysicalCard self);


    public OptionalTriggerAction getKilledOptionalTrigger(String playerId, LotroGame game, PhysicalCard self);

    public SitesBlock getSiteBlock();

    public int getSiteNumber();

    public Set<PossessionClass> getPossessionClasses();

    boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo);

    public Direction getSiteDirection();

    public String getDisplayableInformation(PhysicalCard self);

    List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self);

    int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self);

    void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self);

    boolean canPayAidCost(LotroGame game, PhysicalCard self);

    void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self);

    boolean skipUniquenessCheck();
}
