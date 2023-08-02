package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.Preventable;

public class AddTwilightEffect extends AbstractEffect implements Preventable {
    private final LotroPhysicalCard _source;
    private final Evaluator _twilight;
    private boolean _prevented;
    private String _sourceText;

    public AddTwilightEffect(LotroPhysicalCard source, Evaluator twilight) {
        _source = source;
        _twilight = twilight;
        if (source != null)
            _sourceText = GameUtils.getCardLink(source);
    }

    public AddTwilightEffect(LotroPhysicalCard source, int twilight) {
        this(source, new ConstantEvaluator(twilight));
    }

    public void setSourceText(String sourceText) {
        _sourceText = sourceText;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Add (" + getTwilightToAdd(game) + ")";
    }

    @Override
    public Effect.Type getType() {
        return Effect.Type.BEFORE_ADD_TWILIGHT;
    }

    @Override
    public boolean isPrevented(DefaultGame game) {
        return _prevented;
    }

    @Override
    public void prevent() {
        _prevented = true;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (!isPrevented(game)) {
            int count = getTwilightToAdd(game);
            game.getGameState().sendMessage(_sourceText + " added " + count + " twilight");
            game.getGameState().addTwilight(count);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    private int getTwilightToAdd(DefaultGame game) {
        return _twilight.evaluateExpression(game, null);
    }
}
