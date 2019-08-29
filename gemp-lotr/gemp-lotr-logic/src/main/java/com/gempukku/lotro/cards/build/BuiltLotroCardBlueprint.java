package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.*;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuiltLotroCardBlueprint implements LotroCardBlueprint {
    private String title;
    private String subtitle;
    private boolean unique;
    private Side side;
    private CardType cardType;
    private Culture culture;
    private Race race;
    private Signet signet;
    private Map<Keyword, Integer> keywords;
    private int cost;
    private int strength;
    private int vitality;
    private int resistance;
    private SitesBlock siteBlock;
    private int siteNumber;
    private Set<PossessionClass> possessionClasses;
    private Direction direction;
    private SitesBlock allyHomeBlock;
    private int[] allyHomeSites = new int[0];

    private List<Requirement> requirements;
    private List<FilterableSource> targetFilters;

    private List<ActionSource> requiredBeforeTriggers;
    private List<ActionSource> requiredAfterTriggers;
    private List<ActionSource> optionalBeforeTriggers;
    private List<ActionSource> optionalAfterTriggers;

    private List<ActionSource> beforeActivatedTriggers;
    private List<ActionSource> afterActivatedTriggers;

    private List<ActionSource> inPlayPhaseActions;
    private List<ActionSource> fromStackedPhaseActions;

    private List<ModifierSource> inPlayModifiers;
    private List<ModifierSource> stackedOnModifiers;
    private List<ModifierSource> inDiscardModifiers;
    private List<ModifierSource> controlledSiteModifiers;

    private List<TwilightCostModifierSource> twilightCostModifiers;

    private ActionSource playEventAction;

    // Building methods

    public void setAllyHomeSites(SitesBlock block, int[] numbers) {
        this.allyHomeBlock = block;
        this.allyHomeSites = numbers;
    }

    public void appendBeforeActivatedTrigger(ActionSource actionSource) {
        if (beforeActivatedTriggers == null)
            beforeActivatedTriggers = new LinkedList<>();
        beforeActivatedTriggers.add(actionSource);
    }

    public void appendAfterActivatedTrigger(ActionSource actionSource) {
        if (afterActivatedTriggers == null)
            afterActivatedTriggers = new LinkedList<>();
        afterActivatedTriggers.add(actionSource);
    }

    public void appendRequiredBeforeTrigger(ActionSource actionSource) {
        if (requiredBeforeTriggers == null)
            requiredBeforeTriggers = new LinkedList<>();
        requiredBeforeTriggers.add(actionSource);
    }

    public void appendRequiredAfterTrigger(ActionSource actionSource) {
        if (requiredAfterTriggers == null)
            requiredAfterTriggers = new LinkedList<>();
        requiredAfterTriggers.add(actionSource);
    }

    public void appendOptionalBeforeTrigger(ActionSource actionSource) {
        if (optionalBeforeTriggers == null)
            optionalBeforeTriggers = new LinkedList<>();
        optionalBeforeTriggers.add(actionSource);
    }

    public void appendOptionalAfterTrigger(ActionSource actionSource) {
        if (optionalAfterTriggers == null)
            optionalAfterTriggers = new LinkedList<>();
        optionalAfterTriggers.add(actionSource);
    }

    public void appendPlayRequirement(Requirement requirement) {
        if (requirements == null)
            requirements = new LinkedList<>();
        requirements.add(requirement);
    }

    public void appendInPlayModifier(ModifierSource modifierSource) {
        if (inPlayModifiers == null)
            inPlayModifiers = new LinkedList<>();
        inPlayModifiers.add(modifierSource);
    }

    public void appendStackedOnModifier(ModifierSource modifierSource) {
        if (stackedOnModifiers == null)
            stackedOnModifiers = new LinkedList<>();
        stackedOnModifiers.add(modifierSource);
    }

    public void appendInDiscardModifier(ModifierSource modifierSource) {
        if (inDiscardModifiers == null)
            inDiscardModifiers = new LinkedList<>();
        inDiscardModifiers.add(modifierSource);
    }

    public void appendControlledSiteModifier(ModifierSource modifierSource) {
        if (controlledSiteModifiers == null)
            controlledSiteModifiers = new LinkedList<>();
        controlledSiteModifiers.add(modifierSource);
    }

    public void appendTargetFilter(FilterableSource targetFilter) {
        if (targetFilters == null)
            targetFilters = new LinkedList<>();
        targetFilters.add(targetFilter);
    }

    public void appendInPlayPhaseAction(ActionSource actionSource) {
        if (inPlayPhaseActions == null)
            inPlayPhaseActions = new LinkedList<>();
        inPlayPhaseActions.add(actionSource);
    }

    public void appendFromStackedPhaseAction(ActionSource actionSource) {
        if (fromStackedPhaseActions == null)
            fromStackedPhaseActions = new LinkedList<>();
        fromStackedPhaseActions.add(actionSource);
    }

    public void appendTwilightCostModifier(TwilightCostModifierSource twilightCostModifierSource) {
        if (twilightCostModifiers == null)
            twilightCostModifiers = new LinkedList<>();
        twilightCostModifiers.add(twilightCostModifierSource);
    }

    public void setPlayEventAction(ActionSource playEventAction) {
        this.playEventAction = playEventAction;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setSignet(Signet signet) {
        this.signet = signet;
    }

    public void setKeywords(Map<Keyword, Integer> keywords) {
        this.keywords = keywords;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setSiteBlock(SitesBlock siteBlock) {
        this.siteBlock = siteBlock;
    }

    public void setSiteNumber(int siteNumber) {
        this.siteNumber = siteNumber;
    }

    public void setPossessionClasses(Set<PossessionClass> possessionClasses) {
        this.possessionClasses = possessionClasses;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Implemented methods

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public Culture getCulture() {
        return culture;
    }

    @Override
    public Race getRace() {
        return race;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public Signet getSignet() {
        return signet;
    }

    @Override
    public int getTwilightCost() {
        return cost;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getVitality() {
        return vitality;
    }

    @Override
    public int getResistance() {
        return resistance;
    }

    @Override
    public SitesBlock getSiteBlock() {
        return siteBlock;
    }

    @Override
    public int getSiteNumber() {
        return siteNumber;
    }

    @Override
    public int[] getAllyHomeSiteNumbers() {
        return allyHomeSites;
    }

    @Override
    public SitesBlock getAllyHomeSiteBlock() {
        return allyHomeBlock;
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return possessionClasses;
    }

    @Override
    public Direction getSiteDirection() {
        return direction;
    }

    @Override
    public boolean hasKeyword(Keyword keyword) {
        return keywords != null && keywords.containsKey(keyword);
    }

    @Override
    public int getKeywordCount(Keyword keyword) {
        if (keywords == null)
            return 0;
        Integer count = keywords.get(keyword);
        if (count == null)
            return 0;
        else
            return count;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        if (targetFilters == null)
            return null;

        Filterable[] result = new Filterable[targetFilters.size()];
        for (int i = 0; i < result.length; i++) {
            final FilterableSource filterableSource = targetFilters.get(i);
            result[i] = filterableSource.getFilterable(playerId, game, self, null, null);
        }

        return Filters.and(result);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        return getActivatedActions(playerId, game, self, inPlayPhaseActions);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        return getActivatedActions(playerId, game, self, fromStackedPhaseActions);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return getModifiers(game, self, inPlayModifiers);
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, PhysicalCard self) {
        return getModifiers(game, self, stackedOnModifiers);
    }

    @Override
    public List<? extends Modifier> getInDiscardModifiers(LotroGame game, PhysicalCard self) {
        return getModifiers(game, self, inDiscardModifiers);
    }

    @Override
    public List<? extends Modifier> getControlledSiteModifiers(LotroGame game, PhysicalCard self) {
        return getModifiers(game, self, controlledSiteModifiers);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        if (requirements == null)
            return true;

        for (Requirement requirement : requirements) {
            if (!requirement.accepts(null, null, game, self, null, null))
                return false;
        }

        if (playEventAction != null)
            playEventAction.isValid(null, game, self, null, null);

        return true;
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (twilightCostModifiers == null)
            return 0;

        int result = 0;
        for (TwilightCostModifierSource twilightCostModifier : twilightCostModifiers)
            result += twilightCostModifier.getTwilightCostModifier(game, self);

        return result;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        playEventAction.createAction(action, playerId, game, self, null, null);
        return action;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (requiredBeforeTriggers == null)
            return null;

        List<RequiredTriggerAction> result = new LinkedList<>();

        for (ActionSource requiredBeforeTrigger : requiredBeforeTriggers) {
            if (requiredBeforeTrigger.isValid(null, game, self, null, effect)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                requiredBeforeTrigger.createAction(action, null, game, self, null, effect);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (requiredAfterTriggers == null)
            return null;

        List<RequiredTriggerAction> result = new LinkedList<>();

        for (ActionSource requiredAfterTrigger : requiredAfterTriggers) {
            if (requiredAfterTrigger.isValid(null, game, self, effectResult, null)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                requiredAfterTrigger.createAction(action, null, game, self, effectResult, null);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (optionalBeforeTriggers == null)
            return null;

        List<OptionalTriggerAction> result = new LinkedList<>();

        for (ActionSource optionalBeforeTrigger : optionalBeforeTriggers) {
            if (optionalBeforeTrigger.isValid(null, game, self, null, effect)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                optionalBeforeTrigger.createAction(action, null, game, self, null, effect);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (optionalAfterTriggers == null)
            return null;

        List<OptionalTriggerAction> result = new LinkedList<>();

        for (ActionSource optionalAfterTrigger : optionalAfterTriggers) {
            if (optionalAfterTrigger.isValid(null, game, self, effectResult, null)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                optionalAfterTrigger.createAction(action, null, game, self, effectResult, null);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (beforeActivatedTriggers == null)
            return null;

        List<ActivateCardAction> result = new LinkedList<>();
        for (ActionSource beforeActivatedTrigger : beforeActivatedTriggers) {
            if (beforeActivatedTrigger.isValid(playerId, game, self, null, effect)) {
                ActivateCardAction action = new ActivateCardAction(self);
                beforeActivatedTrigger.createAction(action, playerId, game, self, null, effect);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (afterActivatedTriggers == null)
            return null;

        List<ActivateCardAction> result = new LinkedList<>();
        for (ActionSource afterActivatedTrigger : afterActivatedTriggers) {
            if (afterActivatedTrigger.isValid(playerId, game, self, effectResult, null)) {
                ActivateCardAction action = new ActivateCardAction(self);
                afterActivatedTrigger.createAction(action, playerId, game, self, effectResult, null);
                result.add(action);
            }
        }

        return result;
    }

    // Default implementations - not needed (for now)

    @Override
    public List<? extends Action> getPhaseActionsInHand(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return false;
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {

    }

    @Override
    public Map<Filter, Integer> getTargetCostModifiers(String playerId, LotroGame game, PhysicalCard self) {
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
    public String getDisplayableInformation(PhysicalCard self) {
        return null;
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return false;
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {

    }

    @Override
    public boolean skipUniquenessCheck() {
        return false;
    }

    // Helper methods

    private List<? extends Modifier> getModifiers(LotroGame game, PhysicalCard self, List<ModifierSource> sources) {
        if (sources == null)
            return null;

        List<Modifier> result = new LinkedList<>();
        for (ModifierSource inPlayModifier : sources) {
            result.add(inPlayModifier.getModifier(game, self));
        }
        return result;
    }

    private List<? extends ActivateCardAction> getActivatedActions(String playerId, LotroGame game, PhysicalCard self, List<ActionSource> sources) {
        if (sources == null)
            return null;

        List<ActivateCardAction> result = new LinkedList<>();
        for (ActionSource inPlayPhaseAction : sources) {
            if (inPlayPhaseAction.isValid(playerId, game, self, null, null)) {
                ActivateCardAction action = new ActivateCardAction(self);
                inPlayPhaseAction.createAction(action, playerId, game, self, null, null);
                result.add(action);
            }
        }
        return result;
    }

    public void validateConsistency() throws InvalidCardDefinitionException {
        if (title == null)
            throw new InvalidCardDefinitionException("Card has to have a title");
        if (cardType == null)
            throw new InvalidCardDefinitionException("Card has to have a type");
        if (cardType != CardType.THE_ONE_RING && side == null)
            throw new InvalidCardDefinitionException("Only The One Ring does not have a side defined");
        if (siteNumber != 0
                && cardType != CardType.SITE
                && cardType != CardType.MINION)
            throw new InvalidCardDefinitionException("Only minions and sites have a site number, use siteHome for allies");
        if (cardType == CardType.EVENT && playEventAction == null)
            throw new InvalidCardDefinitionException("Events have to have an event type effect");
        if (cardType != CardType.EVENT && playEventAction != null)
            throw new InvalidCardDefinitionException("Only events should have an event type effect");
    }
}
