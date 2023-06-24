package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;

public class PutCardFromDeckIntoHandOrDiscardEffect extends AbstractEffect {
    private final PhysicalCard _physicalCard;
    private final boolean _reveal;

    public PutCardFromDeckIntoHandOrDiscardEffect(PhysicalCard physicalCard, boolean reveal) {
        _physicalCard = physicalCard;
        _reveal = reveal;
    }

    public PhysicalCard getCard() {
        return _physicalCard;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put card from deck into hand";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_physicalCard.getZone() == Zone.DECK) {
            var gameState = game.getGameState();
            if ((!game.getFormat().hasRuleOfFour() || game.getModifiersQuerying().canDrawCardAndIncrementForRuleOfFour(game, _physicalCard.getOwner()))) {
                if(_reveal) {
                    gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from deck into their hand");
                }
                else {
                    gameState.sendMessage(_physicalCard.getOwner() + " puts a card from deck into their hand");
                }
                gameState.removeCardsFromZone(_physicalCard.getOwner(), Collections.singleton(_physicalCard));
                gameState.addCardToZone(game, _physicalCard, Zone.HAND);
                game.getActionsEnvironment().emitEffectResult(new DrawCardOrPutIntoHandResult(_physicalCard.getOwner()));
                return new FullEffectResult(true);
            } else {
                gameState.sendMessage(_physicalCard.getOwner() + " discards " + GameUtils.getCardLink(_physicalCard) + " from deck due to Rule of 4");
                gameState.removeCardsFromZone(_physicalCard.getOwner(), Collections.singleton(_physicalCard));
                gameState.addCardToZone(game, _physicalCard, Zone.DISCARD);
            }
        }
        return new FullEffectResult(false);
    }
}
