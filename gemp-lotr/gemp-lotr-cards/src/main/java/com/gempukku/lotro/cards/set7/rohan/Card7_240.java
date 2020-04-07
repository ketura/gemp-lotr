package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [ROHAN] Man. You may add a threat to play this possession anytime you could play
 * a skirmish event.
 */
public class Card7_240 extends AbstractAttachableFPPossession {
    public Card7_240() {
        super(1, 2, 0, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Long Spear");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<? extends Action> getPhaseActionsInHand(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && PlayConditions.canAddThreat(game, self, 1)) {
            final CostToEffectAction playCardAction = PlayUtils.getPlayCardAction(game, self, 0, Filters.any, false);
            playCardAction.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }

}
