package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public abstract class AbstractModifier implements Modifier {
    private PhysicalCard _physicalCard;
    private String _text;
    private Filter _affectFilter;

    protected AbstractModifier(PhysicalCard source, String text, Filter affectFilter) {
        _physicalCard = source;
        _text = text;
        _affectFilter = affectFilter;
    }

    public PhysicalCard getSource() {
        return _physicalCard;
    }

    public String getText() {
        return _text;
    }

    @Override
    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _affectFilter != null && _affectFilter.accepts(gameState, modifiersQuerying, physicalCard);
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        return result;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, boolean result) {
        return result;
    }

    @Override
    public int getVitality(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, PhysicalCard target, int result) {
        return result;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int strength, int opposingStrength, boolean result) {
        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, boolean result) {
        return result;
    }

    @Override
    public boolean isAllyOnCurrentSite(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard card, boolean allyOnCurrentSite) {
        return allyOnCurrentSite;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
        return result;
    }

    @Override
    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
        return result;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        return result;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, Action action, boolean result) {
        return result;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, boolean result) {
        return result;
    }

    @Override
    public boolean isValidFreePlayerAssignments(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions, boolean result) {
        return result;
    }
}
