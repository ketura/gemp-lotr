package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Exert a ranger companion to discard a minion.
 */
public class Card1_106 extends AbstractEvent {
    public Card1_106() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Gondor's Vengeance", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RANGER));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.keyword(Keyword.RANGER), Filters.type(CardType.COMPANION)));
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(PhysicalCard minion) {
                        action.appendEffect(
                                new DiscardCardsFromPlayEffect(self, minion));
                    }
                }
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
