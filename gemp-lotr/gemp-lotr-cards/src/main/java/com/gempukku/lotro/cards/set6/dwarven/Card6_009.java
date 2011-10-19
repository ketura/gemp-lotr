package com.gempukku.lotro.cards.set6.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert Gimli twice to make him and each Elf companion strength +3 until the regroup phase.
 */
public class Card6_009 extends AbstractEvent {
    public Card6_009() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Lend Us Your Aid", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, 2, Filters.name("Gimli"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Gimli")));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new StrengthModifier(self,
                                Filters.or(
                                        Filters.name("Gimli"),
                                        Filters.and(
                                                Race.ELF,
                                                CardType.COMPANION)
                                ), 3), Phase.REGROUP));
        return action;
    }
}
