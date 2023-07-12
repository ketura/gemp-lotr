package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collections;

public class TransferPermanentNotFromPlayEffect extends AbstractEffect {
    private final PhysicalCard _physicalCard;
    private final PhysicalCard _targetCard;

    public TransferPermanentNotFromPlayEffect(PhysicalCard physicalCard, PhysicalCard targetCard) {
        _physicalCard = physicalCard;
        _targetCard = targetCard;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _targetCard.getZone().isInPlay()
                && !_physicalCard.getZone().isInPlay()
                && game.getModifiersQuerying().canHaveTransferredOn(game, _physicalCard, _targetCard);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " transfers " + GameUtils.getCardLink(_physicalCard) + " to " + GameUtils.getCardLink(_targetCard));
            gameState.removeCardsFromZone(_physicalCard.getOwner(), Collections.singleton(_physicalCard));
            gameState.attachCard(game, _physicalCard, _targetCard);

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
