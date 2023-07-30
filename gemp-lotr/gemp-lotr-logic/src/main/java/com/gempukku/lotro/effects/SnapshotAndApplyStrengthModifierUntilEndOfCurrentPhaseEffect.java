package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.StrengthModifier;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final Filterable[] _filters;
    private final Evaluator _evaluator;

    public SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect(LotroPhysicalCard source, int value, Filterable... filter) {
        this(source, new ConstantEvaluator(value), filter);
    }

    public SnapshotAndApplyStrengthModifierUntilEndOfCurrentPhaseEffect(LotroPhysicalCard source, Evaluator evaluator, Filterable... filter) {
        _source = source;
        _evaluator = evaluator;
        _filters = filter;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Apply strength modifier";
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        for (LotroPhysicalCard physicalCard : Filters.filterActive(game, _filters)) {
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
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }
}
