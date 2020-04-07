package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Shadow
 * Game Text: Discard Gollum from play to wound an unbound companion who has resistance 6 or less.
 */
public class Card11_043 extends AbstractEvent {
    public Card11_043() {
        super(Side.SHADOW, 2, Culture.GOLLUM, "Horribly Strong", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, Filters.gollum);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gollum));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion, Filters.maxResistance(6)));
        return action;
    }
}
