package com.gempukku.lotro.cards.set21.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
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
public class Card21_57 extends AbstractSite {
    public Card21_57() {
        super("Battle of the Five Armies", Block.HOBBIT, 9, 9, Direction.RIGHT);
		addKeyword(keyword.MOUNTAIN);
		addKeyword(keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && game.getGameState().getBurdens() >= 2
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION, Filters.not(Filters.name("Smaug"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.MINION, Filters.not(Filters.name("Smaug")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
