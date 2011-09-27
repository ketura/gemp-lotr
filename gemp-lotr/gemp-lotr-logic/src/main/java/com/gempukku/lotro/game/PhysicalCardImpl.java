package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierHook;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;

import java.util.LinkedList;
import java.util.List;

public class PhysicalCardImpl implements PhysicalCard {
    private int _cardId;
    private String _blueprintId;
    private String _owner;
    private Zone _zone;
    private LotroCardBlueprint _blueprint;

    private PhysicalCardImpl _attachedTo;
    private PhysicalCardImpl _stackedOn;

    private List<ModifierHook> _modifierHooks;

    private Object _data;

    public PhysicalCardImpl(int cardId, String blueprintId, String owner, Zone zone, LotroCardBlueprint blueprint) {
        _cardId = cardId;
        _blueprintId = blueprintId;
        _owner = owner;
        _zone = zone;
        _blueprint = blueprint;
    }

    @Override
    public String getBlueprintId() {
        return _blueprintId;
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

    public void startAffectingGame(ModifiersEnvironment modifiersEnvironment) {
        List<Modifier> modifiers = _blueprint.getAlwaysOnModifiers(this);
        if (modifiers != null) {
            _modifierHooks = new LinkedList<ModifierHook>();
            for (Modifier modifier : modifiers)
                _modifierHooks.add(modifiersEnvironment.addAlwaysOnModifier(modifier));
        }
    }

    public void stopAffectingGame() {
        if (_modifierHooks != null) {
            for (ModifierHook modifierHook : _modifierHooks)
                modifierHook.stop();
            _modifierHooks = null;
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
    public PhysicalCard getAttachedTo() {
        return _attachedTo;
    }

    public void stackOn(PhysicalCardImpl physicalCard) {
        _stackedOn = physicalCard;
    }

    @Override
    public PhysicalCard getStackedOn() {
        return _stackedOn;
    }

    @Override
    public void storeData(Object object) {
        _data = object;
    }

    @Override
    public Object getData() {
        return _data;
    }

    @Override
    public void removeData() {
        _data = null;
    }
}
