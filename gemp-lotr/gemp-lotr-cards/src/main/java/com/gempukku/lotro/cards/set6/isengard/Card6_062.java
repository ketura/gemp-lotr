package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, 4, Culture.ISENGARD, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 4, 4, Culture.ISENGARD, Race.ORC));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 4, CardType.COMPANION, Filters.not(Filters.ringBearer)));
        return action;
    }
}
