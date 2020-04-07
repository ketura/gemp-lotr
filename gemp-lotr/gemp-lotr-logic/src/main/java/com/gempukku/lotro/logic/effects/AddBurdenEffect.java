package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;

public class AddBurdenEffect extends AbstractEffect implements Preventable {
    private String _performingPlayer;
    private PhysicalCard _source;
    private Evaluator _count;
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
    public boolean isPrevented(LotroGame game) {
        return _prevented >= getBurdensToAdd(game);
    }

    @Override
    public void prevent() {
        _prevented++;
    }

    public void preventAll() {
        _prevented = Integer.MAX_VALUE;
    }

    private int getBurdensToAdd(LotroGame game) {
        return _count.evaluateExpression(game, null);
    }

    @Override
    public String getText(LotroGame game) {
        final int burdensToAdd = getBurdensToAdd(game) - _prevented;
        return "Add " + burdensToAdd + " burden" + ((burdensToAdd > 1) ? "s" : "");
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getModifiersQuerying().canAddBurden(game, _performingPlayer, _source);
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_ADD_BURDENS;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
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
