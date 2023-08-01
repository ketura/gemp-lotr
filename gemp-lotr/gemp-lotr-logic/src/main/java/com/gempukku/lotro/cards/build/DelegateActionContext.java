package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Collection;

public class DelegateActionContext<AbstractGame extends DefaultGame> extends DefaultActionContext<AbstractGame> {
    private final DefaultActionContext<AbstractGame> delegate;

    public DelegateActionContext(DefaultActionContext<AbstractGame> delegate, String performingPlayer, AbstractGame game,
                                 LotroPhysicalCard source, EffectResult effectResult, Effect effect) {
        super(performingPlayer, game, source, effectResult, effect);
        this.delegate = delegate;
    }

    @Override
    public void setValueToMemory(String memory, String value) {
        delegate.setValueToMemory(memory, value);
    }

    @Override
    public String getValueFromMemory(String memory) {
        return delegate.getValueFromMemory(memory);
    }

    @Override
    public void setCardMemory(String memory, LotroPhysicalCard card) {
        delegate.setCardMemory(memory, card);
    }

    @Override
    public void setCardMemory(String memory, Collection<? extends LotroPhysicalCard> cards) {
        delegate.setCardMemory(memory, cards);
    }

    @Override
    public Collection<? extends LotroPhysicalCard> getCardsFromMemory(String memory) {
        return delegate.getCardsFromMemory(memory);
    }

    @Override
    public LotroPhysicalCard getCardFromMemory(String memory) {
        return delegate.getCardFromMemory(memory);
    }

}
