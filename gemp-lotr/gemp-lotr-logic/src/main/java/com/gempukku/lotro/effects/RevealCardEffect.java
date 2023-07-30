package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.game.PlayOrder;

import java.util.Collection;
import java.util.Collections;

public class RevealCardEffect extends AbstractSuccessfulEffect {
    private final LotroPhysicalCard _source;
    private final Collection<? extends LotroPhysicalCard> _cards;

    public RevealCardEffect(LotroPhysicalCard source, LotroPhysicalCard card) {
        this(source, Collections.singleton(card));
    }

    public RevealCardEffect(LotroPhysicalCard source, Collection<? extends LotroPhysicalCard> cards) {
        _source = source;
        _cards = cards;
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
    public void playEffect(DefaultGame game) {
        if (_cards.size() > 0) {
            final PlayOrder playerOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_source.getOwner(), false);

            String nextPlayer;
            while ((nextPlayer = playerOrder.getNextPlayer()) != null) {
                game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                        new ArbitraryCardsSelectionDecision(1, "Revealed card(s)", _cards, Collections.emptySet(), 0, 0) {
                            @Override
                            public void decisionMade(String result) {
                            }
                        });
            }

            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed cards - " + getAppendedNames(_cards));
        }
    }
}
