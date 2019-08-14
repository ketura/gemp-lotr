package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 3
 * Type: Event • Regroup
 * Game Text: To play, discard Gollum from play. Heal the Ring-bearer to add a burden. If the fellowship is not in
 * region 1, you may repeat this for each other wound on the Ring-bearer.
 */
public class Card13_057 extends AbstractEvent {
    public Card13_057() {
        super(Side.SHADOW, 3, Culture.GOLLUM, "Trap Is Sprung", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, Filters.gollum)
                && PlayConditions.canHeal(self, game, Filters.ringBearer);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gollum));
        action.appendCost(
                new ChooseAndHealCharactersEffect(action, playerId, Filters.ringBearer));
        action.appendEffect(
                new AddBurdenEffect(self.getOwner(), self, 1));
        action.appendEffect(
                new RepeatEffect(action, playerId, self));
        return action;
    }

    private class RepeatEffect extends UnrespondableEffect {
        private CostToEffectAction _action;
        private String _playerId;
        private PhysicalCard _self;

        private RepeatEffect(CostToEffectAction action, String playerId, PhysicalCard self) {
            _action = action;
            _playerId = playerId;
            _self = self;
        }

        @Override
        protected void doPlayEffect(LotroGame game) {
            if (GameUtils.getRegion(game) != 1
                    && PlayConditions.canHeal(_self, game, Filters.ringBearer)) {
                _action.appendEffect(
                        new PlayoutDecisionEffect(_playerId, new YesNoDecision("Do you want to repeat the effect?") {
                            @Override
                            protected void yes() {
                                _action.appendCost(
                                        new ChooseAndHealCharactersEffect(_action, _playerId, Filters.ringBearer));
                                _action.appendEffect(
                                        new AddBurdenEffect(_self.getOwner(), _self, 1));
                                _action.appendEffect(
                                        new RepeatEffect(_action, _playerId, _self));
                            }
                        }));
            }
        }
    }
}
