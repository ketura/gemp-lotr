package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.TransferToSupportEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Follower
 * Strength: +1
 * Game Text: Aid - Discard a [GANDALF] card from hand. Skirmish: Transfer Gwaihir to your support area to
 * cancel a skirmish involving bearer and a mounted Orc.
 */
public class Card31_015 extends AbstractFollower {
    public Card31_015() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.GANDALF, "Gwaihir", "Lord of the Eagles", true);
    }

    @Override
    public Race getRace() {
        return Race.EAGLE;
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canDiscardFromHand(game, self.getOwner(), 1, Culture.GANDALF));
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new ChooseAndDiscardCardsFromHandEffect(action, self.getOwner(), false, 1, 1, Culture.GANDALF);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && self.getAttachedTo() != null) {
            final ActivateCardAction action = new ActivateCardAction(self);
            PhysicalCard bearer = self.getAttachedTo();
            action.appendCost(new TransferToSupportEffect(self));
            action.appendEffect(new CancelSkirmishEffect(bearer, Filters.inSkirmishAgainst(Race.ORC, Filters.mounted)));
            return Collections.singletonList(action);
        }
        return null;
    }
}

