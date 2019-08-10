package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractLotroCardBlueprint implements LotroCardBlueprint {
    private int _twilightCost;
    private String _name;
    private String _subTitle;
    private CardType _cardType;
    private Side _side;
    private Culture _culture;
    private boolean _unique;
    private Map<Keyword, Integer> _keywords = new HashMap<Keyword, Integer>();

    public AbstractLotroCardBlueprint(int twilightCost, Side side, CardType cardType, Culture culture, String name) {
        this(twilightCost, side, cardType, culture, name, null, false);
    }

    public AbstractLotroCardBlueprint(int twilightCost, Side side, CardType cardType, Culture culture, String name, String subTitle, boolean unique) {
        _twilightCost = twilightCost;
        _side = side;
        _cardType = cardType;
        _culture = culture;
        _name = name;
        _subTitle = subTitle;
        _unique = unique;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        if (!game.getModifiersQuerying().canPayExtraCostsToPlay(game, self))
            return false;

        twilightModifier -= game.getModifiersQuerying().getPotentialDiscount(game, self);

        return (getSide() != Side.SHADOW || PlayConditions.canPayForShadowCard(game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty));
    }

    protected void addKeyword(Keyword keyword) {
        addKeyword(keyword, 1);
    }

    protected void addKeyword(Keyword keyword, int number) {
        _keywords.put(keyword, number);
    }

    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public final int getTwilightCost() {
        return _twilightCost;
    }

    @Override
    public final boolean hasKeyword(Keyword keyword) {
        return _keywords.containsKey(keyword);
    }

    @Override
    public final int getKeywordCount(Keyword keyword) {
        Integer count = _keywords.get(keyword);
        if (count == null)
            return 0;
        else
            return count;
    }

    @Override
    public final Culture getCulture() {
        return _culture;
    }

    @Override
    public final CardType getCardType() {
        return _cardType;
    }

    @Override
    public final Side getSide() {
        return _side;
    }

    @Override
    public final String getName() {
        return _name;
    }

    @Override
    public final String getSubtitle() {
        return _subTitle;
    }

    @Override
    public final boolean isUnique() {
        return _unique;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getInDiscardModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getControlledSiteModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public Race getRace() {
        return null;
    }

    @Override
    public int getStrength() {
        return 0;
    }

    @Override
    public int getVitality() {
        return 0;
    }

    @Override
    public int getResistance() {
        return 0;
    }

    @Override
    public int[] getAllyHomeSiteNumbers() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public SitesBlock getAllyHomeSiteBlock() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public boolean isAllyAtHome(int siteNumber, SitesBlock siteBlock) {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public SitesBlock getSiteBlock() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public int getSiteNumber() {
        return 0;
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return 0;
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggersFromHand(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public OptionalTriggerAction getKilledOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public Signet getSignet() {
        return null;
    }

    @Override
    public String getDisplayableInformation(PhysicalCard self) {
        return null;
    }

    @Override
    public Direction getSiteDirection() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {

    }
}
