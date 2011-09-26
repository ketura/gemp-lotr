package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert a Hobbit to make him strength +3.
 */
public class Card1_293 extends AbstractEvent {
    public Card1_293() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Halfling Deftness", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.HOBBIT), Filters.canExert()) {
                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> hobbit, boolean success) {
                        super.cardsSelected(hobbit, success);
                        if (success) {
                            action.appendEffect(new CardAffectsCardEffect(self, hobbit));
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.in(hobbit), 3), Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
