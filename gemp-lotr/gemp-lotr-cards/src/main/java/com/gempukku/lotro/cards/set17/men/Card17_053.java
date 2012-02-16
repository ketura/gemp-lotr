package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Maneuver: Discard this minion and X minions stacked on a [MEN] possession to play a [MEN] minion from your
 * discard pile. That minion is twilight cost -X.
 */
public class Card17_053 extends AbstractMinion {
    public Card17_053() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Stampeding Savage");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 0, Integer.MAX_VALUE, Filters.and(Culture.MEN, CardType.POSSESSION), CardType.MINION) {
                        @Override
                        protected void discardingCardsCallback(Collection<PhysicalCard> cards) {
                            int count = cards.size();
                            action.appendEffect(
                                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -count, Culture.MEN, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
