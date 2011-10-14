package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 6
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion you play is -1. Assignment: Spot 2 Hobbit
 * companions to make the Free Peoples player assign a Hobbit to skirmish this minion.
 */
public class Card1_262 extends AbstractMinion {
    public Card1_262() {
        super(2, 6, 2, 6, Race.ORC, Culture.SAURON, "Orc Assassin");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RoamingPenaltyModifier(self, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.type(CardType.COMPANION)) >= 2
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.sameCard(self), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, game.getGameState().getCurrentPlayerId(), "Choose a Hobbit", Filters.race(Race.HOBBIT), Filters.type(CardType.COMPANION), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard hobbit) {
                            action.appendEffect(new AssignmentEffect(hobbit.getOwner(), hobbit, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
