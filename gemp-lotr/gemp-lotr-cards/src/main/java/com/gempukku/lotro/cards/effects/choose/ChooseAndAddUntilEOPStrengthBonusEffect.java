package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class ChooseAndAddUntilEOPStrengthBonusEffect extends ChooseActiveCardEffect {
    private CostToEffectAction _action;
    private PhysicalCard _source;
    private Evaluator _bonusEvaluator;

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, PhysicalCard source, String playerId, int bonus, Filterable... selectionFilter) {
        this(action, source, playerId, new ConstantEvaluator(bonus), selectionFilter);
    }

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, PhysicalCard source, String playerId, Evaluator bonusEvaluator, Filterable... selectionFilter) {
        super(source, playerId, "Choose character", selectionFilter);
        _action = action;
        _source = source;
        _bonusEvaluator = bonusEvaluator;
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        final int bonus = _bonusEvaluator.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), card);
        final StrengthModifier modifier = new StrengthModifier(_source, card, null, bonus);
        final Phase phase = game.getGameState().getCurrentPhase();

        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(modifier, phase);
    }
}
