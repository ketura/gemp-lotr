package com.gempukku.lotro.cards.set18.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Reinforce a [GOLLUM] token. Each Shadow player may draw a card.
 */
public class Card18_037 extends AbstractEvent {
    public Card18_037() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "Trusted Promise", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ReinforceTokenEffect(self, playerId, Token.GOLLUM));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new DrawCardsEffect(action, playerId, 1)));
        return action;
    }
}
