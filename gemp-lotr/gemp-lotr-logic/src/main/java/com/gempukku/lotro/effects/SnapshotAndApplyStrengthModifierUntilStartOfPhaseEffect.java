package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.StrengthModifier;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

import java.util.Collection;

public class SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final Filterable[] _filters;
    private final Evaluator _evaluator;
    private final Phase _phase;

    public SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect(LotroPhysicalCard source, Evaluator evaluator, Phase phase, Filterable... filter) {
        _source = source;
        _evaluator = evaluator;
        _phase = phase;
        _filters = filter;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Apply strength modifier";
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        final Collection<LotroPhysicalCard> affectedCards = Filters.filterActive(game, _filters);
        for (LotroPhysicalCard physicalCard : affectedCards) {
            final int modifier = _evaluator.evaluateExpression(game, physicalCard);
            if (modifier != 0)
                game.getModifiersEnvironment().addUntilStartOfPhaseModifier(
                        new StrengthModifier(_source, Filters.sameCard(physicalCard), modifier), _phase);
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
