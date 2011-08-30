package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

import java.util.List;

public class CompositeModifier implements Modifier {
    private PhysicalCard _source;
    private Filter _affectFilter;
    private List<Modifier> _modifiers;

    public CompositeModifier(PhysicalCard source, Filter affectFilter, List<Modifier> modifiers) {
        _source = source;
        _affectFilter = affectFilter;
        _modifiers = modifiers;
    }

    @Override
    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Modifier modifier : _modifiers) {
            if (!first)
                sb.append(", ");
            sb.append(modifier.getText());
            first = false;
        }

        return sb.toString();
    }

    @Override
    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _affectFilter.accepts(gameState, modifiersQuerying, physicalCard);
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.hasKeyword(gameState, modifiersQuerying, physicalCard, keyword, result);

        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getKeywordCount(gameState, modifiersQuerying, physicalCard, keyword, result);

        return result;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getStrength(gameState, modifiersQuerying, physicalCard, result);

        return result;
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.appliesStrengthModifier(gameState, modifiersQuerying, modifierSource, result);

        return result;
    }

    @Override
    public int getVitality(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getVitality(gameState, modifiersQuerying, physicalCard, result);

        return result;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getTwilightCost(gameState, modifiersQuerying, physicalCard, result);

        return result;
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getPlayOnTwilightCost(gameState, modifiersQuerying, physicalCard, target, result);

        return result;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int strength, int opposingStrength, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.isOverwhelmedByStrength(gameState, modifiersQuerying, physicalCard, strength, opposingStrength, result);

        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.canTakeWound(gameState, modifiersQuerying, physicalCard, result);

        return result;
    }

    @Override
    public boolean isAllyOnCurrentSite(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean allyOnCurrentSite) {
        for (Modifier modifier : _modifiers)
            allyOnCurrentSite = modifier.isAllyOnCurrentSite(gameState, modifiersQuerying, card, allyOnCurrentSite);

        return allyOnCurrentSite;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getArcheryTotal(gameState, modifiersQuerying, side, result);

        return result;
    }

    @Override
    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
        for (Modifier modifier : _modifiers)
            result = modifier.getMoveLimit(gameState, modifiersQuerying, result);

        return result;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.addsToArcheryTotal(gameState, modifiersQuerying, card, result);

        return result;
    }

    @Override
    public boolean canPlayPhaseActions(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, boolean result) {
        for (Modifier modifier : _modifiers)
            result = modifier.canPlayPhaseActions(gameState, modifiersQuerying, phase, result);

        return result;
    }
}
