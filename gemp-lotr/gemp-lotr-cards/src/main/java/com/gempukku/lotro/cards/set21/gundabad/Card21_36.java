package com.gempukku.lotro.cards.set21.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

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
public class Card21_36 extends AbstractMinion {
    public Card21_36() {
        super(2, 6, 2, 3, Race.ORC, Culture.GUNDABAD, "Narzug", "Orkish Assassin", true);
		addKeyword(Keyword.ARCHER);
    }

    @Override
    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() != Phase.REGROUP)) 
				&& game.getGameState().getTwilightPool() >= 3) {
			OptionalTriggerAction action = new RequiredTriggerAction(self);
			action.appendCost(
					new RemoveTwilightEffect(3));
			action.appendEffect(
					new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 2, CardType.ALLY));
			return action;
    }
}