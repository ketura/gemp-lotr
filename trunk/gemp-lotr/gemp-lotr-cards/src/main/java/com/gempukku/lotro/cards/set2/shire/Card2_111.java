package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert Bilbo to play a Hobbit from your draw deck.
 */
public class Card2_111 extends AbstractOldEvent {
    public Card2_111() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Practically Everyone Was Invited", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Bilbo"));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Bilbo")));
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.race(Race.HOBBIT)));
        return action;
    }
}
