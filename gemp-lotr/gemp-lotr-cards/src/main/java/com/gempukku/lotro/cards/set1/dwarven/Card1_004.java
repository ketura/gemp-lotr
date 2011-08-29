package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayEventFromHandAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert a Dwarf to make that Dwarf strength +3 and damage +1.
 */
public class Card1_004 extends AbstractLotroCardBlueprint {
    public Card1_004() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Battle Fury", "1_4");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.canExert())) {
            List<Action> actions = new LinkedList<Action>();

            final PlayEventFromHandAction action = new PlayEventFromHandAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(dwarf));
                            action.addEffect(new AddUntilEndOfPhaseModifierEffect(
                                    new AbstractModifier(self, "Strength +3, Damage +1", Filters.sameCard(dwarf)) {
                                        @Override
                                        public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                                            return result + 3;
                                        }

                                        @Override
                                        public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
                                            if (keyword == Keyword.DAMAGE)
                                                return result + 1;
                                            else
                                                return result;
                                        }

                                        @Override
                                        public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
                                            return (result || keyword == Keyword.DAMAGE);
                                        }
                                    }, Phase.SKIRMISH)
                            );
                        }
                    }
            );
            actions.add(action);

            return actions;
        }
        return null;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
