package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;

public class SubAction implements Action {
    private PhysicalCard _source;
    private Keyword _type;
    private LinkedList<Effect> _effects = new LinkedList<Effect>();

    public SubAction(PhysicalCard source, Keyword type) {
        _source = source;
        _type = type;
    }

    public void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _source;
    }

    @Override
    public Keyword getType() {
        return _type;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect nextEffect() {
        return _effects.poll();
    }
}
