package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.actions.lotronly.*;
import com.gempukku.lotro.game.modifiers.ExtraPlayCost;
import com.gempukku.lotro.game.modifiers.Modifier;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractLotroCardBlueprint implements LotroCardBlueprint {
    private final int _twilightCost;
    private final String _name;
    private final String _subTitle;
    private final CardType _cardType;
    private final Side _side;
    private final Culture _culture;
    private final boolean _unique;
    private final Map<Keyword, Integer> _keywords = new HashMap<>();

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
    public boolean checkPlayRequirements(DefaultGame game, PhysicalCard self) {
        return true;
    }

    protected void addKeyword(Keyword keyword) {
        addKeyword(keyword, 1);
    }

    protected void addKeyword(Keyword keyword, int number) {
        _keywords.put(keyword, number);
    }

    public Filterable getValidTargetFilter(String playerId, DefaultGame game, PhysicalCard self) {
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
    public final String getTitle() {
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
    public boolean skipUniquenessCheck() {
        return false;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getInDiscardModifiers(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getControlledSiteModifiers(DefaultGame game, PhysicalCard self) {
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
    public String getTribblePower() { return null; }

    @Override
    public int getTribbleValue() { return 0; }

    @Override
    public int[] getAllyHomeSiteNumbers() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public SitesBlock getAllyHomeSiteBlock() {
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
    public int getTwilightCostModifier(DefaultGame game, PhysicalCard self, PhysicalCard target) {
        return 0;
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return null;
    }

    @Override
    public boolean isExtraPossessionClass(DefaultGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return false;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInHand(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsFromStacked(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, DefaultGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventAfterActions(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventBeforeActions(String playerId, DefaultGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public RequiredTriggerAction getKilledRequiredTrigger(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public OptionalTriggerAction getKilledOptionalTrigger(String playerId, DefaultGame game, PhysicalCard self) {
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
    public List<? extends ExtraPlayCost> getExtraCostToPlay(DefaultGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public int getPotentialDiscount(DefaultGame game, String playerId, PhysicalCard self) {
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(DefaultGame game, CostToEffectAction action, String playerId, PhysicalCard self) {

    }

    @Override
    public boolean canPayAidCost(DefaultGame game, PhysicalCard self) {
        return false;
    }

    @Override
    public void appendAidCosts(DefaultGame game, CostToEffectAction action, PhysicalCard self) {

    }
}
