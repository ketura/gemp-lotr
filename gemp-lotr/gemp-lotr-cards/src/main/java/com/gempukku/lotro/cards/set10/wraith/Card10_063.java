package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: Assignment: Assign this minion to an unbound companion bearing a hand weapon.
 */
public class Card10_063 extends AbstractMinion {
    public Card10_063() {
        super(6, 12, 3, 4, Race.ORC, Culture.WRAITH, "Morgul Vanguard", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canBeAssignedToSkirmishByEffect(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.unboundCompanion, Filters.hasAttached(PossessionClass.HAND_WEAPON)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
