package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver or Skirmish: Remove (3) to heal a Nazgul.
 */
public class Card1_220 extends AbstractPermanent {
    public Card1_220() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "Not Easily Destroyed");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 3)
                || PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self, (game.getGameState().getCurrentPhase() == Phase.MANEUVER) ? Keyword.MANEUVER : Keyword.SKIRMISH);
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndHealCharacterEffect(action, playerId, "Choose a Nazgul", Filters.race(Race.NAZGUL)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
