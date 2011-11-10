package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Discard all Shadow cards borne by a minion bearing a [GONDOR] fortification.
 */
public class Card5_034 extends AbstractEvent {
    public Card5_034() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Fall Back", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.hasAttached(Culture.GONDOR, Keyword.FORTIFICATION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new DiscardCardsFromPlayEffect(self, Filters.and(Side.SHADOW, Filters.attachedTo(Filters.sameCard(self)))));
                    }
                });
        return action;
    }
}
