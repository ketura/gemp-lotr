package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Assignment: Exert Orthanc Champion to assign it to an unbound companion. That companion
 * may exert to prevent this.
 */
public class Card4_164 extends AbstractMinion {
    public Card4_164() {
        super(5, 12, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Othanc Champion", true);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))
                && Filters.and(Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW)).accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion(), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.insertEffect(
                                    new PreventableEffect(action,
                                            new AssignmentEffect(playerId, companion, Collections.singletonList(self)),
                                            game.getGameState().getCurrentPlayerId(),
                                            new ExertCharactersEffect(self, companion)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
