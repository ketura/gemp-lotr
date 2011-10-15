package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Assignment: Spot 5 [MORIA] minions to make the Free Peoples player assign the Ring-bearer to a skirmish.
 */
public class Card1_169 extends AbstractOldEvent {
    public Card1_169() {
        super(Side.SHADOW, Culture.MORIA, "The End Comes", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)) >= 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard ringBearer = game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId());

        if (Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)).size() > 0) {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, game.getGameState().getCurrentPlayerId(), "Choose minion to assign Ring-Bearer to", Filters.type(CardType.MINION), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard minion) {
                            action.appendEffect(
                                    new AssignmentEffect(playerId, ringBearer, minion));
                        }
                    });
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
