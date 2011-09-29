package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
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
public class Card1_063 extends AbstractEvent {
    public Card1_063() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Stand Against Darkness", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.ELF)));
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose SAURON minion, condition or possession", Filters.culture(Culture.SAURON),
                        Filters.or(Filters.type(CardType.MINION), Filters.type(CardType.CONDITION), Filters.type(CardType.POSSESSION))) {
                    @Override
                    protected void cardSelected(PhysicalCard sauronCard) {
                        action.appendEffect(new CardAffectsCardEffect(self, sauronCard));
                        action.appendEffect(new DiscardCardsFromPlayEffect(self, sauronCard));
                    }
                }
        );
        return action;
    }
}
