package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChoiceCost;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ChooseableCost;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver: Add (1) and exert a Dwarf to heal an Elf, or add (1) and exert an
 * Elf to heal a Dwarf.
 */
public class Card1_059 extends AbstractPermanent {
    public Card1_059() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.FREE_SUPPORT, "Shoulder to Shoulder");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && (
                Filters.canSpot(game.getGameState(), game.getModifiersQuerying(),
                        Filters.or(
                                Filters.race(Race.ELF),
                                Filters.race(Race.DWARF)),
                        Filters.canExert()))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER, "Use Shoulder to Shoulder");

            List<ChooseableCost> possibleCosts = new LinkedList<ChooseableCost>();

            possibleCosts.add(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.DWARF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Dwarf";
                        }

                        @Override
                        protected void cardsSelected(Collection<PhysicalCard> dwarf, boolean success) {
                            super.cardsSelected(dwarf, success);
                            if (success) {
                                action.appendEffect(
                                        new ChooseAndHealCharacterEffect(action, playerId, "Choose an Elf", Filters.race(Race.ELF)));
                            }
                        }
                    });

            possibleCosts.add(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.ELF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Elf";
                        }

                        @Override
                        protected void cardsSelected(Collection<PhysicalCard> elf, boolean success) {
                            super.cardsSelected(elf, success);
                            if (success) {
                                action.appendEffect(
                                        new ChooseAndHealCharacterEffect(action, playerId, "Choose a Dwarf", Filters.race(Race.DWARF)));
                            }
                        }
                    });

            action.appendCost(new AddTwilightEffect(1));
            action.appendCost(
                    new ChoiceCost(action, playerId, possibleCosts));

            return Collections.singletonList(action);
        }

        return null;
    }
}
