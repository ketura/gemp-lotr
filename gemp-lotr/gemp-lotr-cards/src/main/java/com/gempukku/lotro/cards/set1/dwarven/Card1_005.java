package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
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
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a Dwarf strength +2 and damage +1.
 */
public class Card1_005 extends AbstractLotroCardBlueprint {
    public Card1_005() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Cleaving Blow", "1_5");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            List<Action> actions = new LinkedList<Action>();

            final PlayEventAction action = new PlayEventAction(self);
            List<PhysicalCard> dwarves = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF));
            if (dwarves.size() > 0) {
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose Dwarf", Filters.keyword(Keyword.DWARF)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                                action.addEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new AbstractModifier(self, "Strength +2, Damage +1", Filters.sameCard(dwarf)) {
                                                    @Override
                                                    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                                                        return result + 2;
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
                                                }, Phase.SKIRMISH
                                        )
                                );
                            }
                        }
                );
            }

            actions.add(action);

            return actions;
        }

        return null;
    }
}
