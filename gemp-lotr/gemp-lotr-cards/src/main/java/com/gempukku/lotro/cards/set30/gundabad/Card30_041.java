package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 2
 * Site: 3
 * Game Text: Maneuver: Remove (3) and discard this minion from play to discard a follower.
 */
public class Card30_041 extends AbstractMinion {
    public Card30_041() {
        super(1, 5, 2, 3, Race.ORC, Culture.SMAUG, "Yazneg", "Orkish Assassin", true);
		addKeyword(Keyword.WARG_RIDER);
	}

	@Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 3)
				&& PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
					new RemoveTwilightEffect(3));
			action.appendCost(
					new SelfDiscardEffect(self));
            action.appendEffect(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.FOLLOWER));
            return Collections.singletonList(action);
		}
        return null;
    }
}