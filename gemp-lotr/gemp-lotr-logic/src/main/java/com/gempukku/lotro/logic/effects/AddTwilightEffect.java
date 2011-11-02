package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;
import com.gempukku.lotro.logic.timing.results.AddTwilightResult;

import java.util.Collections;

public class AddTwilightEffect extends AbstractEffect implements Preventable {
    private PhysicalCard _source;
    private Evaluator _twilight;
    private int _resultTwilight;
    private int _prevented;

    public AddTwilightEffect(PhysicalCard source, Evaluator twilightEvaluator) {
        _source = source;
        _twilight = twilightEvaluator;
    }

    public AddTwilightEffect(PhysicalCard source, int twilight) {
        this(source, new ConstantEvaluator(twilight));
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add (" + _twilight.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null) + ")";
    }

    @Override
    public Effect.Type getType() {
        return Effect.Type.BEFORE_ADD_TWILIGHT;
    }

    @Override
    public boolean isPrevented() {
        return _prevented == _resultTwilight;
    }

    @Override
    public void prevent() {
        _prevented = _resultTwilight;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        _resultTwilight = _twilight.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
        if (!isPrevented()) {
            game.getGameState().sendMessage(_resultTwilight + " gets added to the twilight pool");
            game.getGameState().addTwilight(_resultTwilight);
            return new FullEffectResult(Collections.singleton(new AddTwilightResult(_source)), true, _prevented == 0);
        }
        return new FullEffectResult(null, true, false);
    }
}
