package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;

/**
 * Title: Roll of Thunder
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event - Fellowship or Maneuver
 * Card Number: 1U87
 * Game Text: Spot Gandalf to discard a Shadow possession or Shadow artifact.
 */
public class Card40_087 extends AbstractEvent {
    public Card40_087() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Roll of Thunder", Phase.FELLOWSHIP, Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)));
        return action;
    }
}
