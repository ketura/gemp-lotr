package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Exert the Ring-bearer and a Dwarf to make that Dwarf strength +X, where X equals that Dwarf's resistance.
 */
public class Card11_006 extends AbstractEvent {
    public Card11_006() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Fallen Lord", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.ringBearer) && PlayConditions.canExert(self, game, Race.DWARF, Filters.not(Filters.ringBearer));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.ringBearer));
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, Filters.not(Filters.ringBearer)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        int resistance = game.getModifiersQuerying().getResistance(game.getGameState(), character);
                        if (resistance > 0)
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, character, resistance), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
