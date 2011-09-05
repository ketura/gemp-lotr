package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Dwarf to wound 2 Orcs or to wound 1 Orc twice.
 */
public class Card1_019 extends AbstractEvent {
    public Card1_019() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Here Lies Balin, Son of Fundin", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard dwarf) {
                        action.addCost(new ExertCharacterEffect(dwarf));
                    }
                }
        );
        action.addEffect(
                new ChooseActiveCardsEffect(playerId, "Choose Orc(s) to wound", 1, 2, Filters.keyword(Keyword.ORC)) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> cards) {
                        if (cards.size() == 2) {
                            action.addEffect(new WoundCharacterEffect(cards.get(0)));
                            action.addEffect(new WoundCharacterEffect(cards.get(1)));
                        } else {
                            action.addEffect(new WoundCharacterEffect(cards.get(0)));
                            action.addEffect(new WoundCharacterEffect(cards.get(0)));
                        }
                    }
                }
        );
        return action;
    }
}
