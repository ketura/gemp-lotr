package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert Aragorn to make Arwen strength +3, or exert Arwen to make Aragorn strength +3.
 */
public class Card1_093 extends AbstractEvent {
    public Card1_093() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Arwen's Fate", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.or(Filters.aragorn, Filters.arwen), Filters.canExert(self));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.arwen, Filters.aragorn)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        Filter filter;
                        if (character.getBlueprint().getName().equals("Aragorn"))
                            filter = Filters.arwen;
                        else
                            filter = Filters.aragorn;
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, filter, 3)));
                    }
                }
        );

        return action;
    }
}
