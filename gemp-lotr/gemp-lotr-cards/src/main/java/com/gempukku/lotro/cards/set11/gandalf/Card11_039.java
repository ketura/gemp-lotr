package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Skirmish
 * Game Text: Exert a [GANDALF] Wizard and spot 2 companions who each have resistance 7 or more to wound a minion that
 * Wizard is skirmishing twice.
 */
public class Card11_039 extends AbstractEvent {
    public Card11_039() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Prolonged Struggle", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GANDALF, Race.WIZARD)
                && PlayConditions.canSpot(game, 2, CardType.COMPANION, Filters.minResistance(7));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GANDALF, Race.WIZARD) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION, Filters.inSkirmishAgainst(character)));
                    }
                });
        return action;
    }
}
