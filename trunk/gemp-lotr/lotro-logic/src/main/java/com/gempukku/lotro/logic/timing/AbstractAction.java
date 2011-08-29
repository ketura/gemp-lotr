package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;

public abstract class AbstractAction implements Action {
    private PhysicalCard _source;
    private String _text;

    public AbstractAction(PhysicalCard source, String text) {
        _source = source;
        _text = text;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _source;
    }

    @Override
    public String getText() {
        return _text;
    }
}
