package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.timing.results.AddThreatResult;

public class AddThreatsEffect extends AbstractEffect {
    private Action _action;
    private final String _performingPlayer;
    private final LotroPhysicalCard _source;
    private final Evaluator _count;

    public AddThreatsEffect(String performingPlayer, LotroPhysicalCard source, Evaluator count) {
        _performingPlayer = performingPlayer;
        _source = source;
        _count = count;
    }

    public AddThreatsEffect(String performingPlayer, LotroPhysicalCard source, int count) {
        this(performingPlayer, source, new ConstantEvaluator(count));
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return evaluateCount(game) <= getThreatsPossibleToAdd(game);
    }

    private int evaluateCount(DefaultGame game) {
        return _count.evaluateExpression(game, null);
    }

    @Override
    public String getText(DefaultGame game) {
        int number = evaluateCount(game);
        return "Add " + number + " threat" + ((number > 1) ? "s" : "");
    }

    @Override
    public Type getType() {
        return null;
    }

    private int getThreatsPossibleToAdd(DefaultGame game) {
        return Filters.countActive(game, CardType.COMPANION)
                - game.getGameState().getThreats();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        int count = evaluateCount(game);
        int toAdd = Math.min(count, getThreatsPossibleToAdd(game));
        if (toAdd > 0) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " adds " + GameUtils.formatNumber(toAdd, count) + " threat" + ((toAdd > 1) ? "s" : ""));
            game.getGameState().addThreats(game.getGameState().getCurrentPlayerId(), toAdd);

            for (int i = 0; i < toAdd; i++)
                game.getActionsEnvironment().emitEffectResult(new AddThreatResult(_source));

            return new FullEffectResult(toAdd == count);
        }
        return new FullEffectResult(count == 0);
    }
}
