package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.TransferToSupportEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canDiscardFromHand(game, self.getOwner(), 1, Culture.GANDALF));
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, self.getOwner(), false, 1, 1, Culture.GANDALF));
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
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

