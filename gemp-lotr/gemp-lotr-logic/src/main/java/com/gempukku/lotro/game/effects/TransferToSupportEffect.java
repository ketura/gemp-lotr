package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.results.CardTransferredResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransferToSupportEffect extends AbstractEffect {
    private final PhysicalCard _card;

    public TransferToSupportEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone().isInPlay();
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            PhysicalCard transferredFrom = _card.getAttachedTo();

            Set<PhysicalCard> transferredCards = new HashSet<>();
            transferredCards.add(_card);

            final List<PhysicalCard> attachedCards = game.getGameState().getAttachedCards(_card);
            transferredCards.addAll(attachedCards);

            game.getGameState().removeCardsFromZone(_card.getOwner(), transferredCards);
            game.getGameState().addCardToZone(game, _card, Zone.SUPPORT);
            for (PhysicalCard attachedCard : attachedCards)
                game.getGameState().attachCard(game, attachedCard, _card);

            game.getActionsEnvironment().emitEffectResult(
                    new CardTransferredResult(_card, transferredFrom, null));

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
