package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Hillman Primitive
 * Dunland	Minion • Man
 * 6	1	3
 * Shadow: If stacked on a site you control, play Hillman Primitive to take a [Dunland] possession
 * from your discard pile into hand.
 * Regroup: Spot Saruman or another [Dunland] Man to stack Hillman Primitive on a site you control.
 */
public class Card20_017 extends AbstractMinion {
    public Card20_017() {
        super(1, 6, 1, 3, Race.MAN, Culture.DUNLAND, "Hillman Primitive");
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.stackedOn(self, game, Filters.siteControlled(self.getOwner()))
                && checkPlayRequirements(playerId, game, self, 0, -1, false, false)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self.getStackedOn(), self));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.DUNLAND, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.or(Filters.saruman, Filters.and(Filters.not(self), Culture.DUNLAND, Race.MAN)))
                && PlayConditions.controllsSite(game, playerId)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose site you control", Filters.siteControlled(playerId)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new StackCardFromPlayEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
