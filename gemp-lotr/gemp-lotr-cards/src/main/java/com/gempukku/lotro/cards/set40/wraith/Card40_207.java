package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Nelya, Drawn to the Ring
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 5
 * Type: Minion - Nazgul
 * Strength: 10
 * Vitality: 3
 * Home: 3
 * Card Number: 1R207
 * Game Text: Fierce.
 * Assignment: Spot a companion with 2 or less resistance (except the Ring-bearer) to assign Ulaire Nelya to skirmish that companion.
 */
public class Card40_207 extends AbstractMinion {
   public Card40_207() {
       super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.nelya, "Drawn to the Ring", true);
       addKeyword(Keyword.FIERCE);
   }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, self, Filters.notAssignedToSkirmish)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(2), Filters.not(Filters.ringBearer))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, CardType.COMPANION, Filters.maxResistance(2), Filters.not(Filters.ringBearer)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
