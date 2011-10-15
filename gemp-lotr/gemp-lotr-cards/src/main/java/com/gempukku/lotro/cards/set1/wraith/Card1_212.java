package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;


/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Search. Maneuver: Exert your Nazgul to discard an ally.
 */
public class Card1_212 extends AbstractOldEvent {
    public Card1_212() {
        super(Side.SHADOW, Culture.WRAITH, "Fear", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an ally", Filters.type(CardType.ALLY)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard ally) {
                        action.appendEffect(
                                new DiscardCardsFromPlayEffect(self, ally));
                    }
                });

        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
