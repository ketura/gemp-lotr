package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.results.AddThreatResult;

import java.util.Collections;

public class AddThreatsEffect extends AbstractEffect {
    private Action _action;
    private String _performingPlayer;
    private PhysicalCard _source;
    private Evaluator _count;

    public AddThreatsEffect(String performingPlayer, PhysicalCard source, Evaluator count) {
        _performingPlayer = performingPlayer;
        _source = source;
        _count = count;
    }

    public AddThreatsEffect(String performingPlayer, PhysicalCard source, int count) {
        this(performingPlayer, source, new ConstantEvaluator(count));
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return evaluateCount(game) <= getThreatsPossibleToAdd(game);
    }

    private int evaluateCount(LotroGame game) {
        return _count.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
    }

    @Override
    public String getText(LotroGame game) {
        int number = evaluateCount(game);
        return "Add " + number + " threat" + ((number > 1) ? "s" : "");
    }

    @Override
    public Type getType() {
        return null;
    }

    private int getThreatsPossibleToAdd(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)
                - game.getGameState().getThreats();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int count = evaluateCount(game);
        int toAdd = Math.min(count, getThreatsPossibleToAdd(game));
        if (toAdd > 0) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " adds " + toAdd + " threat" + ((toAdd > 1) ? "s" : ""));
            game.getGameState().addThreats(game.getGameState().getCurrentPlayerId(), toAdd);

            return new FullEffectResult(Collections.singleton(new AddThreatResult(_source, toAdd)), toAdd == count, toAdd == count);
        }
        return new FullEffectResult(null, false, false);
    }
}
