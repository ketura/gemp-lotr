package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(),
                Filters.or(
                        Filters.race(Race.ELF),
                        Filters.race(Race.DWARF)))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);

            List<Effect> possibleCosts = new LinkedList<Effect>();

            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.DWARF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Dwarf";
                        }

                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
                            super.cardsSelected(game, characters);
                            if (characters.size() > 0)
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, Filters.race(Race.ELF)));
                        }
                    });

            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.ELF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Elf";
                        }

                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
                            super.cardsSelected(game, characters);
                            if (characters.size() > 0)
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, Filters.race(Race.DWARF)));
                        }
                    });

            action.appendCost(new AddTwilightEffect(self, 1));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));

            return Collections.singletonList(action);
        }

        return null;
    }
}
