package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: Mountain. Battleground. Shadow: Remove 2 doubts to play a minion from your discard pile (Except Smaug)
 */
public class Card30_057 extends AbstractSite {
    public Card30_057() {
        super("Battle of the Five Armies", Block.HOBBIT, 9, 9, Direction.RIGHT);
		addKeyword(Keyword.MOUNTAIN);
		addKeyword(Keyword.BATTLEGROUND);
	}

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && game.getGameState().getBurdens() >= 2
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION, Filters.not(Filters.name("Smaug")))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.MINION, Filters.not(Filters.name("Smaug"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}