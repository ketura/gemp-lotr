package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class ChooseAndAddUntilEOPStrengthBonusEffect extends ChooseActiveCardEffect {
    private CostToEffectAction _action;
    private PhysicalCard _source;
    private Evaluator _bonusEvaluator;

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, PhysicalCard source, String playerId, int bonus, Filter... selectionFilter) {
        this(action, source, playerId, new ConstantEvaluator(bonus), selectionFilter);
    }

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, PhysicalCard source, String playerId, Evaluator bonusEvaluator, Filter... selectionFilter) {
        super(source, playerId, "Choose character", selectionFilter);
        _action = action;
        _source = source;
        _bonusEvaluator = bonusEvaluator;
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        SubAction action = new SubAction(_action);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new StrengthModifier(_source, Filters.sameCard(card), null, _bonusEvaluator.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), card)), game.getGameState().getCurrentPhase()));
        game.getActionsEnvironment().addActionToStack(action);
    }
}
