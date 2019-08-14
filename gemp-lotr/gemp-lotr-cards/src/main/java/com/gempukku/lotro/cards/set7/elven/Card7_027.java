package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Archery
 * Game Text: Exert 2 [ELVEN] companions to discard the top 3 cards of your draw deck. Make the fellowship archery
 * total +1 for each [ELVEN] card discarded.
 */
public class Card7_027 extends AbstractEvent {
    public Card7_027() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Mirkwood Bowman", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 1, 2, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ELF, CardType.COMPANION));
        action.appendEffect(
                new DiscardTopCardFromDeckEffect(self, playerId, 3, false) {
                    @Override
                    protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                        int elvenCount = Filters.filter(cards, game, Culture.ELVEN).size();
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, elvenCount)));
                    }
                });
        return action;
    }
}
