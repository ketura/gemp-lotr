package com.gempukku.lotro.cards.set9.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCharacterFromPlayInDeadPileEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to discard each minion skirmishing a Wizard. Place that Wizard in your
 * dead pile. Fellowship or Regroup: Play a Wizard (even if another copy of that Wizard is in your dead pile).
 */
public class Card9_027 extends AbstractPermanent {
    public Card9_027() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Sent Back");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.MINION, Filters.inSkirmishAgainst(Race.WIZARD)));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            PhysicalCard wizard = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Race.WIZARD, Filters.inSkirmish);
                            if (wizard != null)
                                action.appendEffect(
                                        new PutCharacterFromPlayInDeadPileEffect(wizard));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
