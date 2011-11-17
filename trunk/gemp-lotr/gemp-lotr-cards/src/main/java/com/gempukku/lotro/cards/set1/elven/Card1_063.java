package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an Elf to discard a [SAURON] minion, a [SAURON] condition, or a [SAURON] possession.
 */
public class Card1_063 extends AbstractOldEvent {
    public Card1_063() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Stand Against Darkness", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.ELF)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose SAURON minion, condition or possession", Culture.SAURON,
                        Filters.or(CardType.MINION, CardType.CONDITION, CardType.POSSESSION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard sauronCard) {
                        action.appendEffect(new DiscardCardsFromPlayEffect(self, sauronCard));
                    }
                }
        );
        return action;
    }
}
