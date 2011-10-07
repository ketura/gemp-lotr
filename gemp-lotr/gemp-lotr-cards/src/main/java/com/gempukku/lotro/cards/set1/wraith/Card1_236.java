package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. Assignment: Spot 4 burdens to assign a companion (except the Ring-bearer) to skirmish
 * Ulaire Toldea.
 */
public class Card1_236 extends AbstractMinion {
    public Card1_236() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Toldea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && game.getGameState().getBurdens() >= 4) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.ASSIGNMENT);
            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.sameCard(self), Filters.notAssigned(), Filters.canBeAssignedToSkirmish(Side.SHADOW))) {
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion (except the Ring-Bearer", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER)), Filters.canBeAssignedToSkirmish(Side.SHADOW)) {
                            @Override
                            protected void cardSelected(PhysicalCard companion) {
                                action.appendEffect(
                                        new AssignmentEffect(playerId, companion, Collections.singletonList(self), "Ulaire Toldea effect"));
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
