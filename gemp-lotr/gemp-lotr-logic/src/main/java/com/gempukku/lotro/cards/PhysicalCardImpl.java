package com.gempukku.lotro.cards;

import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.cards.build.ActionSource;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Modifier;
import com.gempukku.lotro.modifiers.ModifierHook;

import java.util.LinkedList;
import java.util.List;

public class PhysicalCardImpl implements LotroPhysicalCard {
    protected int _cardId;
    protected final String _blueprintId;
    protected final String _owner;
    protected String _cardController;
    protected Zone _zone;
    protected final LotroCardBlueprint _blueprint;

    protected PhysicalCardImpl _attachedTo;
    protected PhysicalCardImpl _stackedOn;

    protected List<ModifierHook> _modifierHooks;
    protected List<ModifierHook> _modifierHooksStacked;
    protected List<ModifierHook> _modifierHooksInDiscard;
    protected List<ModifierHook> _modifierHooksControlledSite;

    private Object _whileInZoneData;

    private Integer _siteNumber;

    public PhysicalCardImpl(int cardId, String blueprintId, String owner, LotroCardBlueprint blueprint) {
        _cardId = cardId;
        _blueprintId = blueprintId;
        _owner = owner;
        _blueprint = blueprint;
    }

    public void setCardId(int cardId) {
        _cardId = cardId;
    }

    @Override
    public String getBlueprintId() {
        return _blueprintId;
    }

    @Override
    public String getImageUrl() {
        return _blueprint.getImageUrl();
    }

    public void setZone(Zone zone) {
        _zone = zone;
    }

    @Override
    public Zone getZone() {
        return _zone;
    }

    @Override
    public String getOwner() {
        return _owner;
    }

    public void setCardController(String siteController) {
        _cardController = siteController;
    }

    @Override
    public String getCardController() {
        return _cardController;
    }

    public void startAffectingGame(DefaultGame game) {
        List<? extends Modifier> modifiers = _blueprint.getInPlayModifiers(game, this);
        if (modifiers != null) {
            _modifierHooks = new LinkedList<>();
            for (Modifier modifier : modifiers)
                _modifierHooks.add(game.getModifiersEnvironment().addAlwaysOnModifier(modifier));
        }
    }

    public void stopAffectingGame() {
        if (_modifierHooks != null) {
            for (ModifierHook modifierHook : _modifierHooks)
                modifierHook.stop();
            _modifierHooks = null;
        }
    }

    public void startAffectingGameStacked(DefaultGame game) {
        List<? extends Modifier> modifiers = _blueprint.getStackedOnModifiers(game, this);
        if (modifiers != null) {
            _modifierHooksStacked = new LinkedList<>();
            for (Modifier modifier : modifiers)
                _modifierHooksStacked.add(game.getModifiersEnvironment().addAlwaysOnModifier(modifier));
        }
    }

    public void stopAffectingGameStacked() {
        if (_modifierHooksStacked != null) {
            for (ModifierHook modifierHook : _modifierHooksStacked)
                modifierHook.stop();
            _modifierHooksStacked = null;
        }
    }

    public void startAffectingGameInDiscard(DefaultGame game) {
        List<? extends Modifier> modifiers = _blueprint.getInDiscardModifiers(game, this);
        if (modifiers != null) {
            _modifierHooksInDiscard = new LinkedList<>();
            for (Modifier modifier : modifiers)
                _modifierHooksInDiscard.add(game.getModifiersEnvironment().addAlwaysOnModifier(modifier));
        }
    }

    public void stopAffectingGameInDiscard() {
        if (_modifierHooksInDiscard != null) {
            for (ModifierHook modifierHook : _modifierHooksInDiscard)
                modifierHook.stop();
            _modifierHooksInDiscard = null;
        }
    }

    public void startAffectingGameControlledSite(DefaultGame game) {
        List<? extends Modifier> modifiers = _blueprint.getControlledSiteModifiers(game, this);
        if (modifiers != null) {
            _modifierHooksControlledSite = new LinkedList<>();
            for (Modifier modifier : modifiers)
                _modifierHooksControlledSite.add(game.getModifiersEnvironment().addAlwaysOnModifier(modifier));
        }
    }

    public void stopAffectingGameControlledSite() {
        if (_modifierHooksControlledSite != null) {
            for (ModifierHook modifierHook : _modifierHooksControlledSite)
                modifierHook.stop();
            _modifierHooksControlledSite = null;
        }
    }

    @Override
    public int getCardId() {
        return _cardId;
    }

    @Override
    public LotroCardBlueprint getBlueprint() {
        return _blueprint;
    }

    public void attachTo(PhysicalCardImpl physicalCard) {
        _attachedTo = physicalCard;
    }

    @Override
    public LotroPhysicalCard getAttachedTo() {
        return _attachedTo;
    }

    public void stackOn(PhysicalCardImpl physicalCard) {
        _stackedOn = physicalCard;
    }

    @Override
    public LotroPhysicalCard getStackedOn() {
        return _stackedOn;
    }

    @Override
    public Object getWhileInZoneData() {
        return _whileInZoneData;
    }

    @Override
    public void setWhileInZoneData(Object object) {
        _whileInZoneData = object;
    }

    @Override
    public Integer getSiteNumber() {
        return _siteNumber;
    }

    @Override
    public void setSiteNumber(Integer number) {
        _siteNumber = number;
    }

    public List<OptionalTriggerAction> getOptionalAfterTriggerActions(String playerId, DefaultGame game,
                                                                      EffectResult effectResult,
                                                                      LotroPhysicalCard self) {
        List<OptionalTriggerAction> result = null;

        if (_blueprint.getOptionalAfterTriggers() != null) {
            result = new LinkedList<>();
            for (ActionSource optionalAfterTrigger : _blueprint.getOptionalAfterTriggers()) {
                DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult,
                        null);
                if (optionalAfterTrigger.isValid(actionContext)) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    optionalAfterTrigger.createAction(action, actionContext);
                    result.add(action);
                }
            }

        }

        if (_blueprint.getCopiedFilters() != null) {
            if (result == null)
                result = new LinkedList<>();
            for (FilterableSource copiedFilter : _blueprint.getCopiedFilters()) {
                DefaultActionContext actionContext = new DefaultActionContext(playerId, game, self, effectResult,
                        null);
                final LotroPhysicalCard firstActive = Filters.findFirstActive(game, copiedFilter.getFilterable(actionContext));
                if (firstActive != null)
                    addAllNotNull(result, firstActive.getOptionalAfterTriggerActions(playerId, game,
                            effectResult, self));
            }
        }

        return result;
    }

    private <T> void addAllNotNull(List<T> list, List<? extends T> possiblyNullList) {
        if (possiblyNullList != null)
            list.addAll(possiblyNullList);
    }

}