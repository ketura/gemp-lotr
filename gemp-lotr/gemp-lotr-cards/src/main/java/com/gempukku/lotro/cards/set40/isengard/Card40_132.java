package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.StackPlayedEventOnACardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromStackedToHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Title: *The Palantir of Orthanc, Corruptor of Wizards
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Artifact - Palantir
 * Card Number: 1R132
 * Game Text: To play, spot an [ISENGARD] minion.
 * Response: If a spell is played, spot Saruman to stack that spell here.
 * Shadow: Spot Saruman and remove a threat to take an [ISENGARD] spell stacked here into hand.
 */
public class Card40_132 extends AbstractPermanent {
    public Card40_132() {
        super(Side.SHADOW, 0, CardType.ARTIFACT, Culture.ISENGARD, "The Palantir or Orthanc", "Corruptor of Wizards", true);
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return Collections.singleton(PossessionClass.PALANTIR);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ISENGARD, CardType.MINION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Filters.saruman)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPutCardFromStackedToHandEffect(action, playerId, 1, 1, self, Culture.ISENGARD, Keyword.SPELL));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Keyword.SPELL, CardType.EVENT)
                && PlayConditions.canSpot(game, Filters.saruman)) {
            PlayEventResult playEventResult = (PlayEventResult) effectResult;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new StackPlayedEventOnACardEffect(playEventResult.getPlayEventAction(), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
