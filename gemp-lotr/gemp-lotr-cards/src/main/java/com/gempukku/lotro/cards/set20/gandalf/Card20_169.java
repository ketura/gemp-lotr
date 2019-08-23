package com.gempukku.lotro.cards.set20.gandalf;

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
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 3
 * Roll of Thunder
 * Gandalf	Event • Fellowship or Maneuver
 * Spot Gandalf to discard a Shadow possession or Shadow artifact.
 */
public class Card20_169 extends AbstractEvent {
    public Card20_169() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Roll of Thunder", Phase.FELLOWSHIP, Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)));
        return action;
    }
}
