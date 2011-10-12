package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;

public class StackCardFromHandEffect extends AbstractEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromHandEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(_card.getOwner() + " stacks " + GameUtils.getCardLink(_card) + " from hand on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().removeCardsFromZone(Collections.singleton(_card));
            game.getGameState().stackCard(_card, _stackOn);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack " + GameUtils.getCardLink(_card) + " from hand on " + GameUtils.getCardLink(_stackOn);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() == Zone.HAND;
    }
}
