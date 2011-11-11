package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If a Dwarf who is damage +X wins a skirmish, wound a minion not assigned to a skirmish X times.
 */
public class Card8_007 extends AbstractResponseEvent {
    public Card8_007() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Unheard of");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game, effectResult, Race.DWARF, Keyword.DAMAGE)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF, Keyword.DAMAGE, Filters.inSkirmish) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int count = game.getModifiersQuerying().getKeywordCount(game.getGameState(), card, Keyword.DAMAGE);
                            action.insertEffect(
                                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, count, CardType.MINION, Filters.not(Filters.inSkirmish)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
