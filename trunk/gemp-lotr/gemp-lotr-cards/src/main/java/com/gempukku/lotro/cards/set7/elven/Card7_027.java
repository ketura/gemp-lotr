package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExertMultiple(self, game, 1, 2, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ELF, CardType.COMPANION));
        action.appendEffect(
                new DiscardTopCardFromDeckEffect(self, playerId, 3, false) {
                    @Override
                    protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                        int elvenCount = Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Culture.ELVEN).size();
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, elvenCount), Phase.ARCHERY));
                    }
                });
        return action;
    }
}
