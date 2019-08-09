package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 3
 * Game Text: Archer. When Narzug is killed or discarded from play (except during the regroup phase), you may remove
 * (3) to wound an ally twice.
 */
public class Card30_036 extends AbstractMinion {
    public Card30_036() {
        super(2, 6, 2, 3, Race.ORC, Culture.GUNDABAD, "Narzug", "Orkish Assassin", true);
		addKeyword(Keyword.ARCHER);
		addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        if ((game.getGameState().getCurrentPhase() != Phase.REGROUP) 
				&& (game.getGameState().getTwilightPool() >= 3)) {
			OptionalTriggerAction action = new OptionalTriggerAction(self);
			action.appendCost(
					new RemoveTwilightEffect(3));
			action.appendEffect(
					new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 2, CardType.ALLY));
			return action;
		}
		return null;
    }
}