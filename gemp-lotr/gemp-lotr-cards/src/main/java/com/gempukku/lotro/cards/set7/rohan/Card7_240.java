package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

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
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SKIRMISH, self)
                && checkPlayRequirements(playerId, game, self, 0)
                && PlayConditions.canAddThreat(game, self, 1)) {
            final AttachPermanentAction playCardAction = getPlayCardAction(playerId, game, self, 0);
            playCardAction.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }

}
