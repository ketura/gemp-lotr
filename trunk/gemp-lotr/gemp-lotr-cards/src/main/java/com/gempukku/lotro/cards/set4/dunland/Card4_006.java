package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: If the fellowship has moved more than once this turn, remove (2)
 * to play a [DUNLAND] Man from your discard pile.
 */
public class Card4_006 extends AbstractPermanent {
    public Card4_006() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Constrantly Threatening");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)
                && game.getGameState().getMoveCount() > 1
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.DUNLAND, Filters.race(Race.MAN), Filters.playable(game, 2)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.and(Culture.DUNLAND, Filters.race(Race.MAN))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
