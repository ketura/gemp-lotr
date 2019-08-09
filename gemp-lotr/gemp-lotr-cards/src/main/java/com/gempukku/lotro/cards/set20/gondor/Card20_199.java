package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * 1
 * Lying in Wait
 * Gondor	Event • Maneuver
 * Discard all Shadow cards borne by a roaming minion.
 */
public class Card20_199 extends AbstractEvent {
    public Card20_199() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Lying in Wait", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a roaming minion", CardType.MINION, Keyword.ROAMING) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new DiscardCardsFromPlayEffect(self.getOwner(), self, Side.SHADOW, Filters.attachedTo(card)));
                    }
                });
        return action;
    }
}
