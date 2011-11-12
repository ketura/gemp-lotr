package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
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
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Possession • Support Area
 * Game Text: Regroup: If there are fewer than 3 cards stacked here, spot your Orc or [GOLLUM] minion to stack that
 * minion here. Shadow: Play a [GOLLUM] minion stacked here as if played from hand.
 */
public class Card8_030 extends AbstractPermanent {
    public Card8_030() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.GOLLUM, Zone.SUPPORT, "Web");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && game.getGameState().getStackedCards(self).size() < 3
                && PlayConditions.canSpot(game, Filters.owner(playerId), CardType.MINION, Filters.or(Race.ORC, Culture.GOLLUM))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.owner(playerId), CardType.MINION, Filters.or(Race.ORC, Culture.GOLLUM)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new StackCardFromPlayEffect(card, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromStacked(playerId, game, self, CardType.MINION, Culture.GOLLUM)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, CardType.MINION, Culture.GOLLUM));
            return Collections.singletonList(action);
        }
        return null;
    }
}
