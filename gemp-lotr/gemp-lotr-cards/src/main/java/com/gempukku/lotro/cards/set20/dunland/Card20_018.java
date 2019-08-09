package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 5
 * Hillman Pillager
 * Minion • Man
 * 11	1	3
 * Shadow: If stacked on a site you control, play this minion; his twilight cost is -1 for each Free Peoples possession
 * you can spot.
 * Regroup: Spot Saruman or another [Dunland] Man to stack this minion on a site you control.
 * http://lotrtcg.org/coreset/dunland/hillmanpillager(r1).png
 */
public class Card20_018 extends AbstractMinion {
    public Card20_018() {
        super(5, 11, 1, 3, Race.MAN, Culture.DUNLAND, "Hillman Pillager");
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.stackedOn(self, game, Filters.siteControlled(self.getOwner()))
                && checkPlayRequirements(playerId, game, self, 0, -Filters.countSpottable(game, Side.FREE_PEOPLE, CardType.POSSESSION), false, false)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, -Filters.countSpottable(game, Side.FREE_PEOPLE, CardType.POSSESSION), false));
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
