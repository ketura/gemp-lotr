package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 6
 * •Ulaire Enquea, Morgul Assassin
 * Ringwraith	Minion • Nazgul
 * 11	4	3
 * Fierce.
 * Maneuver: If you can spot 6 companions (or 5 burdens), spot another [Ringwraith] minion
 * and exert Ulaire Enquea to wound a companion (except the Ring-bearer).
 */
public class Card20_306 extends AbstractMinion {
    public Card20_306() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.enquea, "Morgul Assassin", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && (PlayConditions.canSpot(game, 6, CardType.COMPANION) || PlayConditions.canSpotBurdens(game, 5))
                && PlayConditions.canSpot(game, Filters.not(self), Culture.WRAITH, CardType.MINION)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action,  self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
