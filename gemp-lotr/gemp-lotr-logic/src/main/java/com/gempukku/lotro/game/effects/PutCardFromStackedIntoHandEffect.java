package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;

public class PutCardFromStackedIntoHandEffect extends AbstractEffect {
    private final PhysicalCard _card;

    public PutCardFromStackedIntoHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put card from stacked into hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone() == Zone.STACKED;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (_card.getZone() == Zone.STACKED
                && (!game.getFormat().hasRuleOfFour() || game.getModifiersQuerying().canDrawCardAndIncrementForRuleOfFour(game, _card.getOwner()))) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_card.getOwner() + " puts " + GameUtils.getCardLink(_card) + " from " + GameUtils.getCardLink(_card.getStackedOn()) + " into their hand");
            gameState.removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            gameState.addCardToZone(game, _card, Zone.HAND);
            game.getActionsEnvironment().emitEffectResult(new DrawCardOrPutIntoHandResult(_card.getOwner()));

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}