package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert a knight to play a [GONDOR] fortification from your discard pile.
 */
public class Card5_040 extends AbstractEvent {
    public Card5_040() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Take Cover", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Keyword.KNIGHT)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.GONDOR, Keyword.FORTIFICATION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.KNIGHT));
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.GONDOR, Keyword.FORTIFICATION));
        return action;
    }
}
