package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
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
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Shoulder to Shoulder");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self));
        }

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && (
                Filters.canSpot(game.getGameState(), game.getModifiersQuerying(),
                        Filters.or(
                                Filters.keyword(Keyword.ELF),
                                Filters.keyword(Keyword.DWARF)),
                        Filters.canExert()))) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Use Shoulder to Shoulder");

            List<Effect> possibleCosts = new LinkedList<Effect>();

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF)) {
                        @Override
                        protected void cardSelected(PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(dwarf));
                            action.addEffect(
                                    new ChooseActiveCardEffect(playerId, "Choose Elf to heal", Filters.keyword(Keyword.ELF)) {
                                        @Override
                                        protected void cardSelected(PhysicalCard elf) {
                                            action.addEffect(new HealCharacterEffect(elf));
                                        }
                                    });
                        }
                    });

            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose Elf to exert", Filters.keyword(Keyword.ELF)) {
                        @Override
                        protected void cardSelected(PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(elf));
                            action.addEffect(
                                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to heal", Filters.keyword(Keyword.DWARF)) {
                                        @Override
                                        protected void cardSelected(PhysicalCard dwarf) {
                                            action.addEffect(new HealCharacterEffect(dwarf));
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
