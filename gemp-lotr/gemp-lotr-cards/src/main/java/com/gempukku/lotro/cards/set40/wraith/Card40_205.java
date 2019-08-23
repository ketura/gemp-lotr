package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Enquea, Morgul Assassin
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 6
 * Type: Minion - Nazgul
 * Strength: 11
 * Vitality: 4
 * Home: 3
 * Card Number: 1U205
 * Game Text: Fierce.
 * Maneuver: Spot 6 companions (or 5 burdens) and another [RINGWRAITH] minion and exert Ulaire Enquea to wound
 * a companion (except the Ring-bearer).
 */
public class Card40_205 extends AbstractMinion {
    public Card40_205() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.enquea, "Morgul Assassin", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && (PlayConditions.canSpot(game, 6, CardType.COMPANION) || game.getGameState().getBurdens() >= 5)
                && PlayConditions.canSpot(game, Culture.WRAITH, CardType.MINION, Filters.not(self))
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
