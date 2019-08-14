package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.ringBearer) && PlayConditions.canExert(self, game, Race.DWARF, Filters.not(Filters.ringBearer));
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
                        int resistance = game.getModifiersQuerying().getResistance(game, character);
                        if (resistance > 0)
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, character, resistance)));
                    }
                });
        return action;
    }
}
