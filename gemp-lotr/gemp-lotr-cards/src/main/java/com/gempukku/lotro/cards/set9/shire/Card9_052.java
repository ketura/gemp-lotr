package com.gempukku.lotro.cards.set9.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(5, SitesBlock.FELLOWSHIP, 2, 14, 9, null, Culture.SHIRE, "Tom Bombadil", "The Master", true);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self) {
                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                        List<Effect> possibleCosts = new LinkedList<Effect>();
                        possibleCosts.add(
                                new RemoveBurdenEffect(self.getOwner(), self, 2));
                        possibleCosts.add(
                                new RemoveThreatsEffect(self, 2));
                        action.appendCost(
                                new ChoiceEffect(action, self.getOwner(), possibleCosts));
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return (PlayConditions.canRemoveBurdens(game, self, 2) || PlayConditions.canRemoveThreat(game, self, 2));
                    }
                });
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
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
