package com.gempukku.lotro.cards.set5.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Twilight Cost: 6
 * Type: Site
 * Site: 7T
 * Game Text: Regroup: Spot your minion and remove (9) to make the fellowship move again this turn (if the move
 * limit allows).
 */
public class Card5_118 extends AbstractSite {
    public Card5_118() {
        super("Hornburg Wall", Block.TWO_TOWERS, 7, 6, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canSpot(game, CardType.MINION, Filters.owner(playerId))
                && game.getGameState().getTwilightPool() >= 9) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(9));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new SpecialFlagModifier(self, ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
