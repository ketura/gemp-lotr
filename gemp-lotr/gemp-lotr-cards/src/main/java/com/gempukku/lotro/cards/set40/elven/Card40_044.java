package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.LookAtTopCardOfADeckEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Title: Forewarned
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Condition - Support Area
 * Card Number: 1C44
 * Game Text: To play, spot two Elves.
 * Fellowship: Add (1) to look at the top card of your draw deck. You may discard this condition to discard that card.
 */
public class Card40_044 extends AbstractPermanent {
    public Card40_044() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Forewarned");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Race.ELF);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new LookAtTopCardOfADeckEffect(playerId, 1, playerId));
            action.appendEffect(
                    new OptionalEffect(action, playerId,
                            new SelfDiscardEffect(self) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Discard this condition";
                                }

                                @Override
                                protected void forEachDiscardedByEffectCallback(Collection<PhysicalCard> discardedCards) {
                                    action.appendEffect(
                                            new DiscardTopCardFromDeckEffect(self, playerId, 1, false));
                                }
                            }));

            return Collections.singletonList(action);
        }
        return null;
    }
}
