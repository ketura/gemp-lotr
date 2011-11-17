package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert a companion (except a Hobbit) to make a Hobbit strength +3.
 */
public class Card1_304 extends AbstractOldEvent {
    public Card1_304() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Noble Intentions", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Filters.not(Filters.race(Race.HOBBIT)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.race(Race.HOBBIT))));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Filters.race(Race.HOBBIT)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard hobbit) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(hobbit), 3), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
