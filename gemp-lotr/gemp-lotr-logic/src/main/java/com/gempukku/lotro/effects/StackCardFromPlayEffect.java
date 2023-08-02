package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StackCardFromPlayEffect extends AbstractEffect {
    private final LotroPhysicalCard _card;
    private final LotroPhysicalCard _stackOn;

    public StackCardFromPlayEffect(LotroPhysicalCard card, LotroPhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Stack " + GameUtils.getFullName(_card) + " on " + GameUtils.getFullName(_stackOn);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone().isInPlay() && _stackOn.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();

            Set<LotroPhysicalCard> discardedFromPlayCards = new HashSet<>();
            Set<LotroPhysicalCard> toMoveToDiscardCards = new HashSet<>();

            DiscardUtils.cardsToChangeZones(game, Collections.singleton(_card), discardedFromPlayCards, toMoveToDiscardCards);

            Set<LotroPhysicalCard> removeFromPlay = new HashSet<>(toMoveToDiscardCards);
            removeFromPlay.add(_card);

            gameState.removeCardsFromZone(_card.getOwner(), removeFromPlay);

            // And put them in new zones (attached and stacked to discard, the card gets stacked on)
            for (LotroPhysicalCard attachedCard : toMoveToDiscardCards)
                gameState.addCardToZone(game, attachedCard, Zone.DISCARD);

            game.getGameState().sendMessage(GameUtils.getCardLink(_card) + " is stacked on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().stackCard(game, _card, _stackOn);

            // Send the result (attached cards get discarded)
            for (LotroPhysicalCard discardedCard : discardedFromPlayCards)
                game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, null, discardedCard));

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
