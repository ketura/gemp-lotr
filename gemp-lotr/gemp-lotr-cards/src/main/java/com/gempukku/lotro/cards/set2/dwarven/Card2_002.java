package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

import java.util.Collection;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert a Dwarf to make that Dwarf defender +1 (or defender +2 if you spot an Orc) until
 * the regroup phase.
 */
public class Card2_002 extends AbstractEvent {
    public Card2_002() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Disquiet of Our People", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.DWARF)) {
                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> characters, boolean success) {
                        super.cardsSelected(characters, success);
                        if (success) {
                            action.appendEffect(
                                    new CardAffectsCardEffect(self, characters));
                            boolean spotsOrc = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ORC));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.in(characters), Keyword.DEFENDER, spotsOrc ? 2 : 1), Phase.REGROUP));
                        }
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
