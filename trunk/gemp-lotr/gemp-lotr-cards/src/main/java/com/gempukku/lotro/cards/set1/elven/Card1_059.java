package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Use Shoulder to Shoulder");

            List<Effect> possibleCosts = new LinkedList<Effect>();

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.race(Race.DWARF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Dwarf";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(playerId, dwarf));
                            action.addEffect(
                                    new ChooseAndHealCharacterEffect(action, playerId, "Choose an Elf", false, Filters.race(Race.ELF)));
                        }
                    });

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Elf to exert", Filters.race(Race.ELF)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Elf";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(playerId, elf));
                            action.addEffect(
                                    new ChooseAndHealCharacterEffect(action, playerId, "Choose a Dwarf", false, Filters.race(Race.DWARF)));
                        }
                    });

            action.addCost(new AddTwilightEffect(1));
            action.addCost(
                    new ChoiceEffect(action, playerId, possibleCosts, true));

            return Collections.singletonList(action);
        }

        return null;
    }
}
