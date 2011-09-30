package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert 2 Nazgul and spot X burdens to exert X companions.
 */
public class Card2_081 extends AbstractEvent {
    public Card2_081() {
        super(Side.SHADOW, Culture.WRAITH, "They Will Find the Ring", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.race(Race.NAZGUL));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 2, 2, Filters.race(Race.NAZGUL)));
        action.appendEffect(
                new ForEachBurdenYouSpotEffect(playerId) {
                    @Override
                    protected void burdensSpotted(int burdensSpotted) {
                        action.insertEffect(
                                new ChooseAndExertCharactersEffect(action, playerId, burdensSpotted, burdensSpotted, Filters.type(CardType.COMPANION)));
                    }
                });
        return action;
    }
}
