package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
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
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Battle Fury");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public List<? extends Action> getInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(dwarf));
                            List<Modifier> modifiers = new LinkedList<Modifier>();
                            modifiers.add(new StrengthModifier(null, null, 3));
                            modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));

                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CompositeModifier(self, Filters.sameCard(dwarf), modifiers), Phase.SKIRMISH));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
