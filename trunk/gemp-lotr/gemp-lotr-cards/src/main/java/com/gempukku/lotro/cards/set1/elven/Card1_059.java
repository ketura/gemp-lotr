package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.HealCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
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
public class Card1_059 extends AbstractLotroCardBlueprint {
    public Card1_059() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Shoulder to Shoulder", "1_59");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
            return Collections.singletonList(action);
        }

        if (game.getGameState().getCurrentPhase() == Phase.MANEUVER
                && (
                Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.canExert(),
                        Filters.or(
                                Filters.keyword(Keyword.ELF),
                                Filters.keyword(Keyword.DWARF))))) {
            final CostToEffectAction action = new CostToEffectAction(self, "Use Shoulder to Shoulder");

            List<Effect> possibleCosts = new LinkedList<Effect>();

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(dwarf));
                            action.addEffect(
                                    new ChooseActiveCardEffect(playerId, "Choose Elf to heal", Filters.keyword(Keyword.ELF)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard elf) {
                                            action.addEffect(new HealCardEffect(elf));
                                        }
                                    });
                        }
                    });

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Elf to exert", Filters.keyword(Keyword.ELF)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(elf));
                            action.addEffect(
                                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to heal", Filters.keyword(Keyword.DWARF)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                                            action.addEffect(new HealCardEffect(dwarf));
                                        }
                                    });
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
