package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierHook;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;

public class PhysicalCardImpl implements PhysicalCard {
    private int _cardId;
    private String _owner;
    private Zone _zone;
    private LotroCardBlueprint _blueprint;

    private PhysicalCardImpl _attachedTo;

    private ModifierHook _modifierHook;

    public PhysicalCardImpl(int cardId, String owner, Zone zone, LotroCardBlueprint blueprint) {
        _cardId = cardId;
        _owner = owner;
        _zone = zone;
        _blueprint = blueprint;
    }

    public void setZone(Zone zone) {
        _zone = zone;
    }

    public Zone getZone() {
        return _zone;
    }

    public String getOwner() {
        return _owner;
    }

    public void startAffectingGame(ModifiersEnvironment modifiersEnvironment) {
        Modifier modifier = _blueprint.getAlwaysOnEffect(this);
        if (modifier != null)
            _modifierHook = modifiersEnvironment.addAlwaysOnModifier(modifier);
    }

    public void stopAffectingGame() {
        if (_modifierHook != null) {
            _modifierHook.stop();
            _modifierHook = null;
        }
    }

    public int getCardId() {
        return _cardId;
    }

    public LotroCardBlueprint getBlueprint() {
        return _blueprint;
    }

    public void attachTo(PhysicalCardImpl physicalCard) {
        _attachedTo = physicalCard;
    }

    public PhysicalCard getAttachedTo() {
        return _attachedTo;
    }
}
