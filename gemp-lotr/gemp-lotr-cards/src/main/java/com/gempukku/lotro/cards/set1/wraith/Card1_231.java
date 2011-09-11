package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 11
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Maneuver: Spot 6 companions (or 5 burdens) and exert Ulaire Enquea to wound a companion (except
 * the Ring-bearer).
 */
public class Card1_231 extends AbstractMinion {
    public Card1_231() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Enquea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)
                && (
                Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 6
                        || game.getGameState().getBurdens() >= 5)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert Ulaire Enquea to wound a companion (except the Ring-bearer).");
            action.addCost(
                    new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a companion (except a Ring-Bearer)", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(
                                    new WoundCharacterEffect(companion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
