package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignMinionToCompanionEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Assignment: Spot 5 [MORIA] minions to make the Free Peoples player assign the Ring-bearer to a skirmish.
 */
public class Card1_169 extends AbstractEvent {
    public Card1_169() {
        super(Side.SHADOW, 0, Culture.MORIA, "The End Comes", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 5, Culture.MORIA, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard ringBearer = game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId());
        action.appendEffect(
                new ChooseAndAssignMinionToCompanionEffect(action, game.getGameState().getCurrentPlayerId(), ringBearer, Filters.any));
        return action;
    }
}
