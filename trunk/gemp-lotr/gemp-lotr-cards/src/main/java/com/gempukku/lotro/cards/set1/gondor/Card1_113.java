package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert a ranger at a river or forest to exhaust a minion.
 */
public class Card1_113 extends AbstractEvent {
    public Card1_113() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "A Ranger's Versatility", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.keyword(Keyword.RANGER)));
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(PhysicalCard minion) {
                        action.appendEffect(new CardAffectsCardEffect(self, minion));
                        action.appendEffect(new ExhaustCharacterEffect(playerId, action, minion));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && (game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.RIVER)
                || game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.FOREST))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RANGER), Filters.canExert());
    }
}
