package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Assignment: Make the Free Peoples player assign this minion to an unwounded companion.
 */
public class Card12_135 extends AbstractMinion {
    public Card12_135() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Barbaric Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canBeAssignedToSkirmishByEffect(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, game.getGameState().getCurrentPlayerId(), self, CardType.COMPANION, Filters.unwounded));
            return Collections.singletonList(action);
        }
        return null;
    }
}
