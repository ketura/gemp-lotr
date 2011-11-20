package com.gempukku.lotro.cards.set9.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 5
 * Type: Ally • Home 2
 * Strength: 14
 * Vitality: 9
 * Site: 2
 * Game Text: To play, remove 2 burdens or 2 threats. Skirmish: Spot 2 [SHIRE] companions and exert Tom Bombadil
 * X times, where X is the fellowship's site number, to cancel a skirmish involving a [SHIRE] companion.
 */
public class Card9_052 extends AbstractAlly {
    public Card9_052() {
        super(5, Block.FELLOWSHIP, 2, 14, 9, null, Culture.SHIRE, "Tob Bombadil", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && (PlayConditions.canRemoveBurdens(game, self, 2) || PlayConditions.canRemoveThreat(game, self, 2));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        List<Effect> possibleCosts = new LinkedList<Effect>();

        if (PlayConditions.canRemoveBurdens(game, self, 2)) {
            possibleCosts.add(
                    new RemoveBurdenEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove 2 burdens";
                        }

                        @Override
                        protected FullEffectResult playEffectReturningResult(LotroGame game) {
                            FullEffectResult effectResult = super.playEffectReturningResult(game);
                            action.insertCost(
                                    new RemoveBurdenEffect(self));
                            return effectResult;
                        }
                    });
        }
        possibleCosts.add(
                new RemoveThreatsEffect(self, 2));
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        return action;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION)
                && PlayConditions.canSelfExert(self, game.getGameState().getCurrentSiteNumber(), game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, game.getGameState().getCurrentSiteNumber(), self));
            action.appendEffect(
                    new CancelSkirmishEffect(Culture.SHIRE, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
