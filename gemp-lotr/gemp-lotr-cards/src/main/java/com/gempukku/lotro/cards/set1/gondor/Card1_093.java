package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert Aragorn to make Arwen strength +3, or exert Arwen to make Aragorn strength +3.
 */
public class Card1_093 extends AbstractOldEvent {
    public Card1_093() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Arwen's Fate", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Aragorn"), Filters.name("Arwen")));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.name("Arwen"), Filters.name("Aragorn"))) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        Filter filter;
                        if (character.getBlueprint().getName().equals("Aragorn"))
                            filter = Filters.name("Arwen");
                        else
                            filter = Filters.name("Aragorn");
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, filter, 3), Phase.SKIRMISH));
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
