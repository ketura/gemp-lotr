package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.Preventable;
import com.gempukku.lotro.game.timing.results.AddBurdenResult;

public class AddBurdenEffect extends AbstractEffect implements Preventable {
    private final String _performingPlayer;
    private final PhysicalCard _source;
    private final Evaluator _count;
    private int _prevented;

    public AddBurdenEffect(String performingPlayer, PhysicalCard source, int count) {
        this(performingPlayer, source, new ConstantEvaluator(count));
    }

    public AddBurdenEffect(String performingPlayer, PhysicalCard source, Evaluator count) {
        _performingPlayer = performingPlayer;
        _source = source;
        _count = count;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public boolean isPrevented(DefaultGame game) {
        return _prevented >= getBurdensToAdd(game);
    }

    @Override
    public void prevent() {
        _prevented++;
    }

    public void preventAll() {
        _prevented = Integer.MAX_VALUE;
    }

    private int getBurdensToAdd(DefaultGame game) {
        return _count.evaluateExpression(game, null);
    }

    @Override
    public String getText(DefaultGame game) {
        final int burdensToAdd = getBurdensToAdd(game) - _prevented;
        return "Add " + burdensToAdd + " burden" + ((burdensToAdd > 1) ? "s" : "");
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getModifiersQuerying().canAddBurden(game, _performingPlayer, _source);
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_ADD_BURDENS;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        int burdensEvaluated = getBurdensToAdd(game);
        if (isPlayableInFull(game) && _prevented < burdensEvaluated) {
            int toAdd = burdensEvaluated - _prevented;
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " adds " + GameUtils.formatNumber(toAdd, burdensEvaluated) + " burden" + ((toAdd > 1) ? "s" : ""));
            game.getGameState().addBurdens(toAdd);
            for (int i = 0; i < toAdd; i++)
                game.getActionsEnvironment().emitEffectResult(new AddBurdenResult(_performingPlayer, _source));
            return new FullEffectResult(_prevented == 0);
        }
        return new FullEffectResult(false);
    }
}
