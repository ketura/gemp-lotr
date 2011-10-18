package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;

public class RevealCardEffect extends AbstractSuccessfulEffect {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;

    public RevealCardEffect(PhysicalCard source, PhysicalCard card) {
        this(source, Collections.singleton(card));
    }

    public RevealCardEffect(PhysicalCard source, Collection<PhysicalCard> cards) {
        _source = source;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (_cards.size() > 0)
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed cards - " + getAppendedNames(_cards));
        return null;
    }
}
