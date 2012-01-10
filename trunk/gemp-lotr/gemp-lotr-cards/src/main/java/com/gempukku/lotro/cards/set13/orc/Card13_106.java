package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Regroup: Spot an [ORC] lurker minion to stack that minion here. Shadow: Remove a burden to play a lurker
 * minion stacked here as if from hand. That minion is twilight cost -3.
 */
public class Card13_106 extends AbstractPermanent {
    public Card13_106() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Enemy Upon Enemy");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Culture.ORC, Keyword.LURKER, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose an ORC lurker minion", Culture.ORC, Keyword.LURKER, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new StackCardFromPlayEffect(card, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)
                && PlayConditions.canPlayFromStacked(playerId, game, 0, -3, self, CardType.MINION, Keyword.LURKER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, -3, CardType.MINION, Keyword.LURKER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
