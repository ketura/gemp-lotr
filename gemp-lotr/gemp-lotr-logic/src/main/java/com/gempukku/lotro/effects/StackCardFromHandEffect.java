package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collections;

public class StackCardFromHandEffect extends AbstractEffect {
    private final LotroPhysicalCard _card;
    private final LotroPhysicalCard _stackOn;

    public StackCardFromHandEffect(LotroPhysicalCard card, LotroPhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(_card.getOwner() + " stacks " + GameUtils.getCardLink(_card) + " from hand on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            game.getGameState().stackCard(game, _card, _stackOn);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    @Override
    public String getText(DefaultGame game) {
        return "Stack " + GameUtils.getFullName(_card) + " from hand on " + GameUtils.getFullName(_stackOn);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone() == Zone.HAND;
    }
}
