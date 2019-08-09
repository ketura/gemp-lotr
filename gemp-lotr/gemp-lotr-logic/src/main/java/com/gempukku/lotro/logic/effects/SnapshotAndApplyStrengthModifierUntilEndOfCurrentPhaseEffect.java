package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Filterable[] _filters;
    private Evaluator _evaluator;

    public SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect(PhysicalCard source, int value, Filterable... filter) {
        this(source, new ConstantEvaluator(value), filter);
    }

    public SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect(PhysicalCard source, Evaluator evaluator, Filterable... filter) {
        _source = source;
        _evaluator = evaluator;
        _filters = filter;
    }

    @Override
    public String getText(LotroGame game) {
        return "Apply strength modifier";
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        for (PhysicalCard physicalCard : Filters.filterActive(game, _filters)) {
            final int modifier = _evaluator.evaluateExpression(game, physicalCard);
            if (modifier != 0)
                game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                        new StrengthModifier(_source, Filters.sameCard(physicalCard), modifier), game.getGameState().getCurrentPhase());
        }

        return new FullEffectResult(true);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }
}
