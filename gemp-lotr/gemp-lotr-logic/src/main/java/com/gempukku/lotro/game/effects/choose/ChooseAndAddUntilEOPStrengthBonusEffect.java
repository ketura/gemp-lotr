package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.modifiers.StrengthModifier;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class ChooseAndAddUntilEOPStrengthBonusEffect extends ChooseActiveCardEffect {
    private final CostToEffectAction _action;
    private final LotroPhysicalCard _source;
    private final Evaluator _bonusEvaluator;

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, LotroPhysicalCard source, String playerId, int bonus, Filterable... selectionFilter) {
        this(action, source, playerId, new ConstantEvaluator(bonus), selectionFilter);
    }

    public ChooseAndAddUntilEOPStrengthBonusEffect(CostToEffectAction action, LotroPhysicalCard source, String playerId, Evaluator bonusEvaluator, Filterable... selectionFilter) {
        super(source, playerId, "Choose character", selectionFilter);
        _action = action;
        _source = source;
        _bonusEvaluator = bonusEvaluator;
    }

    @Override
    protected void cardSelected(DefaultGame game, LotroPhysicalCard card) {
        final int bonus = _bonusEvaluator.evaluateExpression(game, card);
        final StrengthModifier modifier = new StrengthModifier(_source, Filters.sameCard(card), bonus);

        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(modifier, game.getGameState().getCurrentPhase());

        selectedCharacterCallback(card);
    }

    protected void selectedCharacterCallback(LotroPhysicalCard selectedCharacter) {

    }
}
