package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;

public class PutCardFromStackedIntoHandEffect extends AbstractEffect {
    private PhysicalCard _card;

    public PutCardFromStackedIntoHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put card from stacked into hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() == Zone.STACKED;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_card.getZone() == Zone.STACKED
                && game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _card.getOwner())) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_card.getOwner() + " puts " + GameUtils.getCardLink(_card) + " from " + GameUtils.getCardLink(_card.getStackedOn()) + " into his or her hand");
            gameState.removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            gameState.addCardToZone(game, _card, Zone.HAND);

            return new FullEffectResult(Collections.singleton(new DrawCardOrPutIntoHandResult(_card.getOwner(), 1)), true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}