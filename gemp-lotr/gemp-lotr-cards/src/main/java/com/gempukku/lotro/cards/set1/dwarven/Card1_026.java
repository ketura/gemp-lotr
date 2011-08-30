package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a Dwarf strength +2 (or +4 if at an underground site).
 */
public class Card1_026 extends AbstractLotroCardBlueprint {
    public Card1_026() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Their Halls of Stone", "1_26");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayDuringPhase(game.getGameState(), Phase.SKIRMISH, self)) {
            PlayEventAction action = new PlayEventAction(self);
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf", Filters.keyword(Keyword.DWARF)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                            GameState gameState = game.getGameState();
                            int bonus = (game.getModifiersQuerying().hasKeyword(gameState, gameState.getCurrentSite(), Keyword.UNDERGROUND)) ? 4 : 2;
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new StrengthModifier(self, Filters.sameCard(dwarf), bonus), Phase.SKIRMISH);
                        }
                    }
            );
            return Collections.<Action>singletonList(action);
        }
        return null;
    }
}
