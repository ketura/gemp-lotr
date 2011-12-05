package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Regroup: Spot another [MEN] minion and 2 Free Peoples characters who each have resistance 5 or less
 * to make the move limit -1 for this turn.
 */
public class Card11_099 extends AbstractMinion {
    public Card11_099() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Squad of Haradrim");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.MEN, CardType.MINION)
                && PlayConditions.canSpot(game, 2, Side.FREE_PEOPLE, Filters.character, Filters.maxResistance(5))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, -1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
