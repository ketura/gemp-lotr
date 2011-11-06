package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: If you have initiative, discard 2 cards from hand to heal all [ROHAN] allies.
 */
public class Card7_234 extends AbstractEvent {
    public Card7_234() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Guarded Fastness", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, playerId, 2, Filters.any);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
        action.appendEffect(
                new HealCharactersEffect(self, Culture.ROHAN, CardType.ALLY));
        return action;
    }
}
