package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.ExtraPlayCost;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.*;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

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

    private List<ActionSource> optionalInHandBeforeActions;
    private List<ActionSource> optionalInHandAfterActions;

    private List<ActionSource> optionalInHandAfterTriggers;

    private List<ActionSource> inPlayPhaseActions;
    private List<ActionSource> fromStackedPhaseActions;

    private List<ModifierSource> inPlayModifiers;
    private List<ModifierSource> stackedOnModifiers;
    private List<ModifierSource> inDiscardModifiers;
    private List<ModifierSource> controlledSiteModifiers;

    private List<TwilightCostModifierSource> twilightCostModifiers;

    private List<ExtraPlayCostSource> extraPlayCosts;
    private List<DiscountSource> discountSources;

    private ActionSource playEventAction;

    private ExtraPossessionClassTest extraPossessionClassTest;

    // Building methods

    public void setAllyHomeSites(SitesBlock block, int[] numbers) {
        this.allyHomeBlock = block;
        this.allyHomeSites = numbers;
    }

    public void setExtraPossessionClassTest(ExtraPossessionClassTest extraPossessionClassTest) {
        this.extraPossessionClassTest = extraPossessionClassTest;
    }

    public void appendDiscountSource(DiscountSource discountSource) {
        if (discountSources == null)
            discountSources = new LinkedList<>();
        discountSources.add(discountSource);
    }

    public void appendOptionalInHandAfterTrigger(ActionSource actionSource) {
        if (optionalInHandAfterTriggers == null)
            optionalInHandAfterTriggers = new LinkedList<>();
        optionalInHandAfterTriggers.add(actionSource);
    }

    public void appendOptionalInHandBeforeAction(ActionSource actionSource) {
        if (optionalInHandBeforeActions == null)
            optionalInHandBeforeActions = new LinkedList<>();
        optionalInHandBeforeActions.add(actionSource);
    }

    public void appendOptionalInHandAfterAction(ActionSource actionSource) {
        if (optionalInHandAfterActions == null)
            optionalInHandAfterActions = new LinkedList<>();
        optionalInHandAfterActions.add(actionSource);
    }

    public void appendExtraPlayCost(ExtraPlayCostSource extraPlayCostSource) {
        if (extraPlayCosts == null)
            extraPlayCosts = new LinkedList<>();
        extraPlayCosts.add(extraPlayCostSource);
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
            result[i] = filterableSource.getFilterable(null);
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
        DefaultActionContext dummy = new DefaultActionContext(self.getOwner(), game, self, null, null);

        if (requirements != null)
            for (Requirement requirement : requirements) {
                if (!requirement.accepts(dummy))
                    return false;
            }

        if (playEventAction != null && !playEventAction.isValid(dummy))
            return false;

        return true;
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (twilightCostModifiers == null)
            return 0;

        DefaultActionContext actionContext = new DefaultActionContext(self.getOwner(), game, self, null, null);

        int result = 0;
        for (TwilightCostModifierSource twilightCostModifier : twilightCostModifiers)
            result += twilightCostModifier.getTwilightCostModifier(actionContext);

        return result;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, null);
        PlayEventAction action = new PlayEventAction(self);
        playEventAction.createAction(action, actionContext);
        return action;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (requiredBeforeTriggers == null)
            return null;

        List<RequiredTriggerAction> result = new LinkedList<>();

        for (ActionSource requiredBeforeTrigger : requiredBeforeTriggers) {
            DefaultActionContext actionContext = new DefaultActionContext(self.getOwner(), game, self, null, effect);
            if (requiredBeforeTrigger.isValid(actionContext)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                requiredBeforeTrigger.createAction(action, actionContext);
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
            DefaultActionContext actionContext = new DefaultActionContext(self.getOwner(), game, self, effectResult, null);
            if (requiredAfterTrigger.isValid(actionContext)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                requiredAfterTrigger.createAction(action, actionContext);
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
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, effect);
            if (optionalBeforeTrigger.isValid(actionContext)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                optionalBeforeTrigger.createAction(action, actionContext);
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
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult, null);
            if (optionalAfterTrigger.isValid(actionContext)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                optionalAfterTrigger.createAction(action, actionContext);
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
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, effect);
            if (beforeActivatedTrigger.isValid(actionContext)) {
                ActivateCardAction action = new ActivateCardAction(self);
                beforeActivatedTrigger.createAction(action, actionContext);
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
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult, null);
            if (afterActivatedTrigger.isValid(actionContext)) {
                ActivateCardAction action = new ActivateCardAction(self);
                afterActivatedTrigger.createAction(action, actionContext);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (optionalInHandBeforeActions == null)
            return null;

        List<PlayEventAction> result = new LinkedList<>();
        for (ActionSource optionalInHandBeforeAction : optionalInHandBeforeActions) {
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, effect);
            if (optionalInHandBeforeAction.isValid(actionContext)) {
                PlayEventAction action = new PlayEventAction(self);
                optionalInHandBeforeAction.createAction(action, actionContext);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (optionalInHandAfterActions == null)
            return null;

        List<PlayEventAction> result = new LinkedList<>();
        for (ActionSource optionalInHandAfterAction : optionalInHandAfterActions) {
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult, null);
            if (optionalInHandAfterAction.isValid(actionContext)) {
                PlayEventAction action = new PlayEventAction(self);
                optionalInHandAfterAction.createAction(action, actionContext);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (optionalInHandAfterTriggers == null)
            return null;

        List<OptionalTriggerAction> result = new LinkedList<>();
        for (ActionSource optionalInHandAfterTrigger : optionalInHandAfterTriggers) {
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult, null);
            if (optionalInHandAfterTrigger.isValid(actionContext)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                optionalInHandAfterTrigger.createAction(action, actionContext);
                result.add(action);
            }
        }

        return result;
    }

    @Override
    public List<? extends ExtraPlayCost> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        if (extraPlayCosts == null)
            return null;

        DefaultActionContext actionContext = new DefaultActionContext(self.getOwner(), game, self, null, null);

        List<ExtraPlayCost> result = new LinkedList<>();
        for (ExtraPlayCostSource extraPlayCost : extraPlayCosts) {
            result.add(extraPlayCost.getExtraPlayCost(actionContext));
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
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        if (extraPossessionClassTest != null)
            return extraPossessionClassTest.isExtraPossessionClass(game, self, attachedTo);
        return false;
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        if (discountSources == null)
            return 0;

        int result = 0;
        DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, null);
        for (DiscountSource discountSource : discountSources)
            result += discountSource.getPotentialDiscount(actionContext);

        return result;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        if (discountSources != null) {
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, null);
            for (DiscountSource discountSource : discountSources) {
                final DiscountEffect discountEffect = discountSource.getDiscountEffect(action, actionContext);
                action.appendPotentialDiscount(discountEffect);
            }
        }
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
            ActionContext actionContext = new DefaultActionContext(null, game, self, null, null);
            result.add(inPlayModifier.getModifier(actionContext));
        }
        return result;
    }

    private List<? extends ActivateCardAction> getActivatedActions(String playerId, LotroGame game, PhysicalCard self, List<ActionSource> sources) {
        if (sources == null)
            return null;

        List<ActivateCardAction> result = new LinkedList<>();
        for (ActionSource inPlayPhaseAction : sources) {
            DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, null, null);
            if (inPlayPhaseAction.isValid(actionContext)) {
                ActivateCardAction action = new ActivateCardAction(self);
                inPlayPhaseAction.createAction(action, actionContext);
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
        if (cardType != CardType.THE_ONE_RING && culture == null)
            throw new InvalidCardDefinitionException("Only The One Ring does not have a culture defined");
        if (siteNumber != 0
                && cardType != CardType.SITE
                && cardType != CardType.MINION)
            throw new InvalidCardDefinitionException("Only minions and sites have a site number, use siteHome for allies");
        if (cardType == CardType.EVENT) {
            List<Keyword> requiredKeywords = Arrays.asList(
                    Keyword.RESPONSE, Keyword.FELLOWSHIP, Keyword.SHADOW, Keyword.MANEUVER, Keyword.ARCHERY, Keyword.ASSIGNMENT,
                    Keyword.SKIRMISH, Keyword.REGROUP);
            if (Collections.disjoint(keywords.keySet(), requiredKeywords))
                throw new InvalidCardDefinitionException("Events have to have a response or phase keyword");

            if (keywords.containsKey(Keyword.RESPONSE)) {
                if (optionalInHandBeforeActions == null && optionalInHandAfterActions == null)
                    throw new InvalidCardDefinitionException("Response events have to have responseEvent type effect");
            } else {
                if (playEventAction == null)
                    throw new InvalidCardDefinitionException("Events have to have an event type effect");
            }
        }
        if (cardType != CardType.EVENT && playEventAction != null)
            throw new InvalidCardDefinitionException("Only events should have an event type effect");
    }
}
