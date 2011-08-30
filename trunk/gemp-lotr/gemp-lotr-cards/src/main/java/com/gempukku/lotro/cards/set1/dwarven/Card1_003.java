package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a Dwarf strength +2 (or +3 if bearing a [DWARVEN] hand weapon).
 */
public class Card1_003 extends AbstractLotroCardBlueprint {
    public Card1_003() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Axe Strike", "1_3");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            List<Action> actions = new LinkedList<Action>();

            final PlayEventAction action = new PlayEventAction(self);
            List<PhysicalCard> dwarves = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF));
            if (dwarves.size() > 0) {
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose Dwarf", Filters.keyword(Keyword.DWARF)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                                List<PhysicalCard> attachedDwarvenHandWeapons = Filters.filter(game.getGameState().getAttachedCards(dwarf), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HAND_WEAPON), Filters.culture(Culture.DWARVEN));
                                int bonus = (attachedDwarvenHandWeapons.size() == 0) ? 2 : 3;
                                action.addEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(dwarf), bonus), Phase.SKIRMISH
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

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
