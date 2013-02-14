package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collection;

public class SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Filterable[] _filters;
    private Evaluator _evaluator;
    private Phase _phase;

    public SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect(PhysicalCard source, Evaluator evaluator, Phase phase, Filterable... filter) {
        _source = source;
        _evaluator = evaluator;
        _phase = phase;
        _filters = filter;
    }

    @Override
    public String getText(LotroGame game) {
        return "Apply strength modifier";
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        final Collection<PhysicalCard> affectedCards = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters);
        for (PhysicalCard physicalCard : affectedCards) {
            final int modifier = _evaluator.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), physicalCard);
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
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }
}
