package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Event
 * Game Text: Regroup: Discard 4 [ISENGARD] orcs to wound a companion 4 times (except the Ring-bearer).
 */
public class Card6_062 extends AbstractEvent {
    public Card6_062() {
        super(Side.SHADOW, 4, Culture.ISENGARD, "Fires and Foul Fumes", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canBeDiscarded(self, game, 4, Culture.ISENGARD, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 4, 4, Culture.ISENGARD, Race.ORC));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 4, CardType.COMPANION, Filters.not(Keyword.RING_BEARER)));
        return action;
    }
}
