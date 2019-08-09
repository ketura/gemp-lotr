package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gwaihir, Great Eagle
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Ally - Eagle
 * Strength: 7
 * Vitality: 4
 * Card Number: 1R75
 * Game Text: To play, spot Gandalf.
 * Maneuver: At sites 1-5, exert Gandalf twice to allow Gwaihir to participate in archery fire and skirmishes.
 * Skirmish: At sites 6-9, exert Gwaihir twice to cancel a skirmish involving Gandalf.
 */
public class Card40_075 extends AbstractAlly{
    public Card40_075() {
        super(4, Block.SECOND_ED, 0, 7, 4, Race.EAGLE, Culture.GANDALF, "Gwaihir", "Great Eagle", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.location(game, Filters.siteNumberBetweenInclusive(1, 5))
                && PlayConditions.canExert(self, game, 2, Filters.gandalf)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, self)));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.location(game, Filters.siteNumberBetweenInclusive(6, 9))
                && PlayConditions.canSelfExert(self, 2, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new CancelSkirmishEffect(Filters.gandalf));
            return Collections.singletonList(action);
        }
        return null;
    }
}
