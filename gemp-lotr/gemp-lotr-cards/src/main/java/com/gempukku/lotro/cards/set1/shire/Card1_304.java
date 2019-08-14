package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert a companion (except a Hobbit) to make a Hobbit strength +3.
 */
public class Card1_304 extends AbstractEvent {
    public Card1_304() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Noble Intentions", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, CardType.COMPANION, Filters.not(Race.HOBBIT));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Race.HOBBIT)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard hobbit) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(hobbit), 3)));
                    }
                });
        return action;
    }
}
