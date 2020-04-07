package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Regroup: Spot a [SAURON] Orc and remove (3) to stack that minion here.
 * Shadow: Play a [SAURON] Orc stacked here as if played from hand.
 */
public class Card5_110 extends AbstractPermanent {
    public Card5_110() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, "Teeth of Mordor");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && Filters.filter(stackedCards, game, Filters.playable(game)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Culture.SAURON, Race.ORC));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 3)
                && PlayConditions.canSpot(game, Culture.SAURON, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndStackCardFromPlayEffect(action, playerId, self, Culture.SAURON, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }

}
