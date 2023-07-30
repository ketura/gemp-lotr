package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;

public class PutCardFromDeckIntoHandEffect extends AbstractEffect {
    private final LotroPhysicalCard _card;
    private final boolean _reveal;

    public PutCardFromDeckIntoHandEffect(LotroPhysicalCard card) {
        this(card, true);
    }

    public PutCardFromDeckIntoHandEffect(LotroPhysicalCard card, boolean reveal) {
        _card = card;
        _reveal = reveal;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put card from deck into hand";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone() == Zone.DECK;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if ((!game.getFormat().hasRuleOfFour() || game.getModifiersQuerying().canDrawCardAndIncrementForRuleOfFour(game, _card.getOwner()))
                && _card.getZone() == Zone.DECK) {
            GameState gameState = game.getGameState();
            if(_reveal) {
                gameState.sendMessage(_card.getOwner() + " puts " + GameUtils.getCardLink(_card) + " from deck into their hand");
            }
            else {
                gameState.sendMessage(_card.getOwner() + " puts a card from deck into their hand");
            }
            gameState.removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            gameState.addCardToZone(game, _card, Zone.HAND);
            game.getActionsEnvironment().emitEffectResult(new DrawCardOrPutIntoHandResult(_card.getOwner()));

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
