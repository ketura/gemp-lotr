package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Regroup
 * Game Text: Spot a Dwarf who is damage +X to place X wounds on minions.
 */
public class Card8_002 extends AbstractEvent {
    public Card8_002() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Battle in Earnest", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Race.DWARF, Keyword.DAMAGE);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF, Keyword.DAMAGE) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = game.getModifiersQuerying().getKeywordCount(game.getGameState(), card, Keyword.DAMAGE);
                        for (int i = 0; i < count; i++)
                            action.appendEffect(
                                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                    }
                });
        return action;
    }
}
